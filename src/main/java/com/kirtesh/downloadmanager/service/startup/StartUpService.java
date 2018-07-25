package com.kirtesh.downloadmanager.service.startup;
/**
 * 
 * @author kirteshdudawat
 *
 *
 *	This Interface defines all startup operations and task. Eg. Initializing JVM Cache etc. All startup operations are / should performed in implementation to this interface.
 */
public interface StartUpService {
	
	/**
	 * Load all properties from application.properties to JVM Cache (DMCache)
	 * 
	 * @return Boolean value true / false based on if System Properties were initialized successfully
	 */
	public boolean initializeSystemProperties();

	/**
	 * Validate if property file mentioned in "download.url.filepath" exists or not. It should also take care that Application have Read access to specified file or not.
	 *
	 * @return Boolean value true / false based on if file with URLs to be downloaded is valid or not.
	 */
	public boolean validateUrlDownloadFilePath();

	/**
	 * It read all URLs from "download.url.filepath" and update JVM Cache - DMCache.downloadUrl. All duplicate URLs in download files are removed.
	 * 
	 * @return Boolean value true / false based on if downloadUrls are initialized successfully or not.
	 */
	public boolean initializeDownloadUrlsList();

}
