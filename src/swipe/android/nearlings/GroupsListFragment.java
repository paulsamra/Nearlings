/*package swipe.android.nearlings;

import java.util.Calendar;

import swipe.android.DatabaseHelpers.EventsDatabaseHelper;
import swipe.android.DatabaseHelpers.MessagesDatabaseHelper;
import swipe.android.nearlings.MessagesSync.EventsRequest;
import swipe.android.nearlings.MessagesSync.MessagesRequest;
import swipe.android.nearlings.MessagesSync.NeedsCommentsRequest;
import swipe.android.nearlings.viewAdapters.EventsViewAdapter;
import swipe.android.nearlings.viewAdapters.GroupsViewAdapter;
import swipe.android.nearlings.viewAdapters.MessagesViewAdapter;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;

public class GroupsListFragment extends NearlingsSwipeToRefreshFragment {
	SwipeListView lView;
	String MESSAGES_START_FLAG = this.getClass().getCanonicalName()
			+ "_MESSAGES_START_FLAG";
	String MESSAGES_FINISH_FLAG = this.getClass().getCanonicalName()
			+ "_MESSAGES_FINISH_FLAG";

	@Override
	public CursorLoader generateCursorLoader() {
		CursorLoader cursorLoader = new CursorLoader(
				this.getActivity(),
				NearlingsContentProvider
						.contentURIbyTableName(GroupsDatabaseHelper.TABLE_NAME),
						GroupsDatabaseHelper.COLUMNS, null, null,
						GroupsDatabaseHelper.COLUMN_TIME_OF_EVENT + " DESC");

		return cursorLoader;

	}

	@Override
	public void reloadData() {
		reloadAdapter();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(
				R.layout.pull_to_refresh_swipe_list_single_list, container,
				false);

		swipeView = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe);
		swipeView.setColorScheme(android.R.color.holo_blue_dark,
				android.R.color.holo_blue_light,
				android.R.color.holo_green_light,
				android.R.color.holo_green_light);

		lView = (SwipeListView) rootView.findViewById(R.id.graphical_list);
		swipeView.setEnabled(false);
		lView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			lView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

				@Override
				public void onItemCheckedStateChanged(ActionMode mode,
						int position, long id, boolean checked) {
					mode.setTitle("Selected (" + lView.getCountSelected() + ")");
				}

				@Override
				public boolean onActionItemClicked(ActionMode mode,
						MenuItem item) {
				
					return false;
				}

				@Override
				public boolean onCreateActionMode(ActionMode mode, Menu menu) {
					
					return true;
				}

				@Override
				public void onDestroyActionMode(ActionMode mode) {
					lView.unselectedChoiceStates();
				}

				@Override
				public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
					return false;
				}
			});
		}

		lView.setSwipeListViewListener(new BaseSwipeListViewListener() {
			@Override
			public void onOpened(int position, boolean toRight) {
			}

			@Override
			public void onClosed(int position, boolean fromRight) {
			}

			@Override
			public void onListChanged() {
			}

			@Override
			public void onMove(int position, float x) {
			}

			@Override
			public void onStartOpen(int position, int action, boolean right) {
				Log.d("swipe", String.format("onStartOpen %d - action %d",
						position, action));
			}

			@Override
			public void onStartClose(int position, boolean right) {
				Log.d("swipe", String.format("onStartClose %d", position));
			}

			@Override
			public void onClickFrontView(int position) {
				Log.d("swipe", String.format("onClickFrontView %d", position));
			}

			@Override
			public void onClickBackView(int position) {
				Log.d("swipe", String.format("onClickBackView %d", position));
			}

			@Override
			public void onDismiss(int[] reverseSortedPositions) {

			}

		});
		swipeView.setOnRefreshListener(this);
		// lView.setOnItemClickListener(this);
		lView.setSwipeOpenOnLongPress(true);
		lView.setSwipeCloseAllItemsWhenMoveList(true);

		// lView.setOffsetLeft(convertDpToPixel(100.0f));
		// lView.setOffsetRight(convertDpToPixel(100.0f));

		return rootView;
	}

	public int convertDpToPixel(float dp) {
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return (int) px;
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
		//super.helper = new GroupsRequest(this.getActivity());
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

		this.mAdapter = new GroupsViewAdapter(this.getActivity(), c, lView);

		mAdapter.notifyDataSetChanged();
		lView.setAdapter(mAdapter);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Inflate the menu; this adds items to the action bar if it is present.
		inflater.inflate(R.menu.events_menu, menu);
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
	
	
	

}*/