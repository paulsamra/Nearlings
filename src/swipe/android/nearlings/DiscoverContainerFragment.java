/**
 * Class is the container for a listview and mapview. Both listview and mapview represent
 * 2 views of the same action. 
 */
package swipe.android.nearlings;

import java.text.NumberFormat;
import java.util.ArrayList;

import swipe.android.nearlings.MessagesSync.NeedsDetailsRequest;
import swipe.android.nearlings.discover.options.SearchFilterCategoryOptionsListAdapter;
import swipe.android.nearlings.discover.options.SearchOptionsFilter;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.CursorLoader;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.edbert.library.sendRequest.SendRequestStrategyManager;
import com.meetme.android.horizontallistview.HorizontalListView;

public class DiscoverContainerFragment extends NearlingsSwipeToRefreshFragment {
	ListView lView;
	TextView searchTerm;
	Button needs_change_view;
	ImageView toggleFilter;
	public static final String MESSAGES_START_FLAG = DiscoverContainerFragment.class
			.getCanonicalName() + "_MESSAGES_START_FLAG";
	public static final String MESSAGES_FINISH_FLAG = DiscoverContainerFragment.class
			.getCanonicalName() + "_MESSAGES_FINISH_FLAG";

	
	public static final String[] radius = new String[] { ".5", "1.0", "2.0",
			"5.0" };
	public static final int[] unselectedIconsRadius = new int[] {
			R.drawable.half_mile_unselected, R.drawable.one_mile_unselected,
			R.drawable.two_mile_unselected, R.drawable.five_mile_unselected };
	int[] selectedIconsRadius = new int[] { R.drawable.half_mile_selected,
			R.drawable.one_mile_selected, R.drawable.two_mile_selected,
			R.drawable.five_mile_selected };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);
		// Log.e("CREATE VIEW", "CREATE");
		RelativeLayout rootView = (RelativeLayout) inflater.inflate(
				R.layout.discover_needs_container_layout, null);

		// searchTerm = (TextView)
		// rootView.findViewById(R.id.search_bar_search_term);
		// location = (TextView)
		// rootView.findViewById(R.id.search_bar_search_location);

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
			newFragment = new DiscoverMapViewFragment();
			needs_change_view.setText("List View");
		} else {
			// Create new fragment and transaction
			newFragment = new DiscoverListViewFragment();

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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final ViewGroup actionBarLayout = (ViewGroup) this.getActivity()
				.getLayoutInflater()
				.inflate(R.layout.actionbar_searchbar, null);
		final ActionBar actionBar = this.getActivity().getActionBar();

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
				((MainActivity) DiscoverContainerFragment.this.getActivity())
						.toggleDrawer();
			}

		});
		searchTerm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent myIntent = new Intent(DiscoverContainerFragment.this
						.getActivity(), SearchActivity.class);
				// myIntent.putExtra("key", value); //Optional parameters
				DiscoverContainerFragment.this.getActivity().startActivity(
						myIntent);
			}

		});

		toggleFilter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// toggle
				/*
				 * subbar_visible = !subbar_visible; if (subbar_visible) {
				 * listview.setVisibility(View.VISIBLE); } else {
				 * listview.setVisibility(View.GONE); }
				 */
				// create a popup filter
				generatePopup();
			}

		});
	}

	@Override
	public void onResume() {
		super.onResume();

		// grab new search terms
		updateSearchString();

		String locationString = SessionManager.getInstance(this.getActivity())
				.getSearchLocation();

		if (locationString != null && locationString != "") {
			// location.setText(locationString);
		} else {
			// location.setText("Unknown Location");

		}
		if (isMapView) {
			needs_change_view.setText("List View");
		} else {
			needs_change_view.setText("Map View");
		}

		requestUpdate();
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
		super.helper = SendRequestStrategyManager
				.getHelper(NeedsDetailsRequest.class);
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

	@Override
	public void onStop() {
		super.onStop();
		final ActionBar actionBar = this.getActivity().getActionBar();

		getActivity().getActionBar().setDisplayShowCustomEnabled(false);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
	}

	@Override
	public void onStart() {
		super.onStart();
		final ActionBar actionBar = this.getActivity().getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);

	}

	private void setUpFilters(View rootView) {
		final HorizontalListView listview = (HorizontalListView) rootView
				.findViewById(R.id.search_options_listview_categories);
		final ArrayList<SearchOptionsFilter> listOfFilter = new ArrayList();
		Resources res = getResources();
		TypedArray icons = res.obtainTypedArray(R.array.needs_types_unchecked);
		String[] terms = res.getStringArray(R.array.needs_types);
		//Drawable drawable = icons.getDrawable(0);

		for (int i = 0; i < icons.length(); i++) {
		int resource = icons.getResourceId(i, 0);
			listOfFilter.add(new SearchOptionsFilter(false,
					resource, terms[i]));
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
				String term = listOfFilter.get(position).getSearchTerm();
				if (current) {
					term = "All";
				}
				// searchTerm.setText(term);
				adapter.notifyDataSetChanged();
				// requeue with search filters. We should pass in filters at
				// some point
				// DiscoverContainerFragment.this.onRefresh();
				SessionManager.getInstance(
						DiscoverContainerFragment.this.getActivity())
						.setSearchString(term);
			}
		});

		for (SearchOptionsFilter f : listOfFilter) {
			if (f.getSearchTerm().equals(
					SessionManager.getInstance(this.getActivity())
							.getSearchString())) {
				f.setSelected(true);
			}
		}
	}

	private void setUpRadius(View rootView) {
		final HorizontalListView listview = (HorizontalListView) rootView
				.findViewById(R.id.radius_selection);
		final ArrayList<SearchOptionsFilter> listOfFilter = new ArrayList();
		for (int i = 0; i < radius.length; i++) {
			listOfFilter
					.add(new SearchOptionsFilter(false,
							unselectedIconsRadius[i], selectedIconsRadius[i],
							radius[i]));
		}

		final SearchFilterCategoryOptionsListAdapter adapter = new SearchFilterCategoryOptionsListAdapter(
				this.getActivity(),
				R.layout.search_options_view_item_unsquared, listOfFilter);

		listview.setAdapter(adapter);

		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				boolean current = listOfFilter.get(position).isSelected();
				for (SearchOptionsFilter f : listOfFilter)
					f.setSelected(false);

				listOfFilter.get(position).setSelected(!current);
				String term = listOfFilter.get(position).getSearchTerm();
				if (current) {

					term = "-1";
				}
				// searchTerm.setText(term);
				adapter.notifyDataSetChanged();
				// requeue with search filters. We should pass in filters at
				// some point
				SessionManager.getInstance(
						DiscoverContainerFragment.this.getActivity())
						.setSearchRadius(Float.valueOf(term));
			}
		});

		for (SearchOptionsFilter f : listOfFilter) {
			if (f.getSearchTerm().equals(
					String.valueOf(SessionManager.getInstance(
							this.getActivity()).getSearchRadius()))) {
				f.setSelected(true);
			}
		}
	}

	private void setUpStatus(View rootView) {
		final Button b = (Button) rootView.findViewById(R.id.status_button);
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				final String[] items = getResources().getStringArray(
						R.array.needs_statuses);
				AlertDialog.Builder builder = new AlertDialog.Builder(
						DiscoverContainerFragment.this.getActivity());

				builder.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						SessionManager.getInstance(
								DiscoverContainerFragment.this.getActivity())
								.setSearchStatus(items[item]);
						b.setText(items[item]);
						dialog.cancel();
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
			}

		});
		String searchStatus = SessionManager.getInstance(
				DiscoverContainerFragment.this.getActivity()).getSearchStatus();
		if (!searchStatus.equals(SessionManager.SEARCH_DEFAULT_FILTER))
			b.setText(searchStatus);
	}

	private void setUpMinimumReward(View rootView) {
		final EditText et = (EditText) rootView
				.findViewById(R.id.reward_minimum);
		et.addTextChangedListener(new TextWatcher() {
			private String current = "";

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (!s.toString().equals(current)) {
					et.removeTextChangedListener(this);

					String cleanString = s.toString().replaceAll("[$,.]", "");

					double parsed = Double.parseDouble(cleanString);
					SessionManager.getInstance(
							DiscoverContainerFragment.this.getActivity())
							.setSearchRewardMinimum((float) parsed);
					String formatted = NumberFormat.getCurrencyInstance()
							.format((parsed / 100));

					current = formatted;
					et.setText(formatted);
					et.setSelection(formatted.length());

					et.addTextChangedListener(this);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		float reward = SessionManager.getInstance(
				DiscoverContainerFragment.this.getActivity())
				.getSearchRewardMinimum();
		if (reward != SessionManager.SEARCH_DEFAULT_NUMERIC) {
			double parsed = (double) reward;
			String formatted = NumberFormat.getCurrencyInstance().format(
					(parsed / 100));

			et.setText(formatted);
			et.setSelection(formatted.length());
		}

	}

	public void generatePopup() {
		// custom dialog

		// setup
		AlertDialog.Builder builder = new AlertDialog.Builder(
				this.getActivity());
		TextView title = new TextView(this.getActivity());

		title.setPadding(10, 10, 10, 10);
		title.setGravity(Gravity.CENTER);
		title.setTextSize(40);

		title.setTextSize(30.0f);
		title.setText("Filters");

		builder.setCustomTitle(title)
				.setCancelable(true)
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						})
				.setPositiveButton("Search",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
								SessionManager.getInstance(DiscoverContainerFragment.this.getActivity()).commitPendingChanges();
								DiscoverContainerFragment.this.updateSearchString();
								
								requestUpdate();
							}
						});

		LayoutInflater inflater = this.getActivity().getLayoutInflater();
		View dialoglayout = inflater.inflate(R.layout.search_popup, null);

		setUpFilters(dialoglayout);
		setUpStatus(dialoglayout);
		setUpRadius(dialoglayout);
		setUpMinimumReward(dialoglayout);

		// initialize default items

		builder.setView(dialoglayout);
		builder.show();
	}

	public void requestUpdate() {
		Bundle b = new Bundle();
		SessionManager sm = SessionManager.getInstance(this.getActivity());
		Location currentLocation = ((NearlingsApplication) this.getActivity()
				.getApplication()).getLastLocation();

		if (sm.getSearchLocation() != "") {
			b.putString(NeedsDetailsRequest.BUNDLE_LOCATION,
					sm.getSearchLocation());
			b.putString(NeedsDetailsRequest.BUNDLE_LOCATION_TYPE,
					NeedsDetailsRequest.BUNDLE_LOCATION_TYPE_ADDRESS);
			b.putFloat(NeedsDetailsRequest.BUNDLE_RADIUS, 20.0f);
		}/*
		 * else if( currentLocation != null){ String currentLocationLatLng =
		 * currentLocation.getLatitude() + ","+currentLocation.getLongitude();
		 * b.putString(NeedsDetailsRequest.BUNDLE_LOCATION,
		 * currentLocationLatLng);
		 * b.putString(NeedsDetailsRequest.BUNDLE_LOCATION_TYPE,
		 * NeedsDetailsRequest.BUNDLE_LOCATION_TYPE_LATITUDE );
		 * b.putFloat(NeedsDetailsRequest.BUNDLE_RADIUS, 20.0f ); }
		 */

		if (sm.getSearchRewardMinimum() != -1) {

			b.putFloat(NeedsDetailsRequest.BUNDLE_REWARD,
					sm.getSearchRewardMinimum());
		}

		if (sm.getSearchString() != ""
				&& !sm.getSearchString().equals(
						SessionManager.SEARCH_DEFAULT_FILTER)) {
			b.putString(NeedsDetailsRequest.BUNDLE_KEYWORDS,
					sm.getSearchString());
		}

		super.onRefresh(b);
	}
	public void updateSearchString(){
		// grab new search terms
		String searchString = SessionManager.getInstance(this.getActivity())
				.getSearchString();

		if (searchString != null && searchString != "") {
			searchTerm.setText(searchString);
		} else {
			searchTerm.setText("All");
		}
	}
}