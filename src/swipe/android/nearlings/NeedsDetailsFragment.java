package swipe.android.nearlings;

import swipe.android.DatabaseHelpers.MessagesDatabaseHelper;
import swipe.android.DatabaseHelpers.NeedsDetailsDatabaseHelper;
import swipe.android.nearlings.MessagesSync.NeedsDetailsRequest;
import swipe.android.nearlings.viewAdapters.DiscoverListOfNeedsAdapter;
import swipe.android.nearlings.viewAdapters.NeedsDetailViewAdapter;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

//need to check whether parent clas has sync. In fact, we just need to know how toa ccess it.
public class NeedsDetailsFragment extends NearlingsSwipeToRefreshFragment
		implements Refreshable {
	String id;
	View view;
	NeedsDetailViewAdapter adapter;

	public static final String MESSAGES_START_FLAG = NeedsDetailsFragment.class
			.getCanonicalName() + "_MESSAGES_START_FLAG";
	public static final String MESSAGES_FINISH_FLAG = NeedsDetailsFragment.class
			.getCanonicalName() + "_MESSAGES_FINISH_FLAG";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	Bundle savedInstanceState;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// we need to refrence our container class. For now, we should just
		// return a text message
		super.onCreateView(inflater, container, savedInstanceState);
		view = inflater.inflate(R.layout.needs_details, container, false);
		this.savedInstanceState = savedInstanceState;
		Bundle b = getActivity().getIntent().getExtras();
		id = b.getString("id");
		
		reloadData();

		return view;

	}

	@Override
	public void onRefresh() {
		// resync data?
		requestSync();
	}

	@Override
	public void setSourceRequestHelper() {
		super.helper = new NeedsDetailsRequest(this.getActivity(), id);
	}

	@Override
	public CursorLoader generateCursorLoader() {
		/*
		 * String selectionClause = NeedsDetailsDatabaseHelper.COLUMN_ID +
		 * " = '" + id + "'";
		 */

		Log.i("CURSOR_ID_GEN", id);
		String selectionClause = NeedsDetailsDatabaseHelper.COLUMN_ID + " = ?";
		String[] mSelectionArgs = { "" };
		mSelectionArgs[0] = id;
		CursorLoader cursorLoader = new CursorLoader(
				this.getActivity(),
				NearlingsContentProvider
						.contentURIbyTableName(NeedsDetailsDatabaseHelper.TABLE_NAME),
				NeedsDetailsDatabaseHelper.COLUMNS, selectionClause,
				mSelectionArgs, NeedsDetailsDatabaseHelper.COLUMN_DATE
						+ " DESC");
		return cursorLoader;

	}

	Cursor c;

	@Override
	public void reloadData() {
		// TODO Auto-generated method stub
		// getLoaderManager().initLoader(0, null, this);
		reloadAdapter();

		//adapter = new NeedsDetailViewAdapter(view, this.getActivity(), id, savedInstanceState);

	
		/*
		 * mAdapter = new NeedsDetailViewAdapter(this.getActivity(), id, c,
		 * savedInstanceState);
		 * 
		 * // this.mAdapter = new NeedsDetailViewAdapter(this.getActivity(), c);
		 * 
		 * mAdapter.();
		 */
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
		
			adapter = new NeedsDetailViewAdapter(view, this.getActivity(), id, c, savedInstanceState);
		
		adapter.reloadData();
	}

}