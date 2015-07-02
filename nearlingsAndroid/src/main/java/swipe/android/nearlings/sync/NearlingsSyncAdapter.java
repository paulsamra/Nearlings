package swipe.android.nearlings.sync;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import swipe.android.nearlings.ExpiredSessionException;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.edbert.library.network.sync.AbstractSyncAdapter;
import com.edbert.library.sendRequest.SendRequestInterface;
import com.edbert.library.sendRequest.SendRequestStrategyManager;
import com.edbert.library.utils.ListUtils;

public class NearlingsSyncAdapter extends AbstractSyncAdapter {
	private static final String SYNC_ADAPTER_TAG = "NearlingssyncAdapter";
	public static final String HELPER_FLAG_ID = "NEARLINGS_HELPER";
	public static final String SESSION_IS_BAD = "SESSION_IS_BAD";

	public static final String SYNC_STARTED_FLAG_ID = "NEARLINGS_SYNC_START";

	public static final String SYNC_FINISHED_FLAG_ID = "NEARLINGS_SYNC_FINISHED";
public static final String CLEAR_DB = "CLEAR_DB";
	public static final String LIMIT = "LIMIT";
	public NearlingsSyncAdapter(Context context, boolean autoInitialize) {
		super(context, autoInitialize);

	}

	private SendRequestInterface requestInterface;

	@Override
	protected void beginSync(ContentProviderClient provider, Bundle extras)
			throws Exception {
		preSync();
		// fuck it we'll do it sequentially
		String helpers = extras.getString(HELPER_FLAG_ID);
		ArrayList<String> TAG = ListUtils.stringToList(helpers);
		extras.remove(HELPER_FLAG_ID);

		Log.d("Starting sync", "starting sync");
		for (String tags : TAG) {
			Log.d("TAG", tags);
			extras.putString(HELPER_FLAG_ID, tags);
			Object o = getData(extras);
			boolean b = false;
			try {
				b = executePostData(extras, o);
			} catch (ExpiredSessionException e) {
				extras.putStringArrayList(HELPER_FLAG_ID, TAG);
				extras.putBoolean("SESSION_IS_BAD", true);
Log.d("Caught bad session", "Caught bad session");
				postSync(null);
				return;
			}
			if (b)
				updateDatabase(extras, o);
			extras.remove(HELPER_FLAG_ID);
		}

		Log.d("Finish sync", "finish sync");
		extras.putStringArrayList(HELPER_FLAG_ID, TAG);
		extras.putBoolean("SESSION_IS_BAD", false);

		postSync(null);
	}

	protected boolean executePostData(Bundle extras, Object o) throws Exception {
		String TAG = extras.getString(HELPER_FLAG_ID);
		requestInterface = SendRequestStrategyManager.getHelper(TAG);
		if (requestInterface == null) {
			Log.e("NearlingsSyncAdapter",
					"No requestInterface was provided! Will not execute!");
			return false;
		}

		return SendRequestStrategyManager.executePostRetrieval(
				requestInterface, this.getContext(), o, extras);

	}

	@Override
	protected Object getData(Bundle extras) throws InterruptedException,
			ExecutionException, ParseException, RemoteException,
			OperationApplicationException {
		String TAG = extras.getString(HELPER_FLAG_ID);
		requestInterface = SendRequestStrategyManager.getHelper(TAG);
		if (requestInterface == null) {
			Log.e("NearlingsSyncAdapter",
					"No requestInterface was provided! Will not execute!");
			return null;
		}
		Object o = SendRequestStrategyManager.executeRequest(requestInterface,
				extras);
		return o;

	}

	@Override
	public void updateDatabase(Bundle extras, Object o) throws RemoteException,
			OperationApplicationException, ParseException {

		SendRequestStrategyManager.executeWriteToDatabase(requestInterface,
				getContext(), o, extras);

		requestInterface = null;
	}

	@Override
	protected void turnOffSyncAdapterRunning(Bundle extras) {
		  
		 
		String broadcastFinishString = "";
		if (extras == null) {
			broadcastFinishString = SYNC_FINISHED;
		} else if (extras.getString(SYNC_FINISHED_FLAG_ID) != null) {
			broadcastFinishString = extras.getString(SYNC_FINISHED_FLAG_ID);
		} else {
			broadcastFinishString = "NO_SYNC_FINISHED_STRING";
		}
Intent i = new Intent(broadcastFinishString);
i.putExtras(extras);
		LocalBroadcastManager.getInstance(this.getContext()).sendBroadcast(
				i);

		Log.e(SYNC_ADAPTER_TAG, broadcastFinishString);
	}

	@Override
	protected void turnOnSyncAdapterRunning(Bundle extras) {
		String broadcastStartString = "";
		if (extras == null) {
			broadcastStartString = SYNC_STARTED;
		} else if (extras.getString(SYNC_STARTED_FLAG_ID) != null) {
			broadcastStartString = extras.getString(SYNC_STARTED_FLAG_ID);
		} else {
			broadcastStartString = "NO_SYNC_START_STRING";
		}

		LocalBroadcastManager.getInstance(this.getContext()).sendBroadcast(
				new Intent(broadcastStartString));
		Log.e(SYNC_ADAPTER_TAG, broadcastStartString);
	}

}