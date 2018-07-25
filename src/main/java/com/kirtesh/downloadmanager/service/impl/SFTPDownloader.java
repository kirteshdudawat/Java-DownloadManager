package com.kirtesh.downloadmanager.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.kirtesh.downloadmanager.cache.DMCache;
import com.kirtesh.downloadmanager.enums.Protocols;
import com.kirtesh.downloadmanager.factory.RequestRoutingFactory;
import com.kirtesh.downloadmanager.metadata.DownloadMetadata;
import com.kirtesh.downloadmanager.service.Downloader;
import com.kirtesh.downloadmanager.utils.CommonUtils;
/**
 * 
 * @author kirteshdudawat
 *
 * 
 * Uses jcraft.jsch Dependency, Supports SFTP protocol file downloads.
 * This class only downloads file. No Validation or handling are done in it. 
 * Its expected that if Execution has reached implementation of Downloader Interface means it has passed all validation and filepath / name paths.
 *
 */

public class SFTPDownloader implements Downloader {
	
	final static Logger logger = Logger.getLogger(SFTPDownloader.class);
	
	public CommonUtils commonUtils = RequestRoutingFactory.getCommonUtils();

	/**
	 * This methods provides implementation of Downloading file based on SFTP protocol only.
	 * This method handles ChannelTimeout, SessionTimeout, Port and BufferSize based on file to be downloaded.
	 */
	@Override
	public boolean download(DownloadMetadata downloadMetadata) throws IOException {
		return initiateDownload(downloadMetadata.getDownloadUrl(), downloadMetadata.getFilePath(),
				DMCache.getSftpSessionTimeoutInMillis(), DMCache.getSftpChannelTimeoutInMillis(),
				DMCache.getSftpDefultPort());
	}
	
	/**
	 * This method fetches username, password from download URL. Only size equivalent to bufferSize would be read and written at one time.
	 * 
	 * Note: If Download stops in between, file would be Auto-Deleted before execution exits the method. 
	 * Logging would be done if Application fails to close opened streams.
	 * 
	 * @param downloadUrl - Actual URL for file download
	 * @param location - Actual path of Local Disk where file would be saved.
	 * @param sessionTimeout - Used to set SessionTimeout for SFTP downloads.
	 * @param channelTimeout - Used to set channeltimeout for channel to SFTP download.
	 * @param defaultPort - Port to listen to for FileDownload
	 * @return Boolean value true / false, based on wheather download is successful or not.
	 * @throws IOException on Exception occured during IO
	 */
	public boolean initiateDownload(String downloadUrl, String location, int sessionTimeout, int channelTimeout,
			int defaultPort) throws IOException {
	
		boolean isSuccess = false;
		URI uri = null;
		try {
			uri = new URI(downloadUrl);
		} catch (URISyntaxException e1) {
			logger.error("Stopping SFTP Download, URI Syntax Exception.. Aborting Download for " + downloadUrl);
			return false;
		}
		
		String username = null;
		String password = null;
		if (uri.getUserInfo() != null && !uri.getUserInfo().isEmpty()) {
			String[] userInfo = uri.getUserInfo().split(":");
			username = userInfo[0];
			password = userInfo.length == 2 ? userInfo[1] : null;
		}

		String host = uri.getHost();
		int port = uri.getPort() == -1 ? defaultPort : uri.getPort();
		String remotePath = uri.getPath();

		InputStream inputStream = null;
		FileOutputStream outputStream = null;
		File file = null;
		try {
			file = new File(location);
			ChannelSftp sftpChannel = openConnection(username, password, host, port, sessionTimeout, channelTimeout);
			inputStream = sftpChannel.get(remotePath);
			outputStream = writeToFile(inputStream, file);
			isSuccess = true;
		} catch (IOException | JSchException | SftpException e) {
			StringBuffer sb = new StringBuffer().append("Stopping SFTP Download, Exception.. Aborting Download for ").append(downloadUrl).append("with Exception").append(e.getMessage());
			logger.error(sb.toString());
			return false;
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					logger.info("Exception while closing SFTP output Stream"+e.getMessage());
				}
			}
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.info("Exception while closing SFTP input Stream"+e.getMessage());
				}
			}
			if (!isSuccess && file != null) {
				file.delete();
			}
		}
		return isSuccess;
	}

	/**
	 * 
	 * This methods opens SSH FTP connection for allowing file transfer.
	 * 
	 * @param username - Username for SSH connection
	 * @param password - password for SSH connection
	 * @param host - host where file is hosted
	 * @param port - port, to connect to for SFTP downloads.
	 * @param channelTimeout - Used to set Channel timeout.
	 * @param sessionTimeout - Used to set Session timeout.
	 * @return Opened SFTP Channel is returned between local & host system.
	 * @throws JSchException
	 * @throws SftpException
	 */
	private ChannelSftp openConnection(String username, String password, String host, int port, int channelTimeout,
			int sessionTimeout) throws JSchException, SftpException {
		
		JSch jsch = new JSch();
		Session session = jsch.getSession(username, host, port);
		session.setPassword(password);
		Properties props = new Properties();
		props.put("StrictHostKeyChecking", "no");
		session.setConfig(props);
		session.setTimeout(sessionTimeout);
		session.connect(sessionTimeout);

		Channel channel = session.openChannel(Protocols.SFTP.name().toLowerCase());
		channel.connect(channelTimeout);
		ChannelSftp sftpChannel = (ChannelSftp) channel;
		return sftpChannel;
	}

	/**
	 * It writes the data read from file to local disk file.
	 * 
	 * @param inputStream - Stream to file to be downloaded
	 * @param file - Local File (File which would be saved on local disk) 
	 * @return Return stream to local Downloading file. 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private FileOutputStream writeToFile(InputStream inputStream, File file) throws FileNotFoundException, IOException {
		FileOutputStream outputStream = new FileOutputStream(file);
		int bufferSize = commonUtils.getBufferSize(Protocols.SFTP);
		byte[] buffer = new byte[bufferSize];
		int bytesRead = -1;
		while ((bytesRead = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, bytesRead);
		}
		return outputStream;
	}

}
