package swipe.android.nearlings.events;

import java.util.Calendar;

import swipe.android.DatabaseHelpers.EventsDatabaseHelper;
import swipe.android.DatabaseHelpers.MessagesDatabaseHelper;
import swipe.android.nearlings.CreateEventActivity;
import swipe.android.nearlings.NearlingsContentProvider;
import swipe.android.nearlings.NearlingsSwipeToRefreshFragment;
import swipe.android.nearlings.R;
import swipe.android.nearlings.MessagesSync.EventsRequest;
import swipe.android.nearlings.MessagesSync.MessagesRequest;
import swipe.android.nearlings.MessagesSync.NeedsCommentsRequest;
import swipe.android.nearlings.R.id;
import swipe.android.nearlings.R.layout;
import swipe.android.nearlings.viewAdapters.EventsViewAdapter;
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
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;

public class EventsListFragment extends NearlingsSwipeToRefreshFragment {
	SwipeListView lView;
	
	String MESSAGES_START_FLAG = EventsContainerFragment.MESSAGES_START_FLAG;
	String MESSAGES_FINISH_FLAG = EventsContainerFragment.MESSAGES_FINISH_FLAG;

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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(
				R.layout.pull_to_refresh_swipe_list_single_list, container,
				false);

		swipeView = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe);
	
		lView = (SwipeListView) rootView.findViewById(R.id.list);
		
		 mEmptyViewContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_empty);

			

			 lView.setEmptyView(mEmptyViewContainer);
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
					/*
					 * switch (item.getItemId()) { case R.id.menu_delete:
					 * lView.dismissSelected(); mode.finish(); return true;
					 * default: return false; }
					 */
					return false;
				}

				@Override
				public boolean onCreateActionMode(ActionMode mode, Menu menu) {
					/*
					 * MenuInflater inflater = mode.getMenuInflater();
					 * inflater.inflate(R.menu.menu_choice_items, menu);
					 */
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
		lView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
			        int visibleItemCount, int totalItemCount) {
			    boolean enable = false;
			    if(lView != null && lView.getChildCount() > 0){
			        // check if the first item of the list is visible
			        boolean firstItemVisible = lView.getFirstVisiblePosition() == 0;
			        // check if the top of the first item is visible
			        boolean topOfFirstItemVisible = lView.getChildAt(0).getTop() == 0;
			        // enabling or disabling the refresh layout
			        enable = firstItemVisible && topOfFirstItemVisible;
			    }
			    swipeView.setEnabled(enable);
			}});
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

mEmptyViewContainer.setOnRefreshListener(this);
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
		super.helper = new EventsRequest(this.getActivity());
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

		this.mAdapter = new EventsViewAdapter(this.getActivity(), c, lView);

		mAdapter.notifyDataSetChanged();
		lView.setAdapter(mAdapter);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Inflate the menu; this adds items to the action bar if it is present.
	//	inflater.inflate(R.menu.events_menu, menu);
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
		((EventsContainerFragment) this.getParentFragment()).requestUpdate();
	}
	

}