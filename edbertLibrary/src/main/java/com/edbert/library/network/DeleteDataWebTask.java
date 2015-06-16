package com.edbert.library.network;


import java.util.Map;

import com.edbert.library.utils.MapUtils;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

public class DeleteDataWebTask<T> extends GetDataWebTask<T> {

	public DeleteDataWebTask(Context act, Class<T> type) {
		super(act, type);
	}

	public DeleteDataWebTask(Context act, AsyncTaskCompleteListener f, Class<T> type) {
		super(act, f, type);
	}

	@Override
	protected T doInBackground(String... uri) {
		Map<String, String> headers = null;
		if (uri[1] != null) {
			headers = MapUtils.stringToMap(uri[1]);
		}
		return (T) SocketOperator.getInstance(classType).deleteResponse(
				activity.getApplicationContext(), uri[0], headers);

	}

}