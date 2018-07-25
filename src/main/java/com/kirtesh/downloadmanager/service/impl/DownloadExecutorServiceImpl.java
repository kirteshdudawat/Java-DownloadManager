package com.kirtesh.downloadmanager.service.impl;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.log4j.Logger;

import com.kirtesh.downloadmanager.cache.DMCache;
import com.kirtesh.downloadmanager.factory.RequestRoutingFactory;
import com.kirtesh.downloadmanager.metadata.DownloadMetadata;
import com.kirtesh.downloadmanager.service.DownloadExecutorService;
import com.kirtesh.downloadmanager.service.Downloader;
import com.nurkiewicz.asyncretry.AsyncRetryExecutor;
import com.nurkiewicz.asyncretry.RetryExecutor;
/**
 * 
 * @author kirteshdudawat
 *
 *	Reference : https://github.com/nurkiewicz/async-retry
 *
 * This class uses Async Retry Executor for initiating Download concurrently. All the properties are configured via properties specified in application.properties. For more info on properties, follow  DMCache.
 * All Retry takes place in case connection fails or timeout or IOException occurs. RetryExecutor internally uses ScheduledExecutorService.
 *
 */
public class DownloadExecutorServiceImpl implements DownloadExecutorService {

	final static Logger logger = Logger.getLogger(DownloadExecutorServiceImpl.class);

	private ScheduledExecutorService executorService = Executors
			.newScheduledThreadPool(DMCache.getScheduledThreadpoolCoreSize());
	private RetryExecutor retryExecutor;
	
	/**
	 * This method initializes AsynchRetryExecutor with features like condition like when to retry, its retry attempts,
	 * retry strategy, delays in retry etc. to be used for initiating Downloads. 
	 * 
	 * All configuration linked properties are present in application.properties. 
	 */
	private void prepareRetryService() {
		retryExecutor = new AsyncRetryExecutor(executorService).retryOn(IOException.class)
				.withExponentialBackoff(DMCache.getRetryExponentialBackoffInMillis(),
						DMCache.getRetryExponentialBackoffMultiplier())
				.withMaxDelay(DMCache.getRetryMaxDelayMillis()).withUniformJitter()
				.withMaxRetries(DMCache.getRetryMaxRetries());
	}
	
	/**
	 * This method would allow multiple file to be downloaded concurrently at same time. All URLs to be downloaded would be submitted on RetryExecutor. 
	 * @param urlToMetadataMapping would contain mapping of URLs that would be downloaded along with its metadata.
	 */
	@Override
	public void downloadUrls(Map<String, DownloadMetadata> urlToMetadataMapping) {
		prepareRetryService();

		Set<String> downloadUrlSet = new HashSet<>(urlToMetadataMapping.keySet());

		downloadUrlSet.stream().forEach(url -> retryExecutor.getWithRetry(ctx -> {
			StringBuffer sb = new StringBuffer().append("Downloading ").append(url).append(". Download Tries : ")
					.append(ctx.getRetryCount());
			logger.info(sb.toString());
			return initiateDownload(urlToMetadataMapping.get(url));
		}).whenComplete((result, error) -> {
			StringBuffer sb = (result != null && result) ? new StringBuffer("Download Completed Successfully")
					: new StringBuffer().append("Download Failed with Error : ").append(error);
			logger.info(sb.toString());
		}));
	}

	/**
	 * This method fetches Downloader implementation based on protocol to be used for Downloading from RequestRoutingFactory & starting download.
	 * 
	 * @param downloadMetadata - Metadata of file that needs to be downloaded & saved to local disk.
	 * @return Boolean value true / false, based on was Download successful or not.
	 * @throws IOException
	 */
	private boolean initiateDownload(DownloadMetadata downloadMetadata) throws IOException {

		Downloader downloader = RequestRoutingFactory.getDownloaderImpl(downloadMetadata.getProtocols());
		if (downloader == null) {
			return false;
		}
		return downloader.download(downloadMetadata);
	}
}
