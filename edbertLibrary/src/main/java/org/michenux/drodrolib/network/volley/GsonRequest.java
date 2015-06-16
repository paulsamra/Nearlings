package org.michenux.drodrolib.network.volley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.michenux.drodrolib.network.json.TimestampDeserializer;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public class GsonRequest<T> extends Request<T> {

	private final Gson gson;
	private final Class<T> clazz;
	private final Map<String, String> headers;
	private final Listener<T> listener;

	private static final String KEY_PICTURE = "picture";
	private HttpEntity mHttpEntity;

	public GsonRequest(int method, String url, Class<T> clazz,
			Map<String, String> headers, Listener<T> listener,
			ErrorListener errorListener) {
		super(method, url, errorListener);

		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Timestamp.class,
				new TimestampDeserializer());
		gsonBuilder
				.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);

		this.gson = gsonBuilder.create();
		this.clazz = clazz;
		this.headers = headers;
		this.listener = listener;
	}
	
	public GsonRequest(int method, String url, File file, Class clazz,
			Map<String, String> headers, Response.Listener listener,
			Response.ErrorListener errorListener, String body) {
		super(method, url, errorListener);

		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Timestamp.class,
				new TimestampDeserializer());
		gsonBuilder
				.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);

		this.gson = gsonBuilder.create();
		this.clazz = clazz;
		this.headers = headers;
		this.listener = listener;

		mHttpEntity = buildMultipartEntity(file);
	}
	public GsonRequest(int method, String url, Class clazz,
			Map<String, String> headers, Response.Listener listener,
			Response.ErrorListener errorListener, String body) {
		super(method, url, errorListener);

		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Timestamp.class,
				new TimestampDeserializer());
		gsonBuilder
				.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);

		this.gson = gsonBuilder.create();
		this.clazz = clazz;
		this.headers = headers;
		this.listener = listener;

		try {
			mHttpEntity = buildMultipartEntity(body);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		return headers != null ? headers : super.getHeaders();
	}

	@Override
	protected void deliverResponse(T response) {
		listener.onResponse(response);
	}

	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		try {
			String json = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
			return Response.success(gson.fromJson(json, clazz),
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException | JsonSyntaxException e) {
			return Response.error(new ParseError(e));
		}
	}



	private HttpEntity buildMultipartEntity(String body) throws Exception{
		String result = "";
		if (body != null) {
			String xml = body;
			return new ByteArrayEntity(xml.getBytes("UTF-8"));
		}
		return null;
	}

	private HttpEntity buildMultipartEntity(File file) {

		headers.put("enctype", "multipart/form-data");

		MultipartEntityBuilder builder = MultipartEntityBuilder.create();

		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

		if (file != null && file.exists()) {

			FileBody bin = new FileBody(file, "image.jpg", "image/jpg", "utf-8");
			// bin.getMediaType();
			// mpEntity.addPart("picture", bin);
			builder.addPart("image", bin);
		}
		
		return builder.build();
	}

	@Override
	public String getBodyContentType() {
		if(mHttpEntity != null){
		return mHttpEntity.getContentType().getValue();
		}
		else return super.getBodyContentType();
	}

	@Override
	public byte[] getBody() throws AuthFailureError {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			mHttpEntity.writeTo(bos);
		} catch (IOException e) {
			VolleyLog.e("IOException writing to ByteArrayOutputStream");
			return super.getBody();
		}finally{
		return bos.toByteArray();
		}
	}

}