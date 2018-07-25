package com.kirtesh.downloadmanager;

import java.util.Map;

import org.apache.log4j.Logger;

import com.kirtesh.downloadmanager.factory.RequestRoutingFactory;
import com.kirtesh.downloadmanager.metadata.DownloadMetadata;
import com.kirtesh.downloadmanager.service.DownloadExecutorService;
import com.kirtesh.downloadmanager.service.startup.StartUpService;
import com.kirtesh.downloadmanager.utils.DownloadUtils;

/**
 * 
 * @author kirteshdudawat
 *
 * Prerequesties: 
 * 1. Define all properties mentioned in src/main/resources/application.properties
 * 
 * 2. Define logging file path for property "log4j.appender.file.File" in src/main/resources/log4j.properties
 * 
 * 
 * 
 * This Application would parallelly download files for provided Url's in optimized space enviournment. This class would act as entry to the application.
 * 
 * The project is an maven project in Java, with compiler compliance set to Java 1.8 or above.
 * 
 * 
 * 
 * Applications feature overview:
 * 1. Would download data from multiple sources and protocol (FTP, SFTP, HTTP, HTTPS) to local disk.
 * 2. No partial downloaded data be kept at local disk in any case.
 * 
 * 
 * 
 * Application Package Design / Definition:
 * 
 * com.kirtesh.downloadmanager  
 * Entry point to Application (We are currently here) 
 * 
 * com.kirtesh.downloadmanager.cache  
 * All JVM level Cache would reside here. 
 * 
 * com.kirtesh.downloadmanager.enums  
 * All Enums / Constants to be present here 
 * 
 * com.kirtesh.downloadmanager.factory 
 * Factory would return Implementing class, the source can just call factory for providing implementation class. By this, source has been abstracted from Creating objects and would directly get required implementation without having to specify the exact class of the object that will be created by Factory. 
 * 
 * com.kirtesh.downloadmanager.metadata 
 * Would contains Pojo's of all metadata required during call's / task scheduling across project.
 * 
 * com.kirtesh.downloadmanager.service 
 * Package would keep abstraction of all Business logic required. 
 * Note : For ease of development and readability we have separated Start-Up Logic / implementation along with Validation Service and its  implementation from all other Business logic.
 * 
 * com.kirtesh.downloadmanager.service.impl 
 * Implements all Business Logic's as specified in com.kirtesh.downloadmanager.service package.
 * 
 * com.kirtesh.downloadmanager.service.startup 
 * Package would keep abstraction of all Start-up logics required. 
 * 
 * com.kirtesh.downloadmanager.service.startup.impl 
 * Implements all Start-Up Logic's as specified in com.kirtesh.downloadmanager.service.startup package. Eg. Initializing All properties to JVM Cache at Startup
 * 
 * com.kirtesh.downloadmanager.service.validation 
 * Package would keep abstraction of all Validation logics required. 
 * 
 * com.kirtesh.downloadmanager.service.validation.impl 
 * Implements all Validation Logic's as specified in com.kirtesh.downloadmanager.service.validation package. Eg. Validating if a location exist on local disk, before actually starting the download.
 * 
 * com.kirtesh.downloadmanager.utils 
 * Would contain all Utility methods that would be used across multiple services. Eg. Checking if String object is empty or not.
 * 
 * 
 * 
 * Dependencies in project (Defined in POM):
 * 1. log4j : For Logging purpose
 * 2. nurkiewicz.asyncretry : For implementation of Retryable Executor for Downloading files.
 * 3. jcraft.jsch : For downloading SFTP protocol based file.
 * 4. JUnit4 : For testing purpose.
 * 
 */
public class DownloadManager {
	/**
	 * Logger declaration..
	 */
	public final static Logger logger = Logger.getLogger(DownloadManager.class);

