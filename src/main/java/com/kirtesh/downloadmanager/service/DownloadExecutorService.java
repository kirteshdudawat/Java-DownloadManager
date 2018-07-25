package com.kirtesh.downloadmanager.service;

import java.util.Map;

import com.kirtesh.downloadmanager.metadata.DownloadMetadata;
/**
 * 
 * @author kirteshdudawat
 *
 * Provides Interface to Executor Service to be used for multiple concurrent Downloading File. 
 * Their could be multiple implementation to same based on type and functionality of Executors.
 */
public interface DownloadExecutorService {
	
	/**
	 * This method would allow multiple file to be downloaded concurrently at same time. 
	 * @param urlToMetadataMapping would contain mapping of URLs that would be downloaded along with its metadata.
	 */
	public void downloadUrls(Map<String, DownloadMetadata> urlToMetadataMapping);

}
