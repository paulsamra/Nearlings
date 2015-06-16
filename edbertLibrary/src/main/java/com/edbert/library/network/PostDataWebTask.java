
/**
 * We use this instead of a SocketOperator because this allows us to also throw messages
 * back to the calling activity. It also prevents us from having to do any messy
 * multithreading declarations. AKA this is just a simplified class.
 */

package com.edbert.library.network;

import java.io.File;
import java.util.Map;

import com.edbert.library.utils.MapUtils;


import android.app.Activity;
import com.edbert.library.network.SocketOperator;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

public class PostDataWebTask<T> extends GetDataWebTask<T> {

	public PostDataWebTask(Context act, Class<T> type) {
		super(act, type);
	}
	public PostDataWebTask(Context act, Class<T> type, boolean b) {
		super(act, type);
		this.showDialog = b;
	}

	public PostDataWebTask(Context act, AsyncTaskCompleteListener f, Class<T> type) {
		super(act, f, type);
	}

	public PostDataWebTask(Context act, AsyncTaskCompleteListener f, Class<T> type, boolean showDialog) {
		super(act, f, type, showDialog);
	}
	
	File file;

	public PostDataWebTask(Context act, Class<T> type, File file) {
		super(act, type);
		this.file = file;
	}
	public PostDataWebTask(Context act, Class<T> type, File file, boolean showDialog) {
		super(act, type);
		this.file = file;
		this.showDialog = showDialog;
	}

	@Override
	protected T doInBackground(String... uri) {
		Map<String, String> headers = null;
		if (uri[1] != null) {
			headers = MapUtils.stringToMap(uri[1]);
		}
		if (file != null) {
			return (T) SocketOperator.getInstance(classType).postResponse(
					activity.getApplicationContext(), uri[0], headers, uri[2],
					file);
		}
		if(uri.length >= 3) {
			return (T) SocketOperator.getInstance(classType).postResponse(
					activity, uri[0], headers, uri[2]);
		}
		else
			return (T) SocketOperator.getInstance(classType).postResponse(
					activity, uri[0], headers);

	}

}