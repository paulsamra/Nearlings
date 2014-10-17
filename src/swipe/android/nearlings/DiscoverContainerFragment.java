package swipe.android.nearlings;

import java.util.ArrayList;
import com.meetme.android.horizontallistview.HorizontalListView;
import swipe.android.DatabaseHelpers.NeedsDetailsDatabaseHelper;
import swipe.android.nearlings.MessagesSync.MessagesRequest;
import swipe.android.nearlings.MessagesSync.NeedsDetailsRequest;
import swipe.android.nearlings.discover.options.SearchFilterCategoryOptionsListAdapter;
import swipe.android.nearlings.discover.options.SearchOptionsFilter;
import swipe.android.nearlings.discover.options.SearchRadiusFilter;
import swipe.android.nearlings.discover.options.SearchRadiusOptionsListAdapter;
import swipe.android.nearlings.viewAdapters.MessagesViewAdapter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class DiscoverContainerFragment extends NearlingsSwipeToRefreshFragment {
	ListView lView;
	String MESSAGES_START_FLAG = DiscoverContainerFragment.class
			.getCanonicalName() + "_MESSAGES_START_FLAG";
	String MESSAGES_FINISH_FLAG = DiscoverContainerFragment.class
			.getCanonicalName() + "_MESSAGES_FINISH_FLAG";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);

		LinearLayout rootView = (LinearLayout) inflater.inflate(
				R.layout.discover_needs_container_layout, null);

		final HorizontalListView listview = (HorizontalListView) rootView
				.findViewById(R.id.search_options_listview_categories);
		ArrayList<SearchOptionsFilter> listOfFilter = new ArrayList();
		listOfFilter.add(new SearchOptionsFilter(false,
				R.drawable.filter_unselected, R.drawable.filter_selected));
		listOfFilter.add(new SearchOptionsFilter(false,
				R.drawable.filter_unselected, R.drawable.filter_selected));
		listOfFilter.add(new SearchOptionsFilter(false,
				R.drawable.filter_unselected, R.drawable.filter_selected));
		listOfFilter.add(new SearchOptionsFilter(false,
				R.drawable.filter_unselected, R.drawable.filter_selected));
		listOfFilter.add(new SearchOptionsFilter(false,
				R.drawable.filter_unselected, R.drawable.filter_selected));
		listOfFilter.add(new SearchOptionsFilter(false,
				R.drawable.filter_unselected, R.drawable.filter_selected));
		listOfFilter.add(new SearchOptionsFilter(false,
				R.drawable.filter_unselected, R.drawable.filter_selected));

		final SearchFilterCategoryOptionsListAdapter adapter = new SearchFilterCategoryOptionsListAdapter(
				this.getActivity(), R.layout.search_options_view_item,
				listOfFilter);

		// Assign adapter to HorizontalListView
		listview.setAdapter(adapter);

		Fragment newFragment;
		if (isMapView) {
			newFragment = new DiscoverListViewFragment();
		} else {
			// Create new fragment and transaction
			newFragment = new DiscoverListViewFragment();
		}
		FragmentTransaction transaction = getChildFragmentManager()
				.beginTransaction();

		transaction.addToBackStack(null);
		transaction.add(R.id.needs_discover_fragment_view, newFragment);

		transaction.commit();

		
		TextView filterResults = (TextView) rootView.findViewById(R.id.search_options_filterresults);
		filterResults.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
			//toggle
				subbar_visible = !subbar_visible;
				if(subbar_visible){
					listview.setVisibility(View.VISIBLE);
				}else{
					listview.setVisibility(View.GONE);
				}
			}
			
		});
		return rootView;

	}
boolean subbar_visible = false;
	boolean isMapView = false;
	
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
		super.helper = new NeedsDetailsRequest();
	}

	@Override
	public void onResume() {
		super.onResume();
		reloadData();
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

	@Override
	public void reloadData() {

		/*getLoaderManager().initLoader(0, null, this);

		Cursor c = generateCursor();

		this.mAdapter = new MessagesViewAdapter(this.getActivity(), c);

		mAdapter.notifyDataSetChanged();
		lView.setAdapter(mAdapter);*/

	}
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
  
	}

}