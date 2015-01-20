package swipe.android.nearlings;

import swipe.android.DatabaseHelpers.EventsDatabaseHelper;
import swipe.android.nearlings.MessagesSync.NeedsDetailsRequest;
import swipe.android.nearlings.viewAdapters.EventsDetailAdapter;
import swipe.android.nearlings.viewAdapters.EventsListAdapter;
import swipe.android.nearlings.viewAdapters.GroupsViewAdapter;
import android.app.ActionBar;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.CursorLoader;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

//need to check whether parent clas has sync. In fact, we just need to know how toa ccess it.
public class EventsDetailsFragment extends NearlingsSwipeToRefreshFragment
		implements Refreshable {
	String id;
	View view;
	EventsDetailAdapter adapter;

	public static final String MESSAGES_START_FLAG = EventsDetailsFragment.class
			.getCanonicalName() + "_MESSAGES_START_FLAG";
	public static final String MESSAGES_FINISH_FLAG = EventsDetailsFragment.class
			.getCanonicalName() + "_MESSAGES_FINISH_FLAG";
	FragmentManager fm;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		fm = this.getFragmentManager();
	}

	Bundle savedInstanceState;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// we need to refrence our container class. For now, we should just
		// return a text message
		super.onCreateView(inflater, container, savedInstanceState);
		ActionBar ab = this.getActivity().getActionBar();
		ab.setDisplayShowTitleEnabled(true);
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setHomeButtonEnabled(true);


	
		if (view != null) {
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null)
				parent.removeView(view);
		}
		try {
			view = inflater.inflate(R.layout.events_details, container, false);

		} catch (InflateException e) {

		}

		this.savedInstanceState = savedInstanceState;
		Bundle b = getActivity().getIntent().getExtras();
		id = b.getString("id");

		reloadData();

		return view;

	}

	@Override
	public void onRefresh() {
		// resync data?
		super.onRefresh();
		// reloadAdapter();
	}

	@Override
	public void setSourceRequestHelper() {
		super.helper = new NeedsDetailsRequest(this.getActivity(), id);
	}

	@Override
	public CursorLoader generateCursorLoader() {
		String selectionClause = EventsDatabaseHelper.COLUMN_ID + " = ?";
		String[] mSelectionArgs = { "" };
		mSelectionArgs[0] = id;
		CursorLoader cursorLoader = new CursorLoader(
				this.getActivity(),
				NearlingsContentProvider
						.contentURIbyTableName(EventsDatabaseHelper.TABLE_NAME),
						EventsDatabaseHelper.COLUMNS, selectionClause,
				mSelectionArgs, EventsDatabaseHelper.COLUMN_DATE_OF_EVENT
						+ " DESC");
		return cursorLoader;

	}

	Cursor c;

	@Override
	public void onResume() {
		super.onResume();
		reloadAdapter();

	}

	@Override
	public void reloadData() {
	
		reloadAdapter();

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
	public void reloadAdapter() {
		getLoaderManager().initLoader(0, null, this);
		c = generateCursor();
		if (adapter == null)
			adapter = new EventsDetailAdapter(view, this.getActivity(), id,
					c, savedInstanceState);

		adapter.reloadData();
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
			this.getActivity().onBackPressed();
		return super.onOptionsItemSelected(item);
	}
}