package com.kirtesh.downloadmanager.utils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.kirtesh.downloadmanager.cache.DMCache;
import com.kirtesh.downloadmanager.enums.Protocols;
import com.kirtesh.downloadmanager.metadata.DownloadMetadata;
/**
 * 
 * @author kirteshdudawat
 * 
 * This class contains utility methods linked to Download eg. Preprocessing etc. 
 *
 */
public class DownloadUtils {

	final static Logger logger = Logger.getLogger(DownloadUtils.class);

	private static final String OPENING_BRACE = "(";
	private static final String CLOSING_BRACE = ")";

	/**
	 * This methods creates Metadata associated with URL's to download. It performs following task:
	 * 1. Validate URL, ignore if not valid.
	 * 2. Validate Protocol, ignore if not valid.
	 * 3. If local directory to save file does not exists
	 * 	3.1. Creates Directory if property 'create.download.filepath' is set true.
	 *  3.2. If 'create.download.filepath' set to false, ignore all URL's of same protocol.
	 * 4. Create Local File Name same as file to be Downloaded and validates if same name exists
	 *  4.1. If 'override.existing.file' property is set to true, we persist the same name of file and previous data is lost.
	 *  4.2  If 'override.existing.file' property is set to false, a new name is created. Eg. if 'download' file already existed with override.existing.file=false, new file would be saved as 'download(1)'. 
	 *   
	 * @return Map of URL as key to DownloadMetadata as value.
	 */
	public Map<String, DownloadMetadata> downloadPreprocessing() {

		Map<String, DownloadMetadata> urlToMetadataMapping = new HashMap<>();
		Set<String> fileNameSet = new HashSet<>();

		for (String url : DMCache.downloadUrl) {
			DownloadMetadata downloadMetaData = new DownloadMetadata();
			downloadMetaData.setDownloadUrl(url.trim());

			boolean setProtocol = setProtocolToDownloadMetadata(url, downloadMetaData);

			if (!setProtocol) {
				continue;
			}

			String directoryPath = getDirectoryPathForDownloadedFile(downloadMetaData.getProtocols());
			if (null != directoryPath) {
				String fileName = generateDownloadedFileName(directoryPath, downloadMetaData, fileNameSet);
				fileNameSet.add(fileName);
				urlToMetadataMapping.put(url, downloadMetaData);
			}
		}
		return urlToMetadataMapping;
	}

	/**
	 * This method generates Filename for file on local disk.
	 * 
	 * 1. Create Local File Name same as file to be Downloaded and validates if same name exists
	 *  1.1. If 'override.existing.file' property is set to true, we persist the same name of file and previous data is lost.
	 *  1.2  If 'override.existing.file' property is set to false, a new name is created. Eg. if 'download' file already existed with override.existing.file=false, new file would be saved as 'download(1)'. 
	 * 
	 * @param directoryPath - Path where downloaded file would be saved on local disk.
	 * @param downloadMetaData - Metadata required for downloading file
	 * @param fileNameSet - Set of unique filename contained in list provided with URLs to be downloaded.
	 * @return new generated file name.
	 */
	public String generateDownloadedFileName(String directoryPath, DownloadMetadata downloadMetaData,
			Set<String> fileNameSet) {
		String fileName = downloadMetaData.getDownloadUrl()
				.substring(downloadMetaData.getDownloadUrl().lastIndexOf("/") + 1);

		if(DMCache.overrideExisitngFile()) {
			downloadMetaData.setFilePath(directoryPath + fileName);
			return fileName;
		}
		
		Path file = Paths.get(directoryPath + fileName);
		int count = 1;
		int splitPoint = fileName.indexOf(".");
		String newFileName = fileName;
		while (Files.exists(file) || fileNameSet.contains(newFileName)) {
			if (splitPoint != -1) {
				newFileName = fileName.substring(0, splitPoint) + OPENING_BRACE + count + CLOSING_BRACE
						+ fileName.substring(splitPoint);
			} else {
				newFileName = fileName + OPENING_BRACE + count + CLOSING_BRACE;
			}
			file = Paths.get(directoryPath + newFileName);
			count++;
		}
		downloadMetaData.setFilePath(directoryPath + newFileName);
		return newFileName;
	}

	/**
	 * This methods sets Protocol from URL to Download Metadata.
	 * 
	 * @param url - URL to be downloaded.
	 * @param downloadMetaData - metadata in which protocol would be updated.
	 * @return Boolean true / false based on if Protocol was successfully set in Download Metadata or not.
	 */
	public boolean setProtocolToDownloadMetadata(String url, DownloadMetadata downloadMetaData) {
		try {
			URI uri = new URI(url.trim());
			downloadMetaData.setProtocols(Protocols.valueOf(uri.getScheme().toUpperCase()));
			return true;
		} catch (URISyntaxException e) {
			logger.error("Ignoring URL : " + url + "  as parsing resulted in Invalid URL "+e.getMessage());
		}
		return false;
	}

	/**
	 * Validates if the download path for specific protocol exist or not. If not, creates if properties are set accordingly in validateAndReturnPath(downloadDirectoryPath)
	 * 
	 * @param protocols - protocol on which file is to be downloaded.
	 * @return Directory path for Local system where downloaded file would be saved in String.
	 */
	public String getDirectoryPathForDownloadedFile(Protocols protocols) {

		switch (protocols) {
		case FTP:
			return validateAndReturnPath(DMCache.getFtpDownloadDirectoryPath());
		case SFTP:
			return validateAndReturnPath(DMCache.getSftpDownloadDirectoryPath());
		case HTTP:
			return validateAndReturnPath(DMCache.getHttpDownloadDirectoryPath());
		case HTTPS:
			return validateAndReturnPath(DMCache.getHttpsDownloadDirectoryPath());
		default:
			logger.error("Invalid Protocol Specified..");
			return null;
		}
	}

	/**
	 * 1. If local directory 'downloadDirectoryPath' to save file does not exists
	 * 	1.1. Creates Directory if property 'create.download.filepath' is set true.
	 *  1.2. If 'create.download.filepath' set to false, ignore all URL's of same protocol.
	 * 
	 * 
	 * @param downloadDirectoryPath - directory path where downloaded file would be kept.
	 * @return Directory path for Local system where downloaded file would be saved in String.
	 */
	public String validateAndReturnPath(String downloadDirectoryPath) {

		Path path = Paths.get(downloadDirectoryPath);
		if (!Files.exists(path)) {
			if (DMCache.createDownloadFilepathIfNotExist()) {
				try {
					Files.createDirectories(path);
					return downloadDirectoryPath;
				} catch (IOException e) {
					logger.error("Unable to create Download Directory..");
				}
			}
			return null;
		}
		return downloadDirectoryPath;
	}

}
