package com.kirtesh.downloadmanager.service.validation;
/**
 * 
 * @author kirteshdudawat
 * 
 * It defines all Validations required across Application. 
 * Abstracted out ValidationService and Startup from services for better readability.
 *
 */
public interface ValidationService {

	/**
	 * Validate if property file mentioned in "url" exists or not. It also takes care that Application have Read access to specified file or not.
	 * 
	 * @return Boolean value true / false, depending if file with Download Directory path is valid or not.
	 */
	public boolean validateUrl(String url);

}
