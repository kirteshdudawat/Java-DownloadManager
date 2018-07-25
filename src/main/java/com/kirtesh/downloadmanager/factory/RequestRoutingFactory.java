package com.kirtesh.downloadmanager.factory;

import java.util.Properties;

import com.kirtesh.downloadmanager.enums.Protocols;
import com.kirtesh.downloadmanager.service.DownloadExecutorService;
import com.kirtesh.downloadmanager.service.Downloader;
import com.kirtesh.downloadmanager.service.impl.CommonDownloader;
import com.kirtesh.downloadmanager.service.impl.DownloadExecutorServiceImpl;
import com.kirtesh.downloadmanager.service.impl.SFTPDownloader;
import com.kirtesh.downloadmanager.service.startup.StartUpService;
import com.kirtesh.downloadmanager.service.startup.impl.StartUpServiceImpl;
import com.kirtesh.downloadmanager.service.validation.ValidationService;
import com.kirtesh.downloadmanager.service.validation.impl.ValidationServiceImpl;
import com.kirtesh.downloadmanager.utils.CommonUtils;
import com.kirtesh.downloadmanager.utils.DownloadUtils;
/**
 * 
 * @author kirteshdudawat
 *
 * Logic Implementation has been abstracted out from Application. 
 * Hence, RequestRoutingFactory provides proper implementation based on scenerios and returns the valid bean /implementation required by requisite.
 * 
 * Note: Try to keep Declaration and Implementation abstract. 
 * 
 * Request Routing factory contains multiple methods which return a kind of Implementation which would fulfill the requirement of caller. No Documentation would be present for methods directly returning Implementation without holding any logic.
 */
public class RequestRoutingFactory {

	/**
	 * 
	 * Returns propert implementation of Download based on protocol on which file needs to be downloaded.
	 * 
	 * @param protocols - Enum Protocol, specifying protocol on which Download would take place.
	 * @return Return CommonDownloder implementation if protocol for Download is FTP, HTTP, or HTTPS else return SFTPDownloader for SFTP protocol.
	 */
	public static Downloader getDownloaderImpl(Protocols protocols) {

		switch (protocols) {
			case FTP:
			case HTTP:
			case HTTPS:
				return new CommonDownloader();
			case SFTP:
				return new SFTPDownloader();
		}
		return null;
	}

	public static ValidationService getValidationServiceImpl() {
		return new ValidationServiceImpl();
	}

	public static StartUpService getStartUpServiceImpl() {
		return new StartUpServiceImpl();
	}

	public static Properties getPropertiesFile() {
		return new Properties();
	}

	public static DownloadUtils getDownloadUtils() {
		return new DownloadUtils();
	}

	public static CommonUtils getCommonUtils() {
		return new CommonUtils();
	}

	public static DownloadExecutorService getDownloadExecutorService() {
		return new DownloadExecutorServiceImpl();
	}
}
