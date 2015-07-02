/**
 * Class is the container for a listview and mapview. Both listview and mapview represent
 * 2 views of the same action. 
 */
package swipe.android.nearlings.groups;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import swipe.android.DatabaseHelpers.GroupsDatabaseHelper;
import swipe.android.DatabaseHelpers.NeedsDetailsDatabaseHelper;
import swipe.android.nearlings.BaseContainerFragment;
import swipe.android.nearlings.NearlingsApplication;
import swipe.android.nearlings.NearlingsContentProvider;
import swipe.android.nearlings.R;
import swipe.android.nearlings.SessionManager;
import swipe.android.nearlings.MessagesSync.EventsDetailsRequest;
import swipe.android.nearlings.MessagesSync.GroupsRequest;
import swipe.android.nearlings.discover.options.SearchFilterCategoryOptionsListAdapter;
import swipe.android.nearlings.discover.options.SearchOptionsFilter;
import swipe.android.nearlings.json.groups.GroupsMapViewFragment;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.edbert.library.sendRequest.SendRequestStrategyManager;
import com.meetme.android.horizontallistview.HorizontalListView;

public class GroupsContainerFragment extends BaseContainerFragment {
	public static final String MESSAGES_START_FLAG = GroupsContainerFragment.class
			.getCanonicalName() + "_MESSAGES_START_FLAG";
	public static final String MESSAGES_FINISH_FLAG = GroupsContainerFragment.class
			.getCanonicalName() + "_MESSAGES_FINISH_FLAG";

	// setup filters! Begin area where we customize. All these functions are for
	// dynamically adding behavior to the
	// filter popup.
	@Override
	protected int setNumElements() {
		String allActiveSearch = "";
		String[] activeStates = null;
		CursorLoader cursorLoader = new CursorLoader(
				this.getActivity(),
				NearlingsContentProvider
						.contentURIbyTableName(GroupsDatabaseHelper.TABLE_NAME),
				GroupsDatabaseHelper.COLUMNS, allActiveSearch,
				activeStates, GroupsDatabaseHelper.COLUMN_DATE + " DESC");
		return cursorLoader.loadInBackground().getCount();
	}

	final ArrayList<SearchOptionsFilter> listOfGroupCategoryFilter = new ArrayList();
	int groupCategoryPosition = -1;

