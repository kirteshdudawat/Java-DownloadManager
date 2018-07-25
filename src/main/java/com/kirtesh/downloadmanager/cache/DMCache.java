package com.kirtesh.downloadmanager.cache;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.kirtesh.downloadmanager.enums.PropertyConstants;
/**
 * 
 * @author kirteshdudawat
 *
 * 
 * This class acts as JVM level Cache. Addition of class would avoid overhead of reading data repetitively from file. Hence, would save IO operation.
 * 
 * This class is meant to store only static data related to Application which could be frequently used. Class would be initialized at Startup via StartUpService.
 * 
 * DMCache has two internal Caches:
 * 1. propertyCacheMap : Its a Map(String, String) whose key is property defined in application.properties and Value is value specified against property in application.properties.
 * 2. downloadUrl : Its a Set(String) containing all urls mentioned in file download.url.filepath properties.
 * 
 *
 */
public class DMCache {
	
	public static Map<String, String> propertyCacheMap = new ConcurrentHashMap<String, String>();
	
	public static Set<String> downloadUrl = new HashSet<String>();
	
	/**
	 * Adds an element to DMCache.downloadUrl set.
	 * 
	 * @param element : URL in String to be added to downloadUrl
	 */
	public static void addElement(String element) {
		downloadUrl.add(element);
	}
	
	/**
	 * @return Returns filepath to list of URLs which are to be downloaded in form of String i.e. return value of property `download.url.filepath` in `application.properties`
	 */
	public static String getUrlToDownloadDirectoryPath() {
		return DMCacheUtils.getPropertyAsString(propertyCacheMap, PropertyConstants.URL_TO_DOWNLOAD_FILEPATH.getPropertyName());
	}
	
	/**
	 * @return Returns directory path to saved downloaded files of HTTP protocol in form of String i.e. return value of property `http.download.directory.path` in `application.properties`
	 */
	public static String getHttpDownloadDirectoryPath() {
		return DMCacheUtils.getPropertyAsString(propertyCacheMap, PropertyConstants.HTTP_DOWNLOAD_DIRECTORY_PATH.getPropertyName());
	}

	/**
	 * @return Returns directory path to saved downloaded files of HTTPS protocol in form of String i.e. return value of property `https.download.directory.path` in `application.properties`
	 */
	public static String getHttpsDownloadDirectoryPath() {
		return DMCacheUtils.getPropertyAsString(propertyCacheMap, PropertyConstants.HTTPS_DOWNLOAD_DIRECTORY_PATH.getPropertyName());
	}
	
	/**
	 * @return Returns directory path to saved downloaded files of SFTP protocol in form of String i.e. return value of property `sftp.download.directory.path` in `application.properties`
	 */
	public static String getSftpDownloadDirectoryPath() {
		return DMCacheUtils.getPropertyAsString(propertyCacheMap, PropertyConstants.SFTP_DOWNLOAD_DIRECTORY_PATH.getPropertyName());
	}
	
	/**
	 * @return Returns directory path to saved downloaded files of FTP protocol in form of String i.e. return value of property `ftp.download.directory.path` in `application.properties`
	 */
	public static String getFtpDownloadDirectoryPath() {
		return DMCacheUtils.getPropertyAsString(propertyCacheMap, PropertyConstants.FTP_DOWNLOAD_DIRECTORY_PATH.getPropertyName());
	}
	
	/**
	 * @return returns boolean value true or false. If directory path on Local Disk for saving downloaded file does not exist as mention in {protocol}.download.directory.path then it would create if if this method return true else would ignore the URL, i.e. return value of property `create.download.filepath` in `application.properties`
	 */
	public static boolean createDownloadFilepathIfNotExist() {
		return DMCacheUtils.getPropertyAsBoolean(propertyCacheMap, PropertyConstants.CREATE_DOWNLOAD_FILEPATH_IF_NOT_EXIST.getPropertyName());
	}
	
