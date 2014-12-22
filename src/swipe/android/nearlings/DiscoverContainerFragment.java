/**
 * Class is the container for a listview and mapview. Both listview and mapview represent
 * 2 views of the same action. 
 */
package swipe.android.nearlings;

import java.util.ArrayList;

import com.edbert.library.sendRequest.SendRequestStrategyManager;
import com.meetme.android.horizontallistview.HorizontalListView;
import swipe.android.DatabaseHelpers.NeedsDetailsDatabaseHelper;
import swipe.android.nearlings.MessagesSync.MessagesRequest;
import swipe.android.nearlings.MessagesSync.NeedsDetailsRequest;
import swipe.android.nearlings.discover.options.SearchFilterCategoryOptionsListAdapter;
import swipe.android.nearlings.discover.options.SearchOptionsFilter;
import swipe.android.nearlings.discover.options.SearchRadiusFilter;
import swipe.android.nearlings.discover.options.SearchRadiusOptionsListAdapter;
import swipe.android.nearlings.jsonResponses.explore.JsonExploreResponse;
import swipe.android.nearlings.viewAdapters.MessagesViewAdapter;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class DiscoverContainerFragment extends NearlingsSwipeToRefreshFragment {
	ListView lView;
	TextView searchTerm;
	TextView location;
	public static final String MESSAGES_START_FLAG = DiscoverContainerFragment.class
			.getCanonicalName() + "_MESSAGES_START_FLAG";
	public static final String MESSAGES_FINISH_FLAG = DiscoverContainerFragment.class
			.getCanonicalName() + "_MESSAGES_FINISH_FLAG";

	public static final String[] terms = new String[] { "Shopping", "Coffee",
			"Dollar", "Meal", "School", "Computer", "Random" };
	public static final int[] unselectedIcons = new int[] {
			R.drawable.shopping_unselected, R.drawable.coffee_unselected,
			R.drawable.dollar_unselected, R.drawable.meal_unselected,
			R.drawable.school_unselected, R.drawable.computer_unselected,
			R.drawable.random_unselected };
	int[] selectedIcons = new int[] { R.drawable.shopping_selected,
			R.drawable.coffee_selected, R.drawable.dollar_selected,
			R.drawable.meal_selected, R.drawable.school_selected,
			R.drawable.computer_selected, R.drawable.random_selected };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);
		// Log.e("CREATE VIEW", "CREATE");
		LinearLayout rootView = (LinearLayout) inflater.inflate(
				R.layout.discover_needs_container_layout, null);

	
		searchTerm = (TextView) rootView.findViewById(R.id.search_bar_search_term);
		location = (TextView) rootView.findViewById(R.id.search_bar_search_location);
		final HorizontalListView listview = (HorizontalListView) rootView
				.findViewById(R.id.search_options_listview_categories);
		final ArrayList<SearchOptionsFilter> listOfFilter = new ArrayList();
		for (int i = 0; i < terms.length; i++) {
			listOfFilter.add(new SearchOptionsFilter(false, unselectedIcons[i],
					selectedIcons[i], terms[i]));
		}

		final SearchFilterCategoryOptionsListAdapter adapter = new SearchFilterCategoryOptionsListAdapter(
				this.getActivity(), R.layout.search_options_view_item,
				listOfFilter);

		listview.setAdapter(adapter);

		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				boolean current = listOfFilter.get(position).isSelected();
				for (SearchOptionsFilter f : listOfFilter)
					f.setSelected(false);
				listOfFilter.get(position).setSelected(!current);
				String term = listOfFilter.get(position)
						.getSearchTerm();
				if (current) {
					
					term = "All";
				}
				searchTerm.setText(term);
				adapter.notifyDataSetChanged();

				// requeue with search filters. We should pass in filters at
				// some point
				DiscoverContainerFragment.this.onRefresh();
			}
		});

		final Activity act = this.getActivity();
		View textbar = (View) rootView.findViewById(R.id.search_bar_text_items);
		textbar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(act, SearchActivity.class);
				// myIntent.putExtra("key", value); //Optional parameters
				act.startActivity(myIntent);
			}

		});
		ImageView filterResults = (ImageView) rootView
				.findViewById(R.id.search_options_filter_results);
		filterResults.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// toggle
				subbar_visible = !subbar_visible;
				if (subbar_visible) {
					listview.setVisibility(View.VISIBLE);
				} else {
					listview.setVisibility(View.GONE);
				}
			}

		});
		swapView();
		return rootView;

	}

	boolean subbar_visible = false;
	boolean isMapView = false;

	public void swapView() {
		Fragment newFragment;

		if (isMapView) {
			newFragment = new DiscoverMapViewFragment();
		} else {
			// Create new fragment and transaction
			newFragment = new DiscoverListViewFragment();
		}

		String backStateName = newFragment.getClass().getName();

		FragmentManager manager = getChildFragmentManager();
		boolean fragmentPopped = manager
				.popBackStackImmediate(backStateName, 0);

		if (!fragmentPopped) { // fragment not in back stack, create it.
			FragmentTransaction ft = manager.beginTransaction();
			ft.replace(R.id.needs_discover_fragment_view, newFragment);
			ft.addToBackStack(backStateName);
			ft.commit();
		}

	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onResume() {
		super.onResume();
	
		// grab new search terms
		String searchString = SessionManager.getInstance(this.getActivity())
				.getSearchString();
	
			if (searchString != null && searchString != ""){
				searchTerm.setText(searchString);
			}
			else{
				searchTerm.setText("All");
			}
		
		String locationString = SessionManager.getInstance(this.getActivity())
				.getSearchLocation();

			if (locationString != null && locationString != ""){
				location.setText(locationString);
			}
			else {
				location.setText("Unknown Location");
				
			}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.switch_to_map:
			isMapView = true;
			swapView();
			return true;
		case R.id.switch_to_list:
			isMapView = false;
			swapView();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void setSourceRequestHelper() {
		super.helper = SendRequestStrategyManager.getHelper(NeedsDetailsRequest.class);// new NeedsDetailsRequest(this.getActivity(), JsonExploreResponse.class);
	}

	@Override
	public CursorLoader generateCursorLoader() {

		return null;
	}

	@Override
	public void reloadData() {
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

	}

}