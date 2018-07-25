package com.kirtesh.downloadmanager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.kirtesh.downloadmanager.cache.DMCache;
import com.kirtesh.downloadmanager.enums.Protocols;
import com.kirtesh.downloadmanager.metadata.DownloadMetadata;
import com.kirtesh.downloadmanager.service.validation.impl.ValidationServiceImpl;
import com.kirtesh.downloadmanager.utils.DownloadUtils;
/**
 * 
 * @author kirteshdudawat
 *
 * This class tests com.agoda.downloadmanager.utils.DownloadUtils
 */
public class TestDownloadUtils {

	public static final String TEST_SAVE_PATH = "/Users/kirteshdudawat/Desktop/test/";

	/**
	 * Validates if create.download.filepath is set to true and Directory dosen't exist, then creates a directory.
	 * @throws IOException - Exception while handling file
	 */
	@Test
	public void testValidateAndReturnPathForNonExistingDirectory() throws IOException{

		ValidationServiceImpl validationServiceImpl = new ValidationServiceImpl();
		if(validationServiceImpl.validateUrl(TEST_SAVE_PATH)) {
			File file = new File(TEST_SAVE_PATH);
			delete(file);
		}
		DMCache.updateCacheMap("create.download.filepath", "true");
		DownloadUtils downloadUtils = new DownloadUtils();
		
		String path = downloadUtils.validateAndReturnPath(TEST_SAVE_PATH);
		Assert.assertNotNull(path);
		if(!validationServiceImpl.validateUrl(path)) {
			boolean flag = TEST_SAVE_PATH.equals(path);
			Assert.assertTrue(flag);
		}
	}
	
	/**
	 * Validates if create.download.filepath is set to false and Directory dosen't exist, then application dosen't creates a directory.
	 * @throws IOException - Exception while handling file
	 */
	@Test
	public void testValidateAndReturnPathForNonExistingDirectoryWithCreateFlagFalse() throws IOException{

		ValidationServiceImpl validationServiceImpl = new ValidationServiceImpl();
		if(validationServiceImpl.validateUrl(TEST_SAVE_PATH)) {
			File file = new File(TEST_SAVE_PATH);
			delete(file);
		}
		DMCache.updateCacheMap("create.download.filepath", "false");
		DownloadUtils downloadUtils = new DownloadUtils();
		
		String path = downloadUtils.validateAndReturnPath(TEST_SAVE_PATH);
		Assert.assertNull(path);
	}
	
	/**
	 * Checks if Preprocessing works fine with existing directory and overriding existing flag set to true.
	 * @throws IOException - Exception while handling file
	 */
	@Test
	public void testDownloadingPreprocessing() throws IOException{

		DownloadUtils downloadUtils = new DownloadUtils();
		ValidationServiceImpl validationServiceImpl = new ValidationServiceImpl();
		if(validationServiceImpl.validateUrl(TEST_SAVE_PATH)) {
			File file = new File(TEST_SAVE_PATH);
			delete(file);
		}
		Path paths = Paths.get(TEST_SAVE_PATH);
		Files.createDirectories(paths);
		DMCache.updateCacheMap("create.download.filepath", "true");
		DMCache.updateCacheMap("override.existing.file", "true");
		DMCache.updateCacheMap("https.download.directory.path", TEST_SAVE_PATH);
		String url = "https://raw.githubusercontent.com/apache/httpd/trunk/modules/cache/NWGNUcach_dsk";
		DMCache.downloadUrl.add(url);
		
		Map<String, DownloadMetadata> map = downloadUtils.downloadPreprocessing();
		boolean flag = map.get(url).getProtocols().equals(Protocols.HTTPS);
		Assert.assertTrue(flag);
		flag = map.get(url).getFilePath().equals(TEST_SAVE_PATH+url.substring(url.lastIndexOf("/")+1));
		Assert.assertTrue(flag);
		flag = map.get(url).getDownloadUrl().equals(url);
		Assert.assertTrue(flag);
	}
	
	/**
	 * Checks if Preprocessing works fine with existing directory and overriding existing flag set to false.
	 * @throws IOException - Exception while handling file
	 */
	@Test
	public void testDownloadingPreprocessingWithExistingFiles() throws IOException{

		DownloadUtils downloadUtils = new DownloadUtils();
		String url = "https://raw.githubusercontent.com/apache/httpd/trunk/modules/cache/NWGNUcach_dsk";
		ValidationServiceImpl validationServiceImpl = new ValidationServiceImpl();
		if(validationServiceImpl.validateUrl(TEST_SAVE_PATH)) {
			File file = new File(TEST_SAVE_PATH);
			delete(file);
		}
		Path paths = Paths.get(TEST_SAVE_PATH);
		Files.createDirectories(paths);
		new File(TEST_SAVE_PATH+url.substring(url.lastIndexOf("/")+1)).createNewFile();
		DMCache.updateCacheMap("create.download.filepath", "true");
		DMCache.updateCacheMap("override.existing.file", "false");
		DMCache.updateCacheMap("https.download.directory.path", TEST_SAVE_PATH);
		DMCache.downloadUrl.add(url);
		
		Map<String, DownloadMetadata> map = downloadUtils.downloadPreprocessing();
		boolean flag = map.get(url).getProtocols().equals(Protocols.HTTPS);
		Assert.assertTrue(flag);
		flag = map.get(url).getFilePath().equals(TEST_SAVE_PATH+url.substring(url.lastIndexOf("/")+1)+"(1)");
		Assert.assertTrue(flag);
		flag = map.get(url).getDownloadUrl().equals(url);
		Assert.assertTrue(flag);
	}


	/**
	 * This method would delete the directory and all its content..
	 * @param file - Input  file/ directory
	 * @throws IOException - Exception while handling file
	 */
	public static void delete(File file) throws IOException {
		if (file.isDirectory()) {
			if (file.list().length == 0) {
				file.delete();
			} else {
				String files[] = file.list();
				for (String temp : files) {
					File fileDelete = new File(file, temp);
					delete(fileDelete);
				}
				if (file.list().length == 0) {
					file.delete();
				}
			}
		}else {
    		file.delete();
		}
	}

}
