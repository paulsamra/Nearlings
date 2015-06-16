package de.metagear.spring.web;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * A <code>RestTemplate</code> that handles the case when an HTTP response of
 * <code>MediaType</code> <i>application/json</i> is GZIP-encoded.
 * 
 * @see RestTemplate
 */
// reviewed
public class GzipJsonRestTemplate extends RestTemplate {
	/**
	 * The list of <code>HttpMessageConverter</code>s that the
	 * <code>RestTemplate</code> had registered itself, plus our own
	 * <code>GzipEnabledMappingJacksonHttpMessageConverter</code>.<br>
	 * In Spring's <code>HttpMessageConverterExtractor.extractData(..)</code>
	 * you can see that the first matching converter will be used (the other
	 * ones will be discarded), thus our own converter shall be placed at the
	 * top of the list.
	 */
	protected List<HttpMessageConverter<?>> allMessageConverters;

	// if this variable was "protected" in our Spring superclass, we wouldn't
	// need to re-implement it ...
	protected static final boolean jacksonPresent = ClassUtils.isPresent(
			"org.codehaus.jackson.map.ObjectMapper",
			RestTemplate.class.getClassLoader())
			&& ClassUtils.isPresent("org.codehaus.jackson.JsonGenerator",
					RestTemplate.class.getClassLoader());

	public GzipJsonRestTemplate() {
		super();

		if (!jacksonPresent) {
			throw new IllegalStateException("Jackson not present.");
		}

		initializeMessageConverters();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<HttpMessageConverter<?>> getMessageConverters() {
		return allMessageConverters;
	}

	/**
	 * @see #allMessageConverters
	 */
	@SuppressWarnings("serial")
	protected void initializeMessageConverters() {
		allMessageConverters = new ArrayList<HttpMessageConverter<?>>() {
			{
				add(new GzipEnabledMappingJacksonHttpMessageConverter());
				addAll(GzipJsonRestTemplate.super.getMessageConverters());
			}
		};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected <T> T doExecute(URI url, HttpMethod method,
			RequestCallback requestCallback,
			ResponseExtractor<T> responseExtractor) throws RestClientException {
		Assert.notNull(url, "'url' must not be null");
		Assert.notNull(method, "'method' must not be null");

		ClientHttpResponse response = null;
		try {
			ClientHttpRequest request = createRequest(url, method);

			if (request.getHeaders() != null) {
				// This is all we change in this method ...
				// The Spring framework has never been very extensible ...
				request.getHeaders().add("Accept-Encoding", "gzip,deflate");
			}

			if (requestCallback != null) {
				requestCallback.doWithRequest(request);
			}

			response = request.execute();

			if (!getErrorHandler().hasError(response)) {
				logResponseStatus(method, url, response);
			} else {
				handleResponseError(method, url, response);
			}

			if (responseExtractor != null) {
				return responseExtractor.extractData(response);
			} else {
				return null;
			}
		} catch (IOException ex) {
			throw new ResourceAccessException("I/O error: " + ex.getMessage(),
					ex);
		} finally {
			if (response != null) {
				response.close();
			}
		}
	}

	// if this method was "protected" in our Spring superclass, we wouldn't need
	// to re-implement it ...
	protected void handleResponseError(HttpMethod method, URI url,
			ClientHttpResponse response) throws IOException {
		if (logger.isWarnEnabled()) {
			try {
				logger.warn(method.name() + " request for \"" + url
						+ "\" resulted in " + response.getStatusCode() + " ("
						+ response.getStatusText()
						+ "); invoking error handler");
			} catch (IOException e) {
				// ignore logging exceptions when logging at the warning level
			}
		}
		getErrorHandler().handleError(response);
	}

	// if this method was "protected" in our Spring superclass, we wouldn't need
	// to re-implement it ...
	protected void logResponseStatus(HttpMethod method, URI url,
			ClientHttpResponse response) {
		if (logger.isDebugEnabled()) {
			try {
				logger.debug(method.name() + " request for \"" + url
						+ "\" resulted in " + response.getStatusCode() + " ("
						+ response.getStatusText() + ")");
			} catch (IOException e) {
				// ignore logging exceptions when logging at the warning level
			}
		}
	}
}
