package swipe.android.nearlings;

import org.json.JSONException;
import org.json.JSONObject;

import swipe.android.nearlings.googleplaces.GoogleParser;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.edbert.library.network.AsyncTaskCompleteListener;
import com.edbert.library.network.SocketOperator;
import com.edbert.library.utils.MapUtils;
public class CreateItemNearlings extends AsyncTask<String, Void, String> {
	protected Activity activity;
	protected ProgressDialog dialog;
	protected AsyncTaskCompleteListener callback;
	protected Class classType;
	protected boolean showDialog;
	protected Context ctx;

	public CreateItemNearlings(Activity act) {
		this.activity = act;
		this.callback = (AsyncTaskCompleteListener) act;
		showDialog = true;
	}
	public CreateItemNearlings(Activity act, AsyncTaskCompleteListener asyncListener) {
		this.activity = act;
		this.callback =  asyncListener;
		showDialog = true;
	}
	public CreateItemNearlings(Activity act, boolean showDialog) {
		this.activity = act;
		this.callback = (AsyncTaskCompleteListener) act;
		this.showDialog = showDialog;
	}
	public CreateItemNearlings(Activity act, Fragment f) {
		this.activity = act;
		this.callback = (AsyncTaskCompleteListener) f;
		showDialog = true;
	}

	public CreateItemNearlings(Activity act, Fragment f,  boolean showDialog) {
		this.activity = act;
		this.callback = (AsyncTaskCompleteListener) f;
		this.showDialog = showDialog;
	}
	
	public CreateItemNearlings(AsyncTaskCompleteListener listener, Context ctx,  boolean showDialog) {
		this.ctx = ctx;
		this.callback = listener;
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
		protected String doInBackground(String... params) {
			//1. Grab the google place
			String s ="";
			try {	
			JSONObject createRequest = new JSONObject(params[2]);
			
			String location = createRequest.getString("location");
		JSONObject locationInfo = GoogleParser.getLocationInfo(location);
				
			double[] locations = GoogleParser.getLatLong(locationInfo);
			
			//if we cannot, then we exit
			if(locations[0] == 0 && locations[1] == 0 || locationInfo == null){
				return null;
			}
			//build the request
			
			//2. Try to send the request
			
			createRequest.remove("location");
			
				createRequest.put("latitude", locations[1]);
				createRequest.put("longitude", locations[0]);
				Log.d("BODY", createRequest.toString());
				Log.d("URL", params[0]);
				 s = SocketOperator.getInstance(String.class).httpPostRequest(params[0], MapUtils.stringToMap(params[1]), createRequest.toString());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			return s;
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if ( this.showDialog && null != dialog && dialog.isShowing() && passedIn ) {
				dialog.dismiss();
			}
			callback.onTaskComplete(result);
		}
		
	}