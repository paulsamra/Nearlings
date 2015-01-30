package swipe.android.nearlings.needs;

import swipe.android.DatabaseHelpers.NeedsDetailsDatabaseHelper;
import swipe.android.nearlings.NearlingsContentProvider;
import swipe.android.nearlings.NearlingsSwipeToRefreshFragment;
import swipe.android.nearlings.NeedsDetailsActivity;
import swipe.android.nearlings.MessagesSync.NeedsExploreRequest;
import swipe.android.nearlings.viewAdapters.DiscoverListOfNeedsAdapter;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
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

	Cursor c;

	@Override
	public void reloadData() {
		getLoaderManager().initLoader(0, null, this);
		c = generateCursor();

		this.mAdapter = new DiscoverListOfNeedsAdapter(this.getActivity(), c);

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
	public void reloadAdapter() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRefresh() {
		((DiscoverContainerFragment) this.getParentFragment()).requestUpdate();
		// DiscoverContainerFragment.
	}
}