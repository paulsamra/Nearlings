package swipe.android.nearlings;

import swipe.android.nearlings.sync.NearlingsSyncAdapter;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.edbert.library.sendRequest.SendRequestInterface;
import com.edbert.library.sendRequest.SendRequestStrategyManager;
import com.edbert.library.swipeToRefresh.SwipeToRefreshFragment;

public abstract class NearlingsSwipeToRefreshFragment extends
		SwipeToRefreshFragment implements Refreshable {
	protected SwipeRefreshLayout mEmptyViewContainer;
	// we use sourcehelpers
	protected SendRequestInterface helper;
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

		 mEmptyViewContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipe_empty);

		lView = (ListView) view.findViewById(R.id.list);

		 lView.setEmptyView(mEmptyViewContainer);
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
		
		
		swipeView.setOnRefreshListener(this);

mEmptyViewContainer.setOnRefreshListener(this);
		lView.setOnItemClickListener(this);

		return view;

	}
	@Override
	public void onRefresh() {
		String TAG = NearlingsSyncAdapter.HELPER_FLAG_ID;
		Bundle b = new Bundle();
		b.putString(TAG, SendRequestStrategyManager.generateTag(helper));
		requestSync(b);
	}
	public void onRefresh(Bundle b){
		String TAG = NearlingsSyncAdapter.HELPER_FLAG_ID;
		b.putString(TAG, SendRequestStrategyManager.generateTag(helper));
		requestSync(b);
	}
	public void requestSync(Bundle b) {
		//need to add the flags
		Log.e("NearlingsSwipeFragment", "Sync requested with bundle");
		b.putString(NearlingsSyncAdapter.SYNC_STARTED_FLAG_ID, syncStartedFlag());
		b.putString(NearlingsSyncAdapter.SYNC_FINISHED_FLAG_ID, syncFinishedFlag());
		
		((NearlingsApplication) getActivity().getApplication()).getSyncHelper()
				.performSync(b);
	}

	@Override
	public boolean isSyncing() {
		return ((NearlingsApplication) getActivity().getApplication())
				.getSyncHelper().isSyncing();
	}
	public SendRequestInterface getHelper(){
		return helper;
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
}