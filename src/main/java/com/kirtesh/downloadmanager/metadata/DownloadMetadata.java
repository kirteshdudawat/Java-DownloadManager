package com.kirtesh.downloadmanager.metadata;

import com.kirtesh.downloadmanager.enums.Protocols;
/**
 * 
 * @author kirteshdudawat
 *
 * 
 * It contains metadata required to download file. 
 * For eg. 
 * protocol - Specifies protocol on which download takes place.
 * downloadUrl - Specifies complete URL of file which needs to be downloaded.
 * filePath - Contains complete filepath of Local Disk where file needs to be saved.
 *
 */
public class DownloadMetadata {
	
	private Protocols protocols;
	
	private String downloadUrl;
	
	private String filePath;
	
	public DownloadMetadata() {
		super();
	}

	public DownloadMetadata(Protocols protocols, String downloadUrl, String filePath) {
		super();
		this.protocols = protocols;
		this.downloadUrl = downloadUrl;
		this.filePath = filePath;
	}

	public Protocols getProtocols() {
		return protocols;
	}

	public void setProtocols(Protocols protocols) {
		this.protocols = protocols;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DataDownloadMetadata [protocols=");
		builder.append(protocols);
		builder.append(", downloadUrl=");
		builder.append(downloadUrl);
		builder.append(", filePath=");
		builder.append(filePath);
		builder.append("]");
		return builder.toString();
	}
	
}
