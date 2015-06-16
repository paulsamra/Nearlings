//Base container for a fragment when we want to search

//flexible components are the container. However, this container is just for searchbar
/**
 * Class is the container for a listview and mapview. Both listview and mapview represent
 * 2 views of the same action. 
 */
package swipe.android.nearlings;

import swipe.android.nearlings.MessagesSync.NearlingsSyncHelper;
import android.app.ActionBar;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public abstract class BaseContainerFragment extends
		NearlingsSwipeToRefreshFragment {
	 protected Context context;
	protected ListView lView;
	protected TextView searchTerm;
	protected Button needs_change_view;
	protected ImageView toggleFilter;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);
		// Log.e("CREATE VIEW", "CREATE");
		context=getActivity();
		RelativeLayout rootView = (RelativeLayout) inflater.inflate(
				R.layout.discover_needs_container_layout, null);

		needs_change_view = (Button) rootView
				.findViewById(R.id.needs_change_view);
		needs_change_view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isMapView = !isMapView;
				swapView();
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
			newFragment = mapViewFragment();// new DiscoverMapViewFragment();
			needs_change_view.setText("List View");
		} else {
			// Create new fragment and transaction
			newFragment = listViewFragment();// new DiscoverListViewFragment();

			needs_change_view.setText("Map View");
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

	public abstract Fragment mapViewFragment();

	public abstract Fragment listViewFragment();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final ViewGroup actionBarLayout = (ViewGroup) this.getActivity()
				.getLayoutInflater()
				.inflate(R.layout.actionbar_searchbar, null);
		final android.support.v7.app.ActionBar actionBar = ((ActionBarActivity)this.getActivity()).getSupportActionBar();

		// Set up your ActionBar
		actionBar.setCustomView(actionBarLayout);
		searchTerm = (TextView) actionBarLayout.findViewById(R.id.search_text);
		// setHasOptionsMenu(true);
		toggleFilter = (ImageView) actionBarLayout
				.findViewById(R.id.search_bar_filter);
		ImageButton toggleHome = (ImageButton) actionBarLayout
				.findViewById(R.id.search_actionbar_home);
		toggleHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((MainActivity) context)
						.toggleDrawer();
			}

		});
		searchTerm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent myIntent = new Intent(BaseContainerFragment.this
						.getActivity(), SearchActivity.class);
				// myIntent.putExtra("key", value); //Optional parameters
				BaseContainerFragment.this.getActivity()
						.startActivity(myIntent);
			}

		});

		toggleFilter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				generatePopup();
			}

		});

	}

	public abstract void generatePopup();

	@Override
	public void onResume() {
		super.onResume();

		if (isMapView) {
			needs_change_view.setText("List View");
		} else {
			needs_change_view.setText("Map View");
		}

		updateSearchString();

		requestUpdate();

	}

	public abstract void requestUpdate();

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

	public abstract void setSourceRequestHelper();/* {
		super.helper = SendRequestStrategyManager
				.getHelper(NeedsDetailsRequest.class);
	}*/

	@Override
	public CursorLoader generateCursorLoader() {

		return null;
	}

	@Override
	public void reloadData() {
		// TODO Auto-generated method stub

	}


	public abstract String syncStartedFlag();

	public abstract String syncFinishedFlag();

	@Override
	public void reloadAdapter() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStop() {
		NearlingsApplication nap = (NearlingsApplication) this
				.getActivity().getApplicationContext();
		NearlingsSyncHelper nsh = nap.getSyncHelper();

		ContentResolver.cancelSync(nsh.getAccount(), nsh.getAuthority());

		super.onStop();
		final android.support.v7.app.ActionBar actionBar = ((ActionBarActivity)this.getActivity()).getSupportActionBar();

		actionBar.setDisplayShowCustomEnabled(false);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		
		
	}

	@Override
	public void onStart() {
		super.onStart();
		final android.support.v7.app.ActionBar actionBar = ((ActionBarActivity)this.getActivity()).getSupportActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);

	}

	public void updateSearchString() {
		// grab new search terms
		String searchString = SessionManager.getInstance(this.getActivity())
				.getSearchString();

		if (!searchString.equals(null) && !searchString.equals(SessionManager.DEFAULT_STRING)) {
			searchTerm.setText(searchString);
		} else {
			searchTerm.setText("All");
		}
	}
}