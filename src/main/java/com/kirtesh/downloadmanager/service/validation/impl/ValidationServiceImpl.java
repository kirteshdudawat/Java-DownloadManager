package com.kirtesh.downloadmanager.service.validation.impl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.kirtesh.downloadmanager.service.validation.ValidationService;
/**
 * 
 * @author kirteshdudawat
 * 
 * This class implements ValidationService for performing Validation on Input to Applications.
 *
 */
public class ValidationServiceImpl implements ValidationService {

	/**
	 * Validate if property file mentioned in 'url' exists or not. It also takes care that Application have Read access to specified file or not.
	 * 
	 * @return Boolean value true / false, depending if file with Download Directory path is valid or not.
	 */
	public boolean validateUrl(String url) {

		Path file = Paths.get(url);
		return Files.exists(file) && Files.isReadable(file);

	}

}
