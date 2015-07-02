package swipe.android.nearlings.needs;

import swipe.android.DatabaseHelpers.NeedsDetailsDatabaseHelper;
import swipe.android.nearlings.BaseContainerFragment;
import swipe.android.nearlings.NearlingsContentProvider;
import swipe.android.nearlings.NearlingsSwipeToRefreshFragment;
import swipe.android.nearlings.NeedsDetailsActivity;
import swipe.android.nearlings.MessagesSync.NeedsExploreRequest;
import swipe.android.nearlings.sync.NearlingsSyncAdapter;
import swipe.android.nearlings.viewAdapters.DiscoverListOfNeedsAdapter;
import swipe.android.nearlings.viewAdapters.MessagesViewAdapter;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.edbert.library.sendRequest.SendRequestStrategyManager;

//need to check whether parent clas has sync. In fact, we just need to know how toa ccess it.
public class DiscoverListViewFragment extends NearlingsSwipeToRefreshFragment {

	String MESSAGES_START_FLAG = DiscoverContainerFragment.MESSAGES_START_FLAG;
	String MESSAGES_FINISH_FLAG = DiscoverContainerFragment.MESSAGES_FINISH_FLAG;
	TextView text;


	@Override
	public void onResume() {
		super.onResume();
		reloadData();
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
		helpers.add(SendRequestStrategyManager
				.getHelper(NeedsExploreRequest.class));// new
														// NeedsDetailsRequest(this.getActivity(),
														// JsonExploreResponse.class);
	}

	@Override
	public CursorLoader generateCursorLoader() {

		String allActiveSearch = "";
		String[] activeStates = null;
		CursorLoader cursorLoader = new CursorLoader(
				this.getActivity(),
				NearlingsContentProvider
						.contentURIbyTableName(NeedsDetailsDatabaseHelper.TABLE_NAME),
				NeedsDetailsDatabaseHelper.COLUMNS, allActiveSearch,
				activeStates, NeedsDetailsDatabaseHelper.COLUMN_DUE_DATE + " DESC");

		return cursorLoader;

	}


	int index = 0;
	@Override
	public void reloadData() {


			index = lView.getFirstVisiblePosition();
			getLoaderManager().initLoader(0, null, this);
			c = generateCursor();
			if(mAdapter == null) {
				super.mAdapter = new DiscoverListOfNeedsAdapter(this.getActivity(), c);
				lView.setAdapter(mAdapter);
			}
			mAdapter.changeCursor(c);

			mAdapter.notifyDataSetChanged();

			lView.setAdapter(mAdapter);

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		menu.clear();
		// inflater.inflate(R.menu.switch_to_map_view, menu);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		super.onItemClick(parent, view, position, id);
		Intent intent = new Intent(this.getActivity(),
				NeedsDetailsActivity.class);
		Bundle extras = new Bundle();
		Cursor c = generateCursor();

		c.moveToPosition(position);
		String need_id = c.getString(c
				.getColumnIndex(NeedsDetailsDatabaseHelper.COLUMN_ID));
		extras.putString("id", need_id);
		intent.putExtras(extras);

		startActivity(intent);
	}

	@Override
	protected int setNumElements() {
		return mAdapter.getCount();
	}


	@Override
	public void reloadAdapter() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRefresh() {
		previousAmount = -1;
		((DiscoverContainerFragment) this.getParentFragment()).requestUpdate(BaseContainerFragment.is_reload_and_blank);
		// DiscoverContainerFragment.
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
		((DiscoverContainerFragment) this.getParentFragment()).requestUpdate(BaseContainerFragment.is_loadMore);

	}
}