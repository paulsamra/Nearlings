
/**
 * We use this instead of a SocketOperator because this allows us to also throw messages
 * back to the calling activity. It also prevents us from having to do any messy
 * multithreading declarations. AKA this is just a simplified class.
 */

package com.edbert.library.network;

import java.util.Map;

import com.edbert.library.utils.MapUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

public class GetDataWebTask<T> extends AsyncTask<String, Void, T> {

	protected Context activity;
	protected ProgressDialog dialog;
	protected AsyncTaskCompleteListener<T> callback;
	protected Class<T> classType;
	protected boolean showDialog;
	protected Context ctx;

	public GetDataWebTask(Context act, Class<T> type) {
		this.activity = act;
		this.callback = (AsyncTaskCompleteListener<T>) act;
		this.classType = type;
		showDialog = true;
	}
	public GetDataWebTask(Context act, AsyncTaskCompleteListener<T> asyncListener, Class<T> type) {
		this.activity = act;
		this.callback =  asyncListener;
		this.classType = type;
		showDialog = true;
	}
	public GetDataWebTask(Context act, Class<T> type, boolean showDialog) {
		this.activity = act;
		this.callback = (AsyncTaskCompleteListener<T>) act;
		this.classType = type;
		this.showDialog = showDialog;
	}

	public GetDataWebTask(Activity act, AsyncTaskCompleteListener f, Class<T> type, boolean showDialog) {
		this.activity = act;
		this.callback = (AsyncTaskCompleteListener) f;
		this.classType = type;
		this.showDialog = showDialog;
	}
	
	public GetDataWebTask(Context ctx,AsyncTaskCompleteListener listener,  Class<T> type, boolean showDialog) {
		this.ctx = ctx;
		this.callback = listener;
		this.classType = type;
		this.showDialog = showDialog;
	}
	
	protected boolean passedIn = false;

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (dialog == null && this.showDialog) {
			passedIn = true;
			dialog = new ProgressDialog(activity);
			dialog.setMessage("Loading...");
			dialog.show();
		}
	}

	@Override
	protected T doInBackground(String... uri) {
		Map<String, String> headers = null;
		if (uri.length > 1 && uri[1] != null) {
			headers = MapUtils.stringToMap(uri[1]);
		}
		if(activity == null){
			return (T) SocketOperator.getInstance(classType).getResponse(
					ctx, uri[0], headers);
		}
		return (T) SocketOperator.getInstance(classType).getResponse(
				activity.getApplicationContext(), uri[0], headers);
		// SocketOperator.httpGetRequest(uri[0], headers);
	}

	@Override
	protected void onPostExecute(T result) {
		super.onPostExecute(result);
		if ( this.showDialog && null != dialog && dialog.isShowing() && passedIn ) {
			dialog.dismiss();
		}
		callback.onTaskComplete(result);
	}

}