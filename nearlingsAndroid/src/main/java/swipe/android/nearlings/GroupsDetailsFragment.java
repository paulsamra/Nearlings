package swipe.android.nearlings;

import swipe.android.DatabaseHelpers.GroupsDatabaseHelper;
import swipe.android.nearlings.MessagesSync.NeedsExploreRequest;
import swipe.android.nearlings.viewAdapters.GroupsViewAdapter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

//need to check whether parent clas has sync. In fact, we just need to know how toa ccess it.
public class GroupsDetailsFragment extends NearlingsSwipeToRefreshFragment
		implements Refreshable {
	String id;
	View view;
	GroupsViewAdapter adapter;

	public static final String MESSAGES_START_FLAG = GroupsDetailsFragment.class
			.getCanonicalName() + "_MESSAGES_START_FLAG";
	public static final String MESSAGES_FINISH_FLAG = GroupsDetailsFragment.class
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
		ActionBar ab = ((ActionBarActivity)this.getActivity()).getSupportActionBar();
		ab.setDisplayShowTitleEnabled(true);
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setHomeButtonEnabled(true);

		if (view != null) {
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null)
				parent.removeView(view);
		}
		try {
			view = inflater.inflate(R.layout.groups_detail, container, false);

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
		helpers.add(new NeedsExploreRequest(this.getActivity()));
	}

	@Override
	public CursorLoader generateCursorLoader() {
		String selectionClause = GroupsDatabaseHelper.COLUMN_ID + " = ?";
		String[] mSelectionArgs = { "" };
		mSelectionArgs[0] = id;
		CursorLoader cursorLoader = new CursorLoader(
				this.getActivity(),
				NearlingsContentProvider
						.contentURIbyTableName(GroupsDatabaseHelper.TABLE_NAME),
				GroupsDatabaseHelper.COLUMNS, selectionClause, mSelectionArgs,
				GroupsDatabaseHelper.COLUMN_DATE + " DESC");
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
			adapter = new GroupsViewAdapter(view, this.getActivity(), id, c,
					savedInstanceState);

		adapter.reloadData();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		this.getActivity().onBackPressed();

		return super.onOptionsItemSelected(item);
	}
	@Override
	protected int setNumElements() {
		return mAdapter.getCount();
	}
}
