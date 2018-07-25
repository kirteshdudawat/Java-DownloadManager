package com.kirtesh.downloadmanager;

import org.junit.Assert;
import org.junit.Test;

import com.kirtesh.downloadmanager.cache.DMCache;
import com.kirtesh.downloadmanager.service.startup.impl.StartUpServiceImpl;

/**
 * 
 * @author kirteshdudawat
 * 
 * This class tests com.kirtesh.downloadmanager.service.startup.StartupServiceImpl
 * 
 */
public class TestStartUpServiceImpl {

	/**
	 * this methods initilizes JVM cache from application.properties and verifies same if Cache has been populated correctly or not.
	 */
	@Test
	public void initializeSystemPropertiesTest() {	

		StartUpServiceImpl startUpServiceImpl = new StartUpServiceImpl();
		boolean success = startUpServiceImpl.initializeSystemProperties();
		if(success) {
			Assert.assertTrue(DMCache.getFtpBufferSize() == 4096);
		}
	}

}
