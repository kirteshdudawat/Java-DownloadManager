package com.kirtesh.downloadmanager;

import org.junit.Assert;
import org.junit.Test;

import com.kirtesh.downloadmanager.enums.Protocols;
import com.kirtesh.downloadmanager.factory.RequestRoutingFactory;
import com.kirtesh.downloadmanager.service.Downloader;
import com.kirtesh.downloadmanager.service.impl.CommonDownloader;
import com.kirtesh.downloadmanager.service.impl.SFTPDownloader;

/**
 * 
 * @author kirteshdudawat
 *
 *	This class tests com.kirtesh.downloadmanager.factory.RequestRoutingFactory.
 */
public class TestRequestRoutingFactory {
	
	/**
	 * This methods tests for Factory Returing correct implementation against provided Protocols.
	 */
	@Test
	public void getDownloadHandlerTest(){
		Downloader downloader = RequestRoutingFactory.getDownloaderImpl(Protocols.FTP);
		Assert.assertTrue(downloader instanceof CommonDownloader);
		downloader = RequestRoutingFactory.getDownloaderImpl(Protocols.SFTP);
		Assert.assertTrue(downloader instanceof SFTPDownloader);
		downloader = RequestRoutingFactory.getDownloaderImpl(Protocols.HTTP);
		Assert.assertTrue(downloader instanceof CommonDownloader);
		downloader = RequestRoutingFactory.getDownloaderImpl(Protocols.HTTPS);
		Assert.assertTrue(downloader instanceof CommonDownloader);
	}

}
