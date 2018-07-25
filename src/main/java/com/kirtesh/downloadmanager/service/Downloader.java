package com.kirtesh.downloadmanager.service;

import java.io.IOException;

import com.kirtesh.downloadmanager.metadata.DownloadMetadata;
/**
 * 
 * @author kirteshdudawat
 *
 * 
 * Downloader Interface.
 * 
 * All protocols for which Application would Download files need to specify its own implementation of Downloader Interface.
 *
 */
public interface Downloader {
	
	
	/**
	 * 
	 * This method would contains implementation of how to download files of protocol which the implementation class fulfills. 
	 * 
	 * @param downloadMetadata - Contains metadata of file which needs to be downloaded.
	 * @return - boolean true / false based on on Download was Successful or not.
	 * @throws IOException - In case of IO Exception.
	 */
	public boolean download(DownloadMetadata downloadMetadata) throws IOException;
}
