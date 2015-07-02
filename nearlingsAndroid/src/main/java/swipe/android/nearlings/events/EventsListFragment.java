package swipe.android.nearlings.events;

import swipe.android.DatabaseHelpers.EventsDatabaseHelper;
import swipe.android.nearlings.BaseContainerFragment;
import swipe.android.nearlings.CreateEventActivity;
import swipe.android.nearlings.EventsDetailsActivity;
import swipe.android.nearlings.NearlingsContentProvider;
import swipe.android.nearlings.NearlingsSwipeToRefreshFragment;
import swipe.android.nearlings.R;
import swipe.android.nearlings.MessagesSync.EventsDetailsRequest;
import swipe.android.nearlings.groups.GroupsContainerFragment;
import swipe.android.nearlings.sync.NearlingsSyncAdapter;
import swipe.android.nearlings.viewAdapters.EventsListAdapter;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

public class EventsListFragment extends NearlingsSwipeToRefreshFragment {

	String MESSAGES_START_FLAG = EventsContainerFragment.MESSAGES_START_FLAG;
	String MESSAGES_FINISH_FLAG = EventsContainerFragment.MESSAGES_FINISH_FLAG;
	@Override
	protected int setNumElements() {
		return mAdapter.getCount();
	}
	@Override
	public CursorLoader generateCursorLoader() {
		CursorLoader cursorLoader = new CursorLoader(
				this.getActivity(),
				NearlingsContentProvider
						.contentURIbyTableName(EventsDatabaseHelper.TABLE_NAME),
				EventsDatabaseHelper.COLUMNS, null, null,
				EventsDatabaseHelper.COLUMN_TIME_OF_EVENT + " DESC");

		return cursorLoader;

	}

	@Override
	public void reloadData() {
		reloadAdapter();

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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
		helpers.add(new EventsDetailsRequest(this.getActivity()));
	}

	@Override
	public void onResume() {
		super.onResume();
		reloadData();
	}

	@Override
	public void reloadAdapter() {
		getLoaderManager().initLoader(0, null, this);

		Cursor c = generateCursor();

		this.mAdapter = new EventsListAdapter(this.getActivity(), c, lView);

		mAdapter.notifyDataSetChanged();
		lView.setAdapter(mAdapter);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// inflater.inflate(R.menu.events_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.add_event:
			Intent intent = new Intent(this.getActivity(),
					CreateEventActivity.class);
			startActivity(intent);
			return true;

		case R.id.refresh_details:
			super.onRefresh();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onRefresh() {
		previousAmount = -1;
		((EventsContainerFragment) this.getParentFragment()).requestUpdate(BaseContainerFragment.is_reload_and_blank);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		super.onItemClick(parent, view, position, id);
		Intent intent = new Intent(this.getActivity(),
				EventsDetailsActivity.class);
		Bundle extras = new Bundle();
		Cursor c = generateCursor();

		c.moveToPosition(position);
		String need_id = c.getString(c
				.getColumnIndex(EventsDatabaseHelper.COLUMN_ID));
		extras.putString("id", need_id);
		intent.putExtras(extras);

		startActivity(intent);
		Log.d("CLICKEd", "CLICKED");
	}


	protected void loadMoreArticles(int currentPage) {

		Log.d("Load more", " load more");
		footerView.setVisibility(View.VISIBLE);
		// load more but we need to change the base URL this time
		Bundle data = new Bundle();


		// need to put in number of elements
		int numOflistElements = mAdapter.getCount();

		if (numOflistElements == previousAmount){
			footerView.setVisibility(View.GONE);
			return;
		}
		data.putInt(NearlingsSyncAdapter.LIMIT,
				numOflistElements);


		previousAmount = numOflistElements;
		//onRefresh(data, false);
		((EventsContainerFragment) this.getParentFragment()).requestUpdate(BaseContainerFragment.is_loadMore);

	}
}