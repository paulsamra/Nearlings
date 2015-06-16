package com.edbert.library.network.sync;

import java.text.ParseException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.michenux.drodrolib.network.sync.SyncHelperInterface;


import android.accounts.Account;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
/**
 * Modified from Michenux's AbstractSyncAdapter
 * @author cygnus
 *
 * @param <T>
 */
public abstract class AbstractSyncAdapter
		extends AbstractThreadedSyncAdapter {
	private static final String LOG_TAG = "AbstractsyncAdapter";
	public static final String SYNC_FINISHED = "sync_finished";
	public static final String SYNC_STARTED = "sync_started";

	/**
	 * Set up the sync adapter
	 */

	public AbstractSyncAdapter(Context context, boolean autoInitialize) {
		super(context, autoInitialize);

	}

	/**
	 * Set up the sync adapter. This form of the constructor maintains
	 * compatibility with Android 3.0 and later platform versions
	 */

	public AbstractSyncAdapter(Context context, boolean autoInitialize,
			boolean allowParallelSyncs) {
		super(context, autoInitialize, allowParallelSyncs);

	}

	@Override
	public void onSyncCanceled() {
		super.onSyncCanceled();
		LocalBroadcastManager.getInstance(this.getContext()).sendBroadcast(
				new Intent(SYNC_FINISHED));

		turnOffSyncAdapterRunning(null);
	}

	protected void turnOffSyncAdapterRunning(Bundle extras) {
		//SyncHelperManager.getSingleInstance().changeSyncState(helper, false);
		Intent i = new Intent(SYNC_FINISHED);
		i.putExtras(extras);
		LocalBroadcastManager.getInstance(this.getContext()).sendBroadcast(
				i);
	}

	protected void turnOnSyncAdapterRunning(Bundle extras) {
		//SyncHelperManager.getSingleInstance().changeSyncState(helper, true);
		Intent i = new Intent(SYNC_STARTED);
		i.putExtras(extras);
		LocalBroadcastManager.getInstance(this.getContext()).sendBroadcast(
				i);
	}

	@Override
	public void onPerformSync(Account account, Bundle extras, String authority,
			ContentProviderClient provider, SyncResult syncResult) {

		Log.e("Abstract Sync Adapter", "starting sync");
		turnOnSyncAdapterRunning(extras);

		try {
			// sets up retrieval based on whether we have notifications on
			beginSync(provider, extras);
			// AppUsageUtils.updateLastSync(this.getContext());
		} catch (Exception e) {
			Log.e(LOG_TAG, "tutorialSyncAdapter.onPerformSync()", e);
		} finally {

			Log.e("Abstract Sync Adapter", "finished sync");

			turnOffSyncAdapterRunning(extras);
		}
	}

	/**
	 * Very basic hand off
	 */
	protected void beginSync(ContentProviderClient provider, Bundle extras)
			throws Exception {
		preSync();
		Object o = getData(extras);
		postSync(o);
		updateDatabase(extras, o);
	}
	protected void preSync(){
		
	}
	protected void postSync(Object o){
		
	}
	/**
	 * Simple get request
	 */
	protected abstract Object getData(Bundle extras) throws InterruptedException,
			ExecutionException, ParseException, RemoteException,
			OperationApplicationException;

	public abstract void updateDatabase(Bundle extras, Object o) throws RemoteException,
			OperationApplicationException, ParseException;

}