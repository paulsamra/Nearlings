package swipe.android.nearlings;

import java.util.ArrayList;

import swipe.android.nearlings.sync.NearlingsSyncAdapter;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.edbert.library.sendRequest.SendRequestInterface;
import com.edbert.library.sendRequest.SendRequestStrategyManager;
import com.edbert.library.swipeToRefresh.SwipeToRefreshFragment;
import com.edbert.library.utils.ListUtils;

public abstract class NearlingsSwipeToRefreshFragment extends
		SwipeToRefreshFragment implements Refreshable, OnScrollListener {
	protected SwipeRefreshLayout mEmptyViewContainer;
	// we use sourcehelpers
	protected ArrayList<SendRequestInterface> helpers = new ArrayList<SendRequestInterface>();
protected Cursor c;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setSourceRequestHelper();
	}

	@Override
	public void requestSync() {
		Log.e("NearlingsSwipeFragment", "Sync requested without bundle");
		((NearlingsApplication) getActivity().getApplication()).getSyncHelper()
				.performSync();
	}

	protected ListView lView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// we need to refrence our container class. For now, we should just
		// return a text message
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.discover_needs_list_layout,
				container, false);

		swipeView = (SwipeRefreshLayout) view.findViewById(R.id.swipe);

		mEmptyViewContainer = (SwipeRefreshLayout) view
				.findViewById(R.id.swipe_empty);

		lView = (ListView) view.findViewById(R.id.list);

		setUpFooter(inflater);
		lView.setEmptyView(mEmptyViewContainer);
		lView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				NearlingsSwipeToRefreshFragment.this.onScrollStateChanged(view, scrollState);
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
								 int visibleItemCount, int totalItemCount) {
				boolean enable = false;
				if (lView != null && lView.getChildCount() > 0) {
					// check if the first item of the list is visible
					boolean firstItemVisible = lView.getFirstVisiblePosition() == 0;
					// check if the top of the first item is visible
					boolean topOfFirstItemVisible = lView.getChildAt(0)
							.getTop() == 0;
					// enabling or disabling the refresh layout
					enable = firstItemVisible && topOfFirstItemVisible;
				}
				swipeView.setEnabled(enable);
			}
		});

		swipeView.setOnRefreshListener(this);

		mEmptyViewContainer.setOnRefreshListener(this);
		lView.setOnItemClickListener(this);

	//	lView.setOnScrollListener(this);
		return view;

	}

	@Override
	public void onRefresh() {
		footerView.setVisibility(View.VISIBLE);
		String TAG = NearlingsSyncAdapter.HELPER_FLAG_ID;
		Bundle b = new Bundle();

		ArrayList<String> arr = SendRequestStrategyManager.generateTag(helpers);
		// b = NearlingsSyncAdapter.addArrayListOfStrings(b, arr);
		String helpers = ListUtils.listToString(arr);
		b.putString(TAG, helpers);

		b.putBoolean(NearlingsSyncAdapter.CLEAR_DB, true);
		requestSync(b);
	}

	public void onRefresh(Bundle b, boolean clear) {
		footerView.setVisibility(View.VISIBLE);
		String TAG = NearlingsSyncAdapter.HELPER_FLAG_ID;
		// b.putStringArray(TAG, value)
		// b.putStringArrayList(TAG,
		// SendRequestStrategyManager.generateTag(helpers));
		Bundle internal = b;
if (internal == null){
	internal = new Bundle();
}
		ArrayList<String> arr = SendRequestStrategyManager.generateTag(helpers);

		// b = NearlingsSyncAdapter.addArrayListOfStrings(b, arr);
		String helpers = ListUtils.listToString(arr);
		internal.putString(TAG, helpers);

		if(clear)
			internal.putBoolean(NearlingsSyncAdapter.CLEAR_DB, true);
		requestSync(internal);
	}

	public void requestSync(Bundle b) {
		// need to add the flags
		Log.e("NearlingsSwipeFragment", "Sync requested with bundle");
		b.putString(NearlingsSyncAdapter.SYNC_STARTED_FLAG_ID,
				syncStartedFlag());
		b.putString(NearlingsSyncAdapter.SYNC_FINISHED_FLAG_ID,
				syncFinishedFlag());

		((NearlingsApplication) getActivity().getApplication()).getSyncHelper()
				.performSync(b);
	}

	@Override
	public boolean isSyncing() {
		return ((NearlingsApplication) getActivity().getApplication())
				.getSyncHelper().isSyncing();
	}

	public ArrayList<SendRequestInterface> getHelpers() {
		return helpers;
	}

	public abstract void setSourceRequestHelper();

	protected void updateRefresh(boolean isSyncing) {
		super.updateRefresh(isSyncing);
		if (mEmptyViewContainer == null) {
			return;
		}
		if (!isSyncing) {
			mEmptyViewContainer.setRefreshing(false);
		} else {
			mEmptyViewContainer.setRefreshing(true);
		}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Cursor c = generateCursor();
		if(c.getCount() == 0){
			return;
		}
		return;
	}
	@Override
	protected void onReceiveFinish(Context context, Intent intent){

		updateRefresh(false);
		if(intent.getExtras() != null){

		boolean b= intent.getExtras().getBoolean(NearlingsSyncAdapter.SESSION_IS_BAD, false);
		if(b){
			((NearlingsApplication) this
					.getActivity().getApplication()).logoutDialog(this.getActivity());

			return;
		}

		}
int numOflistElements = setNumElements();
		if (numOflistElements == previousAmount || numOflistElements == 0 || numOflistElements - previousAmount < SessionManager.SEARCH_LIMIT){
			footerView.setVisibility(View.GONE);
			return;
		}
	}
	protected abstract int setNumElements();
	protected View footerView;

protected void setUpFooter(LayoutInflater inflater) {
	// put footer before adapter?
	footerView = inflater.inflate(R.layout.load_more_footer, null, false);
	lView.addFooterView(footerView);
	// lView.setVisibility(View.GONE);
}
	/**
	 * Method to detect scroll on listview
	 */
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
						 int visibleItemCount, int totalItemCount) {
		// Leave this empty
	}

	/**
	 * Method to detect if scrolled to end of listview
	 */
	@Override
	public void onScrollStateChanged(AbsListView listView, int scrollState) {
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
			if (listView.getLastVisiblePosition() >= listView.getCount() - 1 - threshold) {
				currentPage++;
				// Load more articles
				loadMoreArticles(currentPage);
			}
		}
	}
protected int currentPage = 0;
	int threshold = SessionManager.SEARCH_LIMIT;
	protected int previousAmount = -1;
	/**
	 * Method to load more articles if scrolled to end of listview
	 */
	protected void loadMoreArticles(int currentPage) {
		Log.d("Load more"," load more");
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
		onRefresh(data, false);
	}
}