	private void setUpFilters(View rootView) {
		final HorizontalListView listview = (HorizontalListView) rootView
				.findViewById(R.id.search_options_listview_categories);
		Resources res = getResources();
		TypedArray icons = res
				.obtainTypedArray(R.array.group_category_types_unchecked);
		String[] terms = res.getStringArray(R.array.group_category_types);
		// Drawable drawable = icons.getDrawable(0);
		listOfGroupCategoryFilter.clear();
		for (int i = 0; i < icons.length(); i++) {
			int resource = icons.getResourceId(i, 0);
			listOfGroupCategoryFilter.add(new SearchOptionsFilter(false,
					resource, terms[i]));
		}

		final SearchFilterCategoryOptionsListAdapter adapter = new SearchFilterCategoryOptionsListAdapter(
				this.getActivity(), R.layout.search_options_view_item,
				listOfGroupCategoryFilter);

		listview.setAdapter(adapter);

		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				boolean current = listOfGroupCategoryFilter.get(position)
						.isSelected();
				for (SearchOptionsFilter f : listOfGroupCategoryFilter)
					f.setSelected(false);

				listOfGroupCategoryFilter.get(position).setSelected(!current);
				/*
				 * String term = listOfFilter.get(position).getSearchTerm(); if
				 * (current) { term = "All"; SessionManager.getInstance(
				 * GroupsContainerFragment.this.getActivity())
				 * .setGroupCategory(SessionManager.DEFAULT_STRING); } else {
				 * SessionManager.getInstance(
				 * GroupsContainerFragment.this.getActivity())
				 * .setGroupCategory(term); }
				 */
				// searchTerm.setText(term);
				groupCategoryPosition = position;
				adapter.notifyDataSetChanged();
				// requeue with search filters. We should pass in filters at
				// some point
				// DiscoverContainerFragment.this.onRefresh();
			}
		});

		for (SearchOptionsFilter f : listOfGroupCategoryFilter) {
			if (f.getSearchTerm().equals(
					SessionManager.getInstance(this.getActivity())
							.getGroupCategory())) {
				f.setSelected(true);
			}
		}
	}

	final ArrayList<SearchOptionsFilter> listOfRadiusFilter = new ArrayList();
	int radiusPosition = -1;

	private void setUpRadius(View rootView) {
		final HorizontalListView listview = (HorizontalListView) rootView
				.findViewById(R.id.radius_selection);
		Resources res = getResources();
		TypedArray selectedIcons = res
				.obtainTypedArray(R.array.radius_selected_icons);
		TypedArray unselectedIcons = res
				.obtainTypedArray(R.array.radius_unselected_icons);
		listOfRadiusFilter.clear();
		String[] radius = res.getStringArray(R.array.radius);
		for (int i = 0; i < radius.length; i++) {

			int selectedResource = selectedIcons.getResourceId(i, 0);
			int unselectedResource = unselectedIcons.getResourceId(i, 0);

			listOfRadiusFilter.add(new SearchOptionsFilter(false,
					unselectedResource, selectedResource, radius[i]));
		}

		final SearchFilterCategoryOptionsListAdapter adapter = new SearchFilterCategoryOptionsListAdapter(
				this.getActivity(),
				R.layout.search_options_view_item_unsquared, listOfRadiusFilter);

		listview.setAdapter(adapter);

		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				boolean current = listOfRadiusFilter.get(position).isSelected();
				for (SearchOptionsFilter f : listOfRadiusFilter)
					f.setSelected(false);

				listOfRadiusFilter.get(position).setSelected(!current);
				radiusPosition = position;
				/*
				 * String term =
				 * listOfRadiusFilter.get(position).getSearchTerm(); if
				 * (current) { term =
				 * String.valueOf(SessionManager.DEFAULT_VALUE); } //
				 * searchTerm.setText(term); adapter.notifyDataSetChanged(); //
				 * requeue with search filters. We should pass in filters at //
				 * some point SessionManager.getInstance(
				 * GroupsContainerFragment.this.getActivity())
				 * .setSearchRadius(Float.valueOf(term));
				 */
				adapter.notifyDataSetChanged();
			}
		});

		for (SearchOptionsFilter f : listOfRadiusFilter) {
			if (f.getSearchTerm().equals(
					String.valueOf(SessionManager.getInstance(
							this.getActivity()).getSearchRadius()))) {
				f.setSelected(true);
			}
		}
	}

	int privacyPosition = -1;
	String[] privacyItems, privacyItemsView;

	// privacy
	private void setUpStatus(View rootView) {
		final Button b = (Button) rootView
				.findViewById(R.id.private_public_btn);
		privacyItems = getResources().getStringArray(R.array.event_privacy);
		privacyItemsView = getResources().getStringArray(
				R.array.event_privacy_view);

		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				AlertDialog.Builder builder = new AlertDialog.Builder(
						GroupsContainerFragment.this.getActivity());

				builder.setItems(privacyItemsView,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int item) {
								// change this
								/*
								 * SessionManager.getInstance(
								 * GroupsContainerFragment.this.getActivity())
								 * .setSearchString(privacyItems[item]);
								 */
								b.setText(privacyItemsView[item]);
								dialog.cancel();
							}
						});
				AlertDialog alert = builder.create();
				alert.show();
			}

		});
		String searchStatus = SessionManager.getInstance(
				GroupsContainerFragment.this.getActivity())
				.getSearchVisibility();
		int i = 0;

		b.setText(privacyItemsView[0]);
		for (; i < privacyItems.length; i++) {
			if (searchStatus != null && privacyItems[i].equals(searchStatus)) {
				b.setText(privacyItemsView[i]);
			}
		}
		
	}

	public void generatePopup() {
		// custom dialog

		// setup
		AlertDialog.Builder builder = new AlertDialog.Builder(super.context);
		TextView title = new TextView(super.context);

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
								/*
								 * SessionManager.getInstance(
								 * GroupsContainerFragment.this .getActivity())
								 * .commitPendingChanges();
								 * GroupsContainerFragment.this
								 * .updateSearchString(); SessionManager
								 * .getInstance( GroupsContainerFragment.this
								 * .getActivity()) .setSearchString(
								 * SessionManager .getInstance(
								 * GroupsContainerFragment.this .getActivity())
								 * .getExploreCategory());
								 * GroupsContainerFragment.this.searchTerm
								 * .setText(SessionManager.getInstance(
								 * GroupsContainerFragment.this .getActivity())
								 * .getSearchString());
								 */

								// set items from all our field
								// categories

								if (groupCategoryPosition != -1) {
									boolean categoryIsSelected = listOfGroupCategoryFilter
											.get(groupCategoryPosition)
											.isSelected();
									String categoryterm = SessionManager.DEFAULT_STRING;
									if (categoryIsSelected) {
										categoryterm = listOfGroupCategoryFilter
												.get(groupCategoryPosition)
												.getSearchTerm();

									}

									SessionManager.getInstance(
											GroupsContainerFragment.this
													.getActivity())
											.setGroupCategory(categoryterm);
								}

								// set private or public
								if (privacyPosition != -1) {

									SessionManager
											.getInstance(
													GroupsContainerFragment.this
															.getActivity())
											.setSearchVisibility(
													privacyItems[privacyPosition]);
								}
								// Log.d("Privacy Position",
								// String.valueOf(privacyPosition));
								// start_date, start_time;
								// start time
								// raidus
								if (radiusPosition != -1) {
									boolean radiusSelected = listOfRadiusFilter
											.get(radiusPosition).isSelected();
									float radius = SessionManager.DEFAULT_SEARCH_RADIUS;
									if (radiusSelected) {
										radius = Float
												.valueOf(listOfRadiusFilter
														.get(radiusPosition)
														.getSearchTerm());
									}
									SessionManager.getInstance(
											GroupsContainerFragment.this
													.getActivity())
											.setSearchRadius(radius);
								}

								// Log.d("Search Term", s);

								requestUpdate(is_reload_and_blank);
							}
						});

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View dialoglayout = inflater
				.inflate(R.layout.group_filters_popup, null);

		// filters are the categories
		setUpFilters(dialoglayout);
		setUpStatus(dialoglayout);
		setUpRadius(dialoglayout);

		builder.setView(dialoglayout);
		builder.show();
	}

	public void requestUpdate(int status) {
		/*
		 * Bundle b = new Bundle(); SessionManager sm =
		 * SessionManager.getInstance(this.getActivity()); Location
		 * currentLocation = ((NearlingsApplication) this.getActivity()
		 * .getApplication()).getLastLocation();
		 * 
		 * if (!sm.getSearchLocation().equals("")) {
		 * b.putString(NeedsExploreRequest.BUNDLE_LOCATION,
		 * sm.getSearchLocation());
		 * b.putString(NeedsExploreRequest.BUNDLE_LOCATION_TYPE,
		 * NeedsExploreRequest.BUNDLE_LOCATION_TYPE_ADDRESS);
		 * b.putFloat(NeedsExploreRequest.BUNDLE_RADIUS, 20.0f); }
		 */
		Bundle b = new Bundle();
		SessionManager sm = SessionManager.getInstance(this.getActivity());
		Location currentLocation = ((NearlingsApplication) this.getActivity()
				.getApplication()).getLastLocation();

		if (sm.getSearchLocation() != null
				&& !sm.getSearchLocation().equals("")) {
			String location_string = sm.getSearchLocation();
			String url_encode_location = location_string;
			try {
				url_encode_location = URLEncoder.encode(location_string,
						"UTF-8");

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} finally {
				b.putString(GroupsRequest.BUNDLE_LOCATION, url_encode_location);
			}

			b.putString(GroupsRequest.BUNDLE_LOCATION_TYPE,
					GroupsRequest.BUNDLE_LOCATION_TYPE_ADDRESS);

			float f = SessionManager.getInstance(this.getActivity())
					.getSearchRadius();
			b.putFloat(EventsDetailsRequest.BUNDLE_RADIUS, f);
		} else if (currentLocation != null) {
			b.putString(GroupsRequest.BUNDLE_LOCATION_TYPE,
					GroupsRequest.BUNDLE_LOCATION_TYPE_COORDINATES);
			b.putString(GroupsRequest.BUNDLE_LOCATION_LATITUDE,
					String.valueOf(currentLocation.getLatitude()));
			b.putString(GroupsRequest.BUNDLE_LOCATION_LONGITUDE,
					String.valueOf(currentLocation.getLongitude()));

			float f = SessionManager.getInstance(this.getActivity())
					.getSearchRadius();
			b.putFloat(EventsDetailsRequest.BUNDLE_RADIUS, f);
		}
		if (sm.getGroupCategory() != null
				&& !sm.getGroupCategory().equals(SessionManager.DEFAULT_VALUE)) {
			b.putString(GroupsRequest.BUNDLE_CATEGORY, sm.getGroupCategory());
		}
		if (sm.getSearchVisibility() != null
				&& !sm.getSearchVisibility().equals(
						SessionManager.DEFAULT_STRING)) {
			b.putString(GroupsRequest.BUNDLE_VISIBILITY,
					sm.getSearchVisibility());
		}

		if (sm.getSearchString() != null

		&& !sm.getSearchString().equals(SessionManager.DEFAULT_STRING)) {
			String s = sm.getSearchString();

			try {
				s = URLEncoder.encode(s, "UTF-8");

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} finally {
				b.putString(GroupsRequest.BUNDLE_KEYWORDS, s);
			}
		}
		if(status == is_reload_and_blank){
			b.putInt(super.STATUS, is_reload_and_blank);
			super.onRefresh(b, true);
		}else if(status == is_maintain) {
			b.putInt(super.STATUS, is_maintain);
			super.onRefresh(b, false);
		}else{
			b.putInt(super.STATUS, is_loadMore);
			super.onRefresh(b, false);
		}
	}

	@Override
	public Fragment mapViewFragment() {
		return new GroupsMapViewFragment();
	}

	@Override
	public Fragment listViewFragment() {
		return new GroupsListFragment();
	}

	@Override
	public void onResume() {
		super.onResume();
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
		helpers.add(SendRequestStrategyManager.getHelper(GroupsRequest.class));

	}
}