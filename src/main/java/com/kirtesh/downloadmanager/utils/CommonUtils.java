package com.kirtesh.downloadmanager.utils;

import com.kirtesh.downloadmanager.cache.DMCache;
import com.kirtesh.downloadmanager.enums.Protocols;
/**
 * 
 * @author kirteshdudawat
 *
 *	This class contains utility methods which would be used across multiple implementation.
 *
 */
public class CommonUtils {
	
	/**
	 * This methods check if String is null or Empty
	 * 
	 * @param inputString - String input.
	 * @return return Boolean true / false based on inputString is Empty / null or not.
	 */
	public static boolean isStringNullOrEmpty(String inputString) {
		if (null == inputString || inputString.isEmpty()) {
			return true;
		}
		return false;
	}

	/**
	 * This method returns Connection timeout based on Protocol. This method is applicable only for FTP, HTTP, HTTPS.
	 * 
	 * @param protocol - Provides download file protocol
	 * @return Connection timeout in integer
	 */
	public int getConnectionTimeout(Protocols protocol) {

		switch (protocol) {
			case FTP:
				return DMCache.getFtpConnectionTimeout();
			case HTTP:
				return DMCache.getHttpConnectionTimeout();
			case HTTPS:
				return DMCache.getHttpsConnectionTimeout();
		}
		return -1;
	}
	
	/**
	 * This method returns Read timeout based on Protocol. This method is applicable only for FTP, HTTP, HTTPS.
	 * 
	 * @param protocol - Provides download file protocol
	 * @return Read timeout in integer
	 */
	public int getReadTimeout(Protocols protocol) {

		switch (protocol) {
			case FTP:
				return DMCache.getFtpReadTimeout();
			case HTTP:
				return DMCache.getHttpReadTimeout();
			case HTTPS:
				return DMCache.getHttpsReadTimeout();
		}
		return -1;
	}

	/**
	 * This method returns bufferSize based on Protocol. This method is applicable only for FTP, HTTP, HTTPS, SFTP.
	 * 
	 * @param protocol - Provides download file protocol
	 * @return Buffer Size in integer
	 */
	public int getBufferSize(Protocols protocol) {
		
		switch (protocol) {
			case FTP:
				return DMCache.getFtpBufferSize();
			case HTTP:
				return DMCache.getHttpBufferSize();
			case HTTPS:
				return DMCache.getHttpsBufferSize();
			case SFTP:
				return DMCache.getSftpBufferSize();
		}
		return -1;
	}
}
