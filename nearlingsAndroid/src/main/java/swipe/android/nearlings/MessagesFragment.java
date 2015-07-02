package swipe.android.nearlings;

import swipe.android.DatabaseHelpers.MessagesDatabaseHelper;
import swipe.android.DatabaseHelpers.NeedsDetailsDatabaseHelper;
import swipe.android.nearlings.MessagesSync.MessagesRequest;
import swipe.android.nearlings.json.alerts.Alerts;
import swipe.android.nearlings.viewAdapters.MessagesViewAdapter;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;

import android.os.Handler;

import android.support.v4.content.CursorLoader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import android.view.View.OnClickListener;
import android.widget.ProgressBar;

import java.util.TimerTask;

import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MessagesFragment extends NearlingsSwipeToRefreshFragment {

	String MESSAGES_START_FLAG = MessagesFragment.class.getCanonicalName()
			+ "_MESSAGES_START_FLAG";
	String MESSAGES_FINISH_FLAG = MessagesFragment.class.getCanonicalName()
			+ "_MESSAGES_FINISH_FLAG";

	@Override
	public CursorLoader generateCursorLoader() {
		CursorLoader cursorLoader = new CursorLoader(
				this.getActivity(),
				NearlingsContentProvider
						.contentURIbyTableName(MessagesDatabaseHelper.TABLE_NAME),
				MessagesDatabaseHelper.COLUMNS, null, null,
				MessagesDatabaseHelper.COLUMN_DATE + " DESC");

		return cursorLoader;

	}

	@Override
	public void reloadData() {
		// onRefresh();
		reloadAdapter();
	}

	View footerView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(R.layout.pull_to_refresh_single_list,
				container, false);

		((ActionBarActivity)this.getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
		((ActionBarActivity)this.getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		((ActionBarActivity)this.getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
		((ActionBarActivity)this.getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
		((ActionBarActivity)this.getActivity()).getSupportActionBar().setTitle("Messages");
		swipeView = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe);
		swipeView.setColorScheme(android.R.color.holo_blue_dark,
				android.R.color.holo_blue_light,
				android.R.color.holo_green_light,
				android.R.color.holo_green_light);
		lView = (ListView) rootView.findViewById(R.id.list);

		swipeView.setOnRefreshListener(this);
		lView.setOnItemClickListener(this);
		lView.setOnScrollListener(this);
		setUpFooter(inflater);
		reloadAdapter();


		return rootView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	public void initializeTimerTask() {

		timerTask = new TimerTask() {
			public void run() {

				//use a handler to run a toast that shows the current timestamp
				MessagesFragment.this.getActivity().runOnUiThread(new Runnable() {
					public void run() {
						//get the current timeStamp
						onRefresh(null, false);
					}
				});
			}
		};
	}
	Timer timer;
	TimerTask timerTask;
	final Handler handler = new Handler();
	@Override
	public void onResume() {
		super.onResume();

		onRefresh();
	startTimer();

	}
	@Override
	public void onPause(){
		super.onPause();
		stoptimertask();
	}
	public void startTimer() {
		//set a new Timer

		timer = new Timer();

		initializeTimerTask();

		timer.schedule(timerTask, 30*1000, 30*1000);
	}

	public void stoptimertask() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	@Override
	public String syncStartedFlag() {
		return MESSAGES_START_FLAG;
	}

	@Override
	public String syncFinishedFlag() {
		return MESSAGES_FINISH_FLAG;
	}

	@Override
	public void setSourceRequestHelper() {
		helpers.add(new MessagesRequest(this.getActivity()));
	}

	@Override
	protected int setNumElements() {
		return mAdapter.getCount();
	}


int index = 0;
	Cursor c;
	@Override
	public void reloadAdapter() {
		index = lView.getFirstVisiblePosition();
		getLoaderManager().initLoader(0, null, this);
		 c = generateCursor();
if(mAdapter == null) {
	this.mAdapter = new MessagesViewAdapter(this.getActivity(), c);
	lView.setAdapter(mAdapter);
}
		Log.d("Size", String.valueOf(c.getCount()));
		mAdapter.changeCursor(c);

		mAdapter.notifyDataSetChanged();

		//lView.setAdapter(mAdapter);
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
			this.getActivity().onBackPressed();
			
		return true;
	}
	// what do we do when we click the item?
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		Cursor c = generateCursor();
		c.moveToPosition(position);
		int time_index = c
				.getColumnIndexOrThrow(MessagesDatabaseHelper.COLUMN_DATE);
		int message_index = c
				.getColumnIndexOrThrow(MessagesDatabaseHelper.COLUMN_MESSAGE);

		int unread_index = c
				.getColumnIndexOrThrow(MessagesDatabaseHelper.COLUMN_UNREAD);

		int id_index = c
				.getColumnIndexOrThrow(MessagesDatabaseHelper.COLUMN_ID);


		Alerts a = new Alerts();
		a.setTimestamp(c.getString(time_index));
		a.setMessage(c.getString(message_index));
		a.setMessage_id(c.getString(id_index));

		ContentValues cv = new ContentValues();

		cv.put(MessagesDatabaseHelper.COLUMN_ID, a.getMessage_id());
		cv.put(MessagesDatabaseHelper.COLUMN_MESSAGE, a.getMessage());
		cv.put(MessagesDatabaseHelper.COLUMN_DATE, Long.valueOf(a.getTimestamp()));

		cv.put(MessagesDatabaseHelper.COLUMN_UNREAD, "False");

		String selectionClause = NeedsDetailsDatabaseHelper.COLUMN_ID + " = ?";
		String[] mSelectionArgs = { "" };
		mSelectionArgs[0] = a.getMessage_id();
		MessagesFragment.this.getActivity().getContentResolver()
				.update(NearlingsContentProvider
								.contentURIbyTableName(MessagesDatabaseHelper.TABLE_NAME),
						cv, selectionClause, mSelectionArgs);

//Cursor d = generateCursor();
//		DatabaseUtils.dumpCursor(d);
		mAdapter.notifyDataSetChanged();
		mAdapter.notifyDataSetInvalidated();
		lView.invalidateViews();
		MessagesFragment.this.getActivity().runOnUiThread(new Runnable() {

			public void run() {
				mAdapter.notifyDataSetChanged();
				mAdapter.notifyDataSetInvalidated();
				lView.invalidateViews();

			}
		});
	}


}