	/**
	 * @return returns boolean value true or false. Used to decide wheather to override exisitng file or nor if it already exisit in download folder, i.e. return value of property `override.existing.file` in `application.properties`
	 */
	public static boolean overrideExisitngFile() {
		return DMCacheUtils.getPropertyAsBoolean(propertyCacheMap, PropertyConstants.OVERRIDE_EXISITING_FILE.getPropertyName());
	}
	
	/**
	 * @return returns Integer value for setting connection timeout in HTTP file download, i.e. return value of property `http.connection.timeout.millis` in `application.properties`
	 */
	public static int getHttpConnectionTimeout() {
		return DMCacheUtils.getPropertyAsInteger(propertyCacheMap, PropertyConstants.HTTP_CONNECTION_TIMEOUT_IN_MILLIS.getPropertyName());
	}

	/**
	 * @return returns Integer value for setting connection timeout in HTTPS file download, i.e. return value of property `https.connection.timeout.millis` in `application.properties`
	 */
	public static int getHttpsConnectionTimeout() {
		return DMCacheUtils.getPropertyAsInteger(propertyCacheMap, PropertyConstants.HTTPS_CONNECTION_TIMEOUT_IN_MILLIS.getPropertyName());
	}

	/**
	 * @return returns Integer value for setting connection timeout in FTP file download, i.e. return value of property `ftp.connection.timeout.millis` in `application.properties`
	 */
	public static int getFtpConnectionTimeout() {
		return DMCacheUtils.getPropertyAsInteger(propertyCacheMap, PropertyConstants.FTP_CONNECTION_TIMEOUT_IN_MILLIS.getPropertyName());
	}

	/**
	 * @return returns Integer value for setting Read timeout in HTTP file download, i.e. return value of property `http.read.timeout.millis` in `application.properties`
	 */
	public static int getHttpReadTimeout() {
		return DMCacheUtils.getPropertyAsInteger(propertyCacheMap, PropertyConstants.HTTP_READ_TIMEOUT_IN_MILLIS.getPropertyName());
	}
	
	/**
	 * @return returns Integer value for setting Read timeout in HTTPS file download, i.e. return value of property `https.read.timeout.millis` in `application.properties`
	 */
	public static int getHttpsReadTimeout() {
		return DMCacheUtils.getPropertyAsInteger(propertyCacheMap, PropertyConstants.HTTPS_READ_TIMEOUT_IN_MILLIS.getPropertyName());
	}
	
	/**
	 * @return returns Integer value for setting Read timeout in FTP file download, i.e. return value of property `ftp.read.timeout.millis` in `application.properties`
	 */
	public static int getFtpReadTimeout() {
		return DMCacheUtils.getPropertyAsInteger(propertyCacheMap, PropertyConstants.FTP_READ_TIMEOUT_IN_MILLIS.getPropertyName());
	}
	
	/**
	 * @return returns Integer value for setting Buffer size in HTTP file download, i.e. return value of property `http.buffer.size` in `application.properties`
	 */
	public static int getHttpBufferSize() {
		return DMCacheUtils.getPropertyAsInteger(propertyCacheMap, PropertyConstants.HTTP_BUFFER_SIZE.getPropertyName());
	}

	/**
	 * @return returns Integer value for setting Buffer size in HTTPS file download, i.e. return value of property `https.buffer.size` in `application.properties`
	 */
	public static int getHttpsBufferSize() {
		return DMCacheUtils.getPropertyAsInteger(propertyCacheMap, PropertyConstants.HTTPS_BUFFER_SIZE.getPropertyName());
	}
	
	/**
	 * @return returns Integer value for setting Buffer size in FTP file download, i.e. return value of property `ftp.buffer.size` in `application.properties`
	 */
	public static int getFtpBufferSize() {
		return DMCacheUtils.getPropertyAsInteger(propertyCacheMap, PropertyConstants.FTP_BUFFER_SIZE.getPropertyName());
	}
	
