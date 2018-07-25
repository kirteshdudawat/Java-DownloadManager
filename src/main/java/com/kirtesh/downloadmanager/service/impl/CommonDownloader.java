package com.kirtesh.downloadmanager.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import com.kirtesh.downloadmanager.factory.RequestRoutingFactory;
import com.kirtesh.downloadmanager.metadata.DownloadMetadata;
import com.kirtesh.downloadmanager.service.Downloader;
import com.kirtesh.downloadmanager.utils.CommonUtils;
/**
 * 
 * @author kirteshdudawat
 *
 * 
 * Uses URLConnection class, Supports HTTP, HTTPS, FTP protocol file downloads.
 * This class only downloads file. No Validation or handling are done in it. 
 * Its expected that if Execution has reached implementation of Downloader Interface means it has passed all validation and filepath / name paths.
 *
 */
public class CommonDownloader implements Downloader {
	
	public final static Logger logger = Logger.getLogger(CommonDownloader.class);
	
	private CommonUtils commonUtils = RequestRoutingFactory.getCommonUtils();
	
	/**
	 * This methods provides implementation of Downloading file based on HTTP, HTTPS, and FTP protocols only.
	 * This method handles ConnectionTimeout, readTimeOut, and BufferSize based on protocol on which file is downloaded.
	 * Protocol for file to be downloaded is passed in DownloadMetadata along with Download URL and Save URL. 
	 */
	@Override
	public boolean download(DownloadMetadata downloadMetadata) throws IOException{
		int connectionTimeout = commonUtils.getConnectionTimeout(downloadMetadata.getProtocols());
		int readTimeout = commonUtils.getReadTimeout(downloadMetadata.getProtocols());
		int bufferSize = commonUtils.getBufferSize(downloadMetadata.getProtocols());
		return download(downloadMetadata.getDownloadUrl(), downloadMetadata.getFilePath(), connectionTimeout,
				readTimeout, bufferSize);
	}

	/**
	 * File Downloading is Done using java.io package. 
	 * URLConnection class is used to make connection. Only size equivalent to bufferSize would be read and written at one time.
	 * 
	 * Note: If Download stops in between, file would be Auto-Deleted before execution exits the method. 
	 * Logging would be done if Application fails to close opened streams.
	 * 
	 * @param downloadUrl - URL of file which needs to be downloaded.
	 * @param location - location where file needs to be saved on Local Disk
	 * @param connectTimeout - Specify connectionTimeout for URLConnection class.
	 * @param readTimeout - Specify readTimeout for URLConnection class.
	 * @param bufferSize - Max buffer available for downloading (Buffer size is based on properties specified for a protocol). Downloading of file would takes place be reading bufferSize bytes multiple times till we completely download the file.
	 * @return - Boolean value true / false, representing if Download was successful or not.
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public boolean download(String downloadUrl, String location, int connectTimeout, int readTimeout,
			int bufferSize) throws MalformedURLException, IOException {
		boolean isSuccess = false;

		InputStream inputStream = null;
		FileOutputStream outputStream = null;
		File file = new File(location);
		try {
			URL url = new URL(downloadUrl);
			URLConnection conn = url.openConnection();
			conn.setConnectTimeout(connectTimeout);
			conn.setReadTimeout(readTimeout);
			inputStream = conn.getInputStream();
			outputStream = new FileOutputStream(file);
			byte[] buffer = new byte[bufferSize];
			int bytesRead = -1;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}
			isSuccess = true;
		} catch(UnknownHostException e){
			logger.info("UnknownHostException occured while closing Downloading.."+e.getMessage());
		}finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					logger.info("IOException occured while closing output Stream.."+e.getMessage());
				}
			}
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.info("IOException occured while closing input Stream.."+e.getMessage());
				}
			}

			if (!isSuccess) {
				file.delete();
			}
		}
		return isSuccess;
	}

}
