
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
import android.util.Log;

//How to use: new MyTask(this).execute("http://samir-mangroliya.blogspot.in");
public class PutDataWebTask<T> extends AsyncTask<String, String, T> {

	protected Context activity;
	protected ProgressDialog dialog;
	protected AsyncTaskCompleteListener callback;
	public Class<T> classType;
	boolean showDialog;

	public PutDataWebTask(Context act, Class<T> type) {
		this.activity = act;
		this.callback = (AsyncTaskCompleteListener) act;
		this.classType = type;
		showDialog = true;
	}
	public PutDataWebTask(Context act, Class<T> type, boolean showDialog) {
		this.activity = act;
		this.callback = (AsyncTaskCompleteListener) act;
		this.classType = type;
		this.showDialog = showDialog;
	}
	public PutDataWebTask(Context act, Context f, Class<T> type) {
		this.activity = act;
		this.callback = (AsyncTaskCompleteListener) f;
		this.classType = type;
		showDialog = true;
	}

	public PutDataWebTask(Context act, AsyncTaskCompleteListener f, Class<T> type, boolean showDialog) {
		this.activity = act;
		this.callback = (AsyncTaskCompleteListener) f;
		this.classType = type;
		this.showDialog = showDialog;
	}
	
	
	boolean passedIn = false;

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
		if (uri[1] != null) {
			headers = MapUtils.stringToMap(uri[1]);
		}
		if(activity != null && uri[2] != null){
			Log.d("uri is null", uri[2]);
			return (T) SocketOperator.getInstance(classType).putResponse(
					activity, uri[0], headers, uri[2]);
		}else{
			Log.d("uri is  null", "uri is  null");
			return (T) SocketOperator.getInstance(classType).putResponse(
					activity, uri[0], headers,"");
		}
	
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