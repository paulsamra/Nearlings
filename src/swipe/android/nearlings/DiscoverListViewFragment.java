package swipe.android.nearlings;

import swipe.android.DatabaseHelpers.MessagesDatabaseHelper;
import swipe.android.DatabaseHelpers.NeedsDetailsDatabaseHelper;
import swipe.android.nearlings.MessagesSync.NeedsDetailsRequest;
import swipe.android.nearlings.viewAdapters.DiscoverListOfNeedsAdapter;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

//need to check whether parent clas has sync. In fact, we just need to know how toa ccess it.
public class DiscoverListViewFragment extends NearlingsSwipeToRefreshFragment {
	ListView lView;
	String MESSAGES_START_FLAG = DiscoverContainerFragment.MESSAGES_START_FLAG;
	String MESSAGES_FINISH_FLAG = DiscoverContainerFragment.MESSAGES_FINISH_FLAG;
	TextView text;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// we need to refrence our container class. For now, we should just
		// return a text message
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.discover_needs_list_layout,
				container, false);

		swipeView = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
		swipeView.setColorScheme(android.R.color.holo_blue_dark,
				android.R.color.holo_blue_light,
				android.R.color.holo_green_light,
				android.R.color.holo_green_light);
		lView = (ListView) view.findViewById(R.id.list);

		swipeView.setOnRefreshListener(this);
		lView.setOnItemClickListener(this);

		return view;

	}

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
		super.helper = new NeedsDetailsRequest(this.getActivity());
	}

	@Override
	public CursorLoader generateCursorLoader() {
		CursorLoader cursorLoader = new CursorLoader(
				this.getActivity(),
				NearlingsContentProvider
						.contentURIbyTableName(NeedsDetailsDatabaseHelper.TABLE_NAME),
				NeedsDetailsDatabaseHelper.COLUMNS, null, null,
				NeedsDetailsDatabaseHelper.COLUMN_DATE + " DESC");

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
		inflater.inflate(R.menu.switch_to_map_view, menu);
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
}