	/**
	 * @return returns Integer value for setting retry back off time while file download, i.e. return value of property `retry.exponential.backoff.millis` in `application.properties`
	 */
	public static int getRetryExponentialBackoffInMillis() {
		return DMCacheUtils.getPropertyAsInteger(propertyCacheMap, PropertyConstants.RETRY_EXPONENTIAL_BACKOFF_MILLIS.getPropertyName());
	}
	
	/**
	 * @return returns Integer value for setting retry back off multiplier time while file download, i.e. return value of property `retry.exponential.backoff.multiplier` in `application.properties`
	 */
	public static int getRetryExponentialBackoffMultiplier() {
		return DMCacheUtils.getPropertyAsInteger(propertyCacheMap, PropertyConstants.RETRY_EXPONENTIAL_BACKOFF_MULTIPLIER.getPropertyName());
	}

	/**
	 * @return returns Integer value for setting delay time during retry while file download, i.e. return value of property `retry.max.delay.millis` in `application.properties`
	 */
	public static int getRetryMaxDelayMillis() {
		return DMCacheUtils.getPropertyAsInteger(propertyCacheMap, PropertyConstants.RETRY_MAX_DELAY_MILLIS.getPropertyName());
	}
	
	/**
	 * @return returns Integer value for maximum allowed retry counts while file downloading, i.e. return value of property `retry.max.retries` in `application.properties`
	 */
	public static int getRetryMaxRetries() {
		return DMCacheUtils.getPropertyAsInteger(propertyCacheMap, PropertyConstants.RETRY_MAX_RETRIES.getPropertyName());
	}
	
	/**
	 * @return returns Integer value for core thread pool size for Executor while file downloading, i.e. return value of property `scheduled.threadpool.core.size` in `application.properties`
	 */
	public static int getScheduledThreadpoolCoreSize() {
		return DMCacheUtils.getPropertyAsInteger(propertyCacheMap, PropertyConstants.SCHEDULED_THREADPOOL_CORE_SIZE.getPropertyName());
	}

	/**
	 * @return returns Integer value for Channel timeout SFTP file downloading, i.e. return value of property `sftp.channel.timeout.millis` in `application.properties`
	 */
	public static int getSftpChannelTimeoutInMillis() {
		return DMCacheUtils.getPropertyAsInteger(propertyCacheMap, PropertyConstants.SFTP_CHANNEL_TIMEOUT_MILLIS.getPropertyName());
	}
	
	/**
	 * @return returns Integer value for Session timeout SFTP file downloading, i.e. return value of property `sftp.session.timeout.millis` in `application.properties`
	 */
	public static int getSftpSessionTimeoutInMillis() {
		return DMCacheUtils.getPropertyAsInteger(propertyCacheMap, PropertyConstants.SFTP_SESSION_TIMEOUT_MILLIS.getPropertyName());
	}

	/**
	 * @return returns Integer value for setting Buffer size in SFTP file download, i.e. return value of property `sftp.buffer.size` in `application.properties`
	 */
	public static int getSftpBufferSize() {
		return DMCacheUtils.getPropertyAsInteger(propertyCacheMap, PropertyConstants.SFTP_BUFFER_SIZE.getPropertyName());
	}
	
	/**
	 * @return returns Integer value for port to be used in SFTP file download, i.e. return value of property `sftp.default.port` in `application.properties`
	 */
	public static int getSftpDefultPort() {
		return DMCacheUtils.getPropertyAsInteger(propertyCacheMap, PropertyConstants.SFTP_DEFAULT_PORT.getPropertyName());
	}

	/**
	 * Adds an element to DMCache.propertyCacheMap map.
	 * 
	 * @param key : property name from application.properties
	 * @param value : value against corresponding property from application.properties.
	 */
	public static void updateCacheMap(String key, String value) {
		propertyCacheMap.put(key, value);
	}
	
}
