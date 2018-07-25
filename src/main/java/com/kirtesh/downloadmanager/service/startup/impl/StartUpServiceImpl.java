package com.kirtesh.downloadmanager.service.startup.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

import com.kirtesh.downloadmanager.cache.DMCache;
import com.kirtesh.downloadmanager.enums.PropertyConstants;
import com.kirtesh.downloadmanager.factory.RequestRoutingFactory;
import com.kirtesh.downloadmanager.service.startup.StartUpService;
import com.kirtesh.downloadmanager.service.validation.ValidationService;
import com.kirtesh.downloadmanager.utils.CommonUtils;
/**
 * 
 * @author kirteshdudawat
 * 
 * This Interface defines all startup operations and task. Eg. Initializing JVM Cache etc. All startup operations are / should performed in implementation to this interface.
 *
 */
public class StartUpServiceImpl implements StartUpService {
	
	public final static Logger logger = Logger.getLogger(StartUpServiceImpl.class);
	
	/**
	 * Static property file name.
	 */
	private static final String PROPERTIES_FILE_NAME = "application.properties";

	/**
	 * Load all properties from application.properties to JVM Cache (DMCache)
	 * 
	 * @return Boolean value true / false based on if System Properties were initialized successfully
	 */
	public boolean initializeSystemProperties() {
		try {
			Properties properties = RequestRoutingFactory.getPropertiesFile();
			InputStream inputStream = initializeInputStream(PROPERTIES_FILE_NAME);
			properties.load(inputStream);
			initializeDMCache(properties);
			return true;
		} catch (Exception e) {
			logger.error("Exception Occured while reading properties filename"+ e.getMessage());
		}
		return false;
	}

	/**
	 * Load all properties from application.properties to JVM Cache (DMCache)
	 * 
	 */
	private void initializeDMCache(Properties properties) {
		List<String> propertyNames = PropertyConstants.getPropertiesList();
		for (String propertyName : propertyNames) {
			String value = properties.getProperty(propertyName);
			if (!CommonUtils.isStringNullOrEmpty(value)) {
				DMCache.updateCacheMap(propertyName, value);
			}
		}
	}
	
	/**
	 * It initializes & returns InputStream to file passed in input.
	 *  
	 * @param file - Filepath to file
	 * @return - Stream linked to file received as Parameter
	 */
	private InputStream initializeInputStream(String file) {
		return getClass().getClassLoader().getResourceAsStream(file);
	}
	
	/**
	 * Validate if property file mentioned in "download.url.filepath" exists or not. It would also check if Application have Read access to specified file or not.
	 *
	 * @return Boolean value true / false based on if file with URLs to be downloaded is valid or not.
	 */
	public boolean validateUrlDownloadFilePath() {
		ValidationService validationService = RequestRoutingFactory.getValidationServiceImpl();
		return validationService.validateUrl(DMCache.getUrlToDownloadDirectoryPath());
	}

	/**
	 * It read all URLs from "download.url.filepath" and update JVM Cache - DMCache.downloadUrl. All duplicate URLs in download files are removed.
	 * Note: File Validations have been performed earlier, in above method validateUrlDownloadFilePath
	 * 
	 * @return Boolean value true / false based on if downloadUrls are initialized successfully or not.
	 */
	public boolean initializeDownloadUrlsList() {
		Path path = Paths.get(DMCache.getUrlToDownloadDirectoryPath());		
		Stream<String> lines;
		try {
			lines = Files.lines(path);
			lines.forEach(line -> DMCache.addElement(line));
			lines.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
