package de.metagear.spring.web;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;

/**
 * A
 * {@link org.springframework.http.converter.json.MappingJacksonHttpMessageConverter
 * MappingJacksonHttpMessageConverter} that decodes a GZIP-encoded HTTP response
 * before forwarding it to its superclass.
 */
// reviewed
public class GzipEnabledMappingJacksonHttpMessageConverter extends
		MappingJacksonHttpMessageConverter {
	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {
		Object result;

		if (isGzipEncoded(inputMessage)) {
			InputStream inputStream = new GZIPInputStream(
					inputMessage.getBody());

//			String foo = getString(inputStream);

			result = objectMapper.readValue(inputStream, getJavaType(clazz));
		} else {
			result = super.readInternal(clazz, inputMessage);
		}

		// NOTE: We'd use "return" directly but our debugger steps into the
		// first AND second "return" in the above "if else" statements ...
		// *sigh*
		return result;
	}

	class CopyInputStream
	{
		private InputStream _is;
		private ByteArrayOutputStream _copy = new ByteArrayOutputStream();

		/**
		 * 
		 */
		public CopyInputStream(InputStream is)
		{
			_is = is;
			
			try
			{
				copy();
			}
			catch(IOException ex)
			{
				// do nothing
			}
		}

		private int copy() throws IOException
		{
			int read = 0;
			int chunk = 0;
			byte[] data = new byte[256];
			
			while(-1 != (chunk = _is.read(data)))
			{
				read += data.length;
				_copy.write(data, 0, chunk);
			}
			
			return read;
		}
		
		public InputStream getCopy()
		{
			return (InputStream)new ByteArrayInputStream(_copy.toByteArray());
		}
	}	
	
	// TODO
	private String getString(InputStream org) {
		InputStream stream = new CopyInputStream(org).getCopy();
		
		try {
			final char[] buffer = new char[0x10000];
			StringBuilder out = new StringBuilder();
			Reader in = new InputStreamReader(stream, "UTF-8");
			int read;
			do {
				read = in.read(buffer, 0, buffer.length);
				if (read > 0) {
					out.append(buffer, 0, read);
				}
			} while (read >= 0);

			return out.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private boolean isGzipEncoded(HttpInputMessage inputMessage) {
		boolean isGzipInput = false;

		HttpHeaders headers = inputMessage.getHeaders();
		if (headers != null) {
			List<String> contentEncodings = headers.get("Content-Encoding");
			if (contentEncodings != null) {
				for (String contentEncoding : contentEncodings) {
					if (contentEncoding != null
							&& contentEncoding.toLowerCase().contains("gzip")) {
						isGzipInput = true;
						break;
					}
				}
			}
		}

		return isGzipInput;
	}
}
