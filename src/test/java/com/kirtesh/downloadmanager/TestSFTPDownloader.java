package com.kirtesh.downloadmanager;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.kirtesh.downloadmanager.cache.DMCache;
import com.kirtesh.downloadmanager.service.impl.SFTPDownloader;
import com.kirtesh.downloadmanager.service.validation.impl.ValidationServiceImpl;
/**
 * 
 * @author kirteshdudawat
 *
 *
 * This class tests Downloader Implementation for SFTP.
 * Test cases for class com.agoda.downloadmanager.service.imple.SFTPDownloader
 */
public class TestSFTPDownloader {

	public static final String TEST_SAVE_PATH = "/Users/kirteshdudawat/Desktop/test/";

	/**
	 * Test for checking Invalid Exception
	 * 
	 * @throws IOException
	 */
	@Test
	public void sftpInvalidUrlTestFailure() throws IOException {
		System.out.println("TestSFTPDownloader "+ "sftpInvalidUrlTestFailure");
		SFTPDownloader _sftpDownloader = new SFTPDownloader();
		String downloadUrl = "maliformedUrl";
		boolean success = _sftpDownloader.initiateDownload(downloadUrl, TEST_SAVE_PATH, 5000, 5000, 22);
		Assert.assertFalse(success);
	}

	/**
	 * Test for checking if SFTP Download is Working as Expected.
	 * 
	 * @throws IOException
	 */
	@Test
	public void downloadSuccessfulTest() throws IOException {
		System.out.println("TestSFTPDownloader "+ "downloadSuccessfulTest");

		DMCache.updateCacheMap("sftp.buffer.size", "4096");
		SFTPDownloader sftpDownloader = new SFTPDownloader();
		String downloadUrl = "sftp://demo-user:demo-user@demo.wftpserver.com:2222/download/manual_en.pdf";
		String location = TEST_SAVE_PATH + "manual_en.pdf";
		boolean downloadSuccessful = sftpDownloader.initiateDownload(downloadUrl, location, 30000, 30000, 22);
		Assert.assertTrue(downloadSuccessful);
	}

	/**
	 * Setting channelTimeout and sessionTimeout to 10 ms. Hence, download will fail
	 * even before read takes place. If readTimeout is increased, This test would
	 * succeed as Download would go through.
	 */
	@Test
	public void incompleteDownloadTest() {
		System.out.println("TestSFTPDownloader "+ "incompleteDownloadTest");

		DMCache.updateCacheMap("sftp.buffer.size", "4096");
		SFTPDownloader sftpDownloader = new SFTPDownloader();
		String downloadUrl = "sftp://demo-user:demo-user@demo.wftpserver.com:2222/download/manual_en.pdf";
		String location = TEST_SAVE_PATH + "incompleteFile.pdf";

		try {
			sftpDownloader.initiateDownload(downloadUrl, location, 10, 10, 22);
		} catch (IOException e) {
			ValidationServiceImpl validationServiceImpl = new ValidationServiceImpl();
			Assert.assertFalse(validationServiceImpl.validateUrl(location));
		}
	}

}
