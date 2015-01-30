package swipe.android.nearlings.sync;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

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

	public static final String SYNC_STARTED_FLAG_ID = "NEARLINGS_SYNC_START";

	public static final String SYNC_FINISHED_FLAG_ID = "NEARLINGS_SYNC_FINISHED";

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
			updateDatabase(o);
			extras.remove(HELPER_FLAG_ID);
		}

		Log.d("Finish sync", "finish sync");
		extras.putStringArrayList(HELPER_FLAG_ID, TAG);

		postSync(null);
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
	public void updateDatabase(Object o) throws RemoteException,
			OperationApplicationException, ParseException {

		SendRequestStrategyManager.executeWriteToDatabase(requestInterface,
				getContext(), o);

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

		LocalBroadcastManager.getInstance(this.getContext()).sendBroadcast(
				new Intent(broadcastFinishString));

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