package com.kirtesh.downloadmanager;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Assert;
import org.junit.Test;

import com.kirtesh.downloadmanager.service.impl.CommonDownloader;
import com.kirtesh.downloadmanager.service.validation.impl.ValidationServiceImpl;
/**
 * 
 * @author kirteshdudawat
 *
 *
 * This class tests Downloader Implementation for HTTPS, FTP, HTTP and SFTP.
 * 
 * Test cases for class com.agoda.downloadmanager.service.impl.CommonDownloader. 
 * 
 * Its expected that TEST_SAVE_PATH exists on Disk.
 * 
 */
public class TestCommonDownloader {
	
	public static final String TEST_SAVE_PATH = "/Users/kirteshdudawat/Desktop/test/";

	/**
	 * FTP success download test.
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	@Test
	public void downloadTestFTPSuccess() throws MalformedURLException, IOException{	

		CommonDownloader commonDownloader = new CommonDownloader();
		String downloadUrl = "ftp://speedtest.tele2.net/5MB.zip";
		String location = TEST_SAVE_PATH+"5MB.pdf";
		boolean success = commonDownloader.download(downloadUrl, location, 60000, 60000, 4096);
		Assert.assertTrue(success);
	}

	/**
	 * HTTPS Success Download Test
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	@Test
	public void downloadTestHttpsSuccess() throws MalformedURLException, IOException{

		CommonDownloader commonDownloader = new CommonDownloader();
		String downloadUrl = "https://raw.githubusercontent.com/apache/httpd/trunk/modules/cache/NWGNUcach_dsk";
		String location = TEST_SAVE_PATH+"NWGNUcach_dsk";
		boolean success = commonDownloader.download(downloadUrl, location, 60000, 60000, 4096);
		Assert.assertTrue(success);
	}
	
	/**
	 * Setting readtimeout to 10 ms. Hence, download will fail even before read takes place.
	 * If readTimeout is increased, This test would succeed as Download would go through.
	 */
	@Test
	public void testIntermediateFailure(){

		CommonDownloader commonDownloader = new CommonDownloader();
		String downloadUrl = "ftp://speedtest.tele2.net/5MB.zip";
		String location = TEST_SAVE_PATH+"5MB(1).zip";
		try{
			int readTimeout = 10;
			commonDownloader.download(downloadUrl, location, 20, readTimeout, 4096);
		}catch(IOException e){
			ValidationServiceImpl validationServiceImpl = new ValidationServiceImpl();
			Assert.assertFalse(validationServiceImpl.validateUrl(location));
		}
	}
	
	/**
	 * Validates unknown and wrong URLs thowing UnknownHostException are not breaking flow.
	 * @throws IOException
	 */
	@Test
	public void testForUnknownHostURL() throws IOException{
		CommonDownloader commonDownloader = new CommonDownloader();
		String downloadUrl = "http://malformedURL/test";
		String location = TEST_SAVE_PATH+"test";
		boolean success = commonDownloader.download(downloadUrl, location, 5000, 5000, 1024);
		Assert.assertFalse(success);
	}

}
