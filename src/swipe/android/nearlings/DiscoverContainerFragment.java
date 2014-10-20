/**
 * Class is the container for a listview and mapview. Both listview and mapview represent
 * 2 views of the same action. 
 */
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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class DiscoverContainerFragment extends Fragment {
	ListView lView;

	/*String MESSAGES_START_FLAG = DiscoverContainerFragment.class
			.getCanonicalName() + "_MESSAGES_START_FLAG";
	String MESSAGES_FINISH_FLAG = DiscoverContainerFragment.class
			.getCanonicalName() + "_MESSAGES_FINISH_FLAG";*/
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

		TextView filterResults = (TextView) rootView
				.findViewById(R.id.search_options_filterresults);
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
		  boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

		  if (!fragmentPopped){ //fragment not in back stack, create it.
		    FragmentTransaction ft = manager.beginTransaction();
		    ft.replace(R.id.needs_discover_fragment_view, newFragment);
		    ft.addToBackStack(backStateName);
		    ft.commit();
		  }
		  
		/*FragmentTransaction transaction = getChildFragmentManager()
				.beginTransaction();

		transaction.addToBackStack(null);
		transaction.replace(R.id.needs_discover_fragment_view, newFragment);*/

		//transaction.commit();
	}

	/*	@Override
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
//leave as null since we arent actually displaying data.
	}*/

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

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

}