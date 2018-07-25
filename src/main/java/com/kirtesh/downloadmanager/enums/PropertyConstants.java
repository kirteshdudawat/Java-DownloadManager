package com.kirtesh.downloadmanager.enums;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/**
 * 
 * @author kirteshdudawat
 *
 * 
 * Contains All properties as Enum Constant provided in application.properties.
 * Usecase was to avoid typo-error while using properties. Hence, thoughout the application Property Constants are used instead of using direct values.
 *
 *
 *	PropertyConstants.propertyNameSet - Contains all property name in actual format.
 *
 *	Note: For more details on what properties mean and usecase, follow DMCache Documentations.
 *	
 */
public enum PropertyConstants {
	
	HTTP_DOWNLOAD_DIRECTORY_PATH("http.download.directory.path"),
	SFTP_DOWNLOAD_DIRECTORY_PATH("sftp.download.directory.path"),
	FTP_DOWNLOAD_DIRECTORY_PATH("ftp.download.directory.path"),
	HTTPS_DOWNLOAD_DIRECTORY_PATH("https.download.directory.path"),
	
	CREATE_DOWNLOAD_FILEPATH_IF_NOT_EXIST("create.download.filepath"),
	URL_TO_DOWNLOAD_FILEPATH("download.url.filepath"),
	OVERRIDE_EXISITING_FILE("override.existing.file"),
	
	HTTP_CONNECTION_TIMEOUT_IN_MILLIS("http.connection.timeout.millis"),
	HTTPS_CONNECTION_TIMEOUT_IN_MILLIS("https.connection.timeout.millis"),
	FTP_CONNECTION_TIMEOUT_IN_MILLIS("ftp.connection.timeout.millis"),
	FTP_READ_TIMEOUT_IN_MILLIS("ftp.read.timeout.millis"),
	HTTPS_READ_TIMEOUT_IN_MILLIS("https.read.timeout.millis"),
	HTTP_READ_TIMEOUT_IN_MILLIS("http.read.timeout.millis"),
	FTP_BUFFER_SIZE("ftp.buffer.size"),
	HTTPS_BUFFER_SIZE("https.buffer.size"),
	HTTP_BUFFER_SIZE("http.buffer.size"),
	SFTP_BUFFER_SIZE("sftp.buffer.size"),
	SFTP_SESSION_TIMEOUT_MILLIS("sftp.session.timeout.millis"),
	SFTP_CHANNEL_TIMEOUT_MILLIS("sftp.channel.timeout.millis"),
	SFTP_DEFAULT_PORT("sftp.default.port"),
	
	RETRY_EXPONENTIAL_BACKOFF_MILLIS("retry.exponential.backoff.millis"),
	RETRY_EXPONENTIAL_BACKOFF_MULTIPLIER("retry.exponential.backoff.multiplier"),
	RETRY_MAX_DELAY_MILLIS("retry.max.delay.millis"),
	RETRY_MAX_RETRIES("retry.max.retries"),
	SCHEDULED_THREADPOOL_CORE_SIZE("scheduled.threadpool.core.size");
	
	private static Set<String> propertyNameSet = new HashSet<String>();
	
	private String propertyName;
	
	private PropertyConstants(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getPropertyName() {
		return propertyName;
	}
	
	static {
		for(PropertyConstants constant : PropertyConstants.values()) {
			propertyNameSet.add(constant.propertyName);
		}
	}
	
	/**
	 * 
	 * @return Would return all Properties name in form of List. Format would be same as in application.properties.
	 */
	public static List<String> getPropertiesList() {
		List<String> propertiesList = new ArrayList<String>();
		propertiesList.addAll(propertyNameSet);
		return propertiesList;
	}
}