	/**
	 * Main method entry point to Application.
	 * 
	 * @param args : args have hot been utilized.
	 */
	public static void main(String[] args) {

		StartUpService startUpService = RequestRoutingFactory.getStartUpServiceImpl();
		DownloadUtils downloadUtils = RequestRoutingFactory.getDownloadUtils();
		DownloadExecutorService downloadExecutorService = RequestRoutingFactory.getDownloadExecutorService();
		
		/**
		 * Load all properties from application.properties to JVM Cache (DMCache)
		 */
		initializeDMCache(startUpService);
		
		/**
		 * Validate if property file mentioned in "download.url.filepath" exists or not. It also takes care that Application have Read access to specified file or not.
		 */
		validateUrlDownloadFilePath(startUpService);
		
		/**
		 * It read all URLs from "download.url.filepath" and update JVM Cache -> DMCache.downloadUrl. All duplicate URLs in download files are removed.
		 */
		initializeDMCacheDownloadUrls(startUpService);
		
		/**
		 * Maps all URLs in JVM Cache -> DMCache.downloadUrl to its Metadata required to download file.
		 * 
		 * Internal task handled:
		 * 1. Checks if Download directory specified by property "{protocol}.download.directory.path" exists or not. If not, check if Application has permission to create download directory by User in property "create.download.filepath". If "create.download.filepath" is set to true & directory does not exists. Create a new Directory.
		 * 
		 * 2. For managing file name at Local Disk, it check if a file name specified in URI, exist at "{protocol}.download.directory.path" or not. If file name does not exist, name remains same as in URL.
		 * 		
		 * 	 2.1 : If file with same name exists, check property "override.existing.file". If set to true, override existing files else update filename to be saved on Disk by appneding ({count}) to existing.
		 * 	
		 * 			eg. For URL http://www.XYZ-ABC.com/download.html, Filename would be download.html. If local directory where downloaded file would be kept has a file name download.html & property override.existing.file is set to false, New file would be saved with name download(1).html. If override.existing.file would have been set to false, file would be save under same name download.html. 
		 * 
		 * Note: If override.existing.file = true, would result in deletion of existing Data & Its an ideal scenerio to keep, if you wish to keep only the latest copy of downloaded file. Previous Copy aren't useful.
		 * 
		 */
		Map<String, DownloadMetadata> urlToMetadataMapping = downloadUtils.downloadPreprocessing();
		
		/**
		 * It would set task to Retry Async Scheduler. Where actual download would take place. For better optimization you can specify properties scheduled.threadpool.core.size, retry.max.retries, retry.max.delay.millis, retry.exponential.backoff.multiplier, retry.exponential.backoff.millis & {protocol}.buffer.size.
		 * 
		 * Note: {protocol}.buffer.size * scheduled.threadpool.core.size should never be more than memory available. For eg. If memory buffer available to system is 70 MB, & threadpool size is set to 7, than max. buffer size to any protocol should never be >10 MB. As if 7 concurrent download takes place of same protocol, Total buffer used would be 10 (Buffer for one protocol download) * 7(total threads executing) = 70 MB which is equivalent to max available buffer.
		 */
		downloadExecutorService.downloadUrls(urlToMetadataMapping);
	}

	/**
	 * @PreCheckValidation: All validation to file path, Like file exists on specified location "download.url.filepath" in property are correct and verified in validateUrlDownloadFilePath(startUpService) of DownloadManager.
	 * 
	 * 
	 * It read all URLs from "download.url.filepath" file and update JVM Cache : DMCache.downloadUrl Set. All duplicate URLs in download files are automatically removed as Set is used in JVM.
	 * 
	 * @param startUpService : StartUpServiceImpl Object.
	 * 
	 * @ExpectedOutput :
	 * 		void : If no  exception occurs.
	 * 		Exit with status Code 30, if Exception occurs while reading file or URLs from file.
	 */
	private static void initializeDMCacheDownloadUrls(StartUpService startUpService) {
		boolean initializeCache = startUpService.initializeDownloadUrlsList();

		if (!initializeCache) {
			exitExecution("Unable to Load Download Urls!! Exiting.. ", 30);
		}
	}

	/**
	 * @PreCheckValidation: It expects JVM Cache would have been populated i.e. Startup would have successfully executed initializeDMCache(startUpService).
	 * 
	 * 	 Validate if property file mentioned in "download.url.filepath" exists or not as well as Application having Read access to specified file or not.
	 * 
	 * @param startUpService : StartUpServiceImpl Bean.
	 * 
	 * @ExpectedOutput :
	 * 		void : If no  exception occurs.
	 * 		Exit with status Code 30, if file does not exist or application dosent have read access to file mentioned.
	 */
	private static void validateUrlDownloadFilePath(StartUpService startUpService) {

		boolean validationStatus = startUpService.validateUrlDownloadFilePath();

		if (!validationStatus) {
			exitExecution("File Path with Download Urls Does not Exists!! Exiting.. ", 30);
		}
	}
	/**
	 * Reads all properties mentioned in application.properties to JVM Cache (DMCache.propertyCacheMap) with type Map<String, String>.
	 * 
	 * Note: Ignores all properties with null, empty values. Nor updates JVM Cache for same.
	 * 
	 * @param startUpService : StartUpServiceImpl Bean.
	 * 
	 * 
	 * @ExpectedOutput :
	 * 		void : If no  exception occurs.
	 * 		Exit with status Code 30, if Exception occurs while reading properties from "application.properties".
	 */
	private static void initializeDMCache(StartUpService startUpService) {

		boolean initializeCache = startUpService.initializeSystemProperties();

		if (!initializeCache) {
			exitExecution("Unable to Load System Properties!! Exiting.. ", 30);
		}
	}
	
	/**
	 * 
	 * It logs the `logObject` in log file mentioned in log4j.properties (log4j.appender.file.File) and terminates / completes execution with statusCode `exitStatus`.
	 * 
	 * @param logObject : Message to be logged.
	 * @param exitStatus : JVM Exit status. Its specifies condition of termination of JVM. Like : 0 = Completed Successfully, 30 : User forced exit on Error/Exception
	 */
	private static void exitExecution(String logObject, int exitStatus) {
		logger.error(logObject);
		System.exit(exitStatus);
	}

}
