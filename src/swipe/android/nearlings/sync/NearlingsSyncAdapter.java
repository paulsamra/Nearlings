package swipe.android.nearlings.sync;

import java.text.ParseException;
import java.util.concurrent.ExecutionException;

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
	protected Object getData(Bundle extras) throws InterruptedException,
			ExecutionException, ParseException, RemoteException,
			OperationApplicationException {
		String TAG = extras.getString(HELPER_FLAG_ID);
		requestInterface = SendRequestStrategyManager.getHelper(TAG);
		return SendRequestStrategyManager.executeRequest(requestInterface,
				extras);
	}

	@Override
	public void updateDatabase(Object o) throws RemoteException,
			OperationApplicationException, ParseException {

		SendRequestStrategyManager.executeWriteToDatabase(requestInterface,
				getContext(), o);
	}

	@Override
	protected void turnOffSyncAdapterRunning(Bundle extras) {
		String broadcastFinishString;
		if (extras == null) {
			broadcastFinishString = SYNC_FINISHED;
		} else {
			broadcastFinishString = extras.getString(SYNC_FINISHED_FLAG_ID);
		}

		LocalBroadcastManager.getInstance(this.getContext()).sendBroadcast(
				new Intent(broadcastFinishString));

		Log.e(SYNC_ADAPTER_TAG, broadcastFinishString);
	}

	@Override
	protected void turnOnSyncAdapterRunning(Bundle extras) {
		String broadcastStartString;
		if (extras == null) {
			broadcastStartString = SYNC_STARTED;
		} else {
			broadcastStartString = extras.getString(SYNC_STARTED_FLAG_ID);
		}

		LocalBroadcastManager.getInstance(this.getContext()).sendBroadcast(
				new Intent(broadcastStartString));
		Log.e(SYNC_ADAPTER_TAG, broadcastStartString);
	}

}