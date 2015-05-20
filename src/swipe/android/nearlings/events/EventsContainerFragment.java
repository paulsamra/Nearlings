/**
 * Class is the container for a listview and mapview. Both listview and mapview represent
 * 2 views of the same action. 
 */
package swipe.android.nearlings.events;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;

import swipe.android.nearlings.BaseContainerFragment;
import swipe.android.nearlings.FieldsParsingUtils;
import swipe.android.nearlings.NearlingsApplication;
import swipe.android.nearlings.R;
import swipe.android.nearlings.SessionManager;
import swipe.android.nearlings.MessagesSync.EventsDetailsRequest;
import swipe.android.nearlings.MessagesSync.GroupsRequest;
import swipe.android.nearlings.MessagesSync.NeedsExploreRequest;
import swipe.android.nearlings.discover.options.SearchFilterCategoryOptionsListAdapter;
import swipe.android.nearlings.discover.options.SearchOptionsFilter;
import swipe.android.nearlings.groups.GroupsContainerFragment;
import swipe.android.nearlings.needs.DiscoverContainerFragment;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.edbert.library.sendRequest.SendRequestStrategyManager;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.meetme.android.horizontallistview.HorizontalListView;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

public class EventsContainerFragment extends BaseContainerFragment implements
		OnDateSetListener, TimePickerDialog.OnTimeSetListener {
	public static final String MESSAGES_START_FLAG = EventsContainerFragment.class
			.getCanonicalName() + "_MESSAGES_START_FLAG";
	public static final String MESSAGES_FINISH_FLAG = EventsContainerFragment.class
			.getCanonicalName() + "_MESSAGES_FINISH_FLAG";

	int selected_category = -1;
	// categories
	final ArrayList<SearchOptionsFilter> listOfCategoryFilter = new ArrayList();

	private void setUpFilters(View rootView) {

		final HorizontalListView listview = (HorizontalListView) rootView
				.findViewById(R.id.search_options_listview_categories);

		Resources res = getResources();
		TypedArray icons = res.obtainTypedArray(R.array.event_types_unchecked);
		String[] categoryTerms = res.getStringArray(R.array.event_types);
		// Drawable drawable = icons.getDrawable(0);
		listOfCategoryFilter.clear();
		for (int i = 0; i < icons.length(); i++) {
			int resource = icons.getResourceId(i, 0);
			listOfCategoryFilter.add(new SearchOptionsFilter(false, resource,
					categoryTerms[i]));
		}

		final SearchFilterCategoryOptionsListAdapter adapter = new SearchFilterCategoryOptionsListAdapter(
				this.getActivity(), R.layout.search_options_view_item,
				listOfCategoryFilter);

		listview.setAdapter(adapter);

		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				boolean current = listOfCategoryFilter.get(position)
						.isSelected();
				for (SearchOptionsFilter f : listOfCategoryFilter)
					f.setSelected(false);

				listOfCategoryFilter.get(position).setSelected(!current);
				/*
				 * String term = listOfFilter.get(position).getSearchTerm(); if
				 * (current) { term = SessionManager.DEFAULT_STRING; } //
				 * searchTerm.setText(term); adapter.notifyDataSetChanged(); //
				 * requeue with search filters. We should pass in filters at //
				 * some point
				 * 
				 * SessionManager.getInstance(
				 * EventsContainerFragment.this.getActivity())
				 * .setEventCategory(term);
				 */
				selected_category = position;
				adapter.notifyDataSetChanged();
			}
		});

		for (SearchOptionsFilter f : listOfCategoryFilter) {
			if (f.getSearchTerm().equals(
					SessionManager.getInstance(this.getActivity())
							.getEventCategory())) {
				f.setSelected(true);
			}
		}
	}

	int radiusLocation = -1;
	final ArrayList<SearchOptionsFilter> listOfRadiusFilter = new ArrayList();

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

				radiusLocation = position;

				// searchTerm.setText(term);
				adapter.notifyDataSetChanged();
				// requeue with search filters. We should pass in filters at
				// some point

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
	String[] privacyItems,privacyItemsView;

	// privacy
	private void setUpStatus(View rootView) {
		final Button b = (Button) rootView
				.findViewById(R.id.private_public_btn);
		privacyItems = getResources().getStringArray(R.array.event_privacy);
		privacyItemsView = getResources().getStringArray(R.array.event_privacy_view);
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				AlertDialog.Builder builder = new AlertDialog.Builder(
						EventsContainerFragment.this.getActivity());

				builder.setItems(privacyItemsView,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int item) {
								// change this
								/*
								 * SessionManager.getInstance(
								 * EventsContainerFragment.this.getActivity())
								 * .setSearchString(items[item]);
								 */
								privacyPosition = item;
								b.setText(privacyItemsView[item]);
								dialog.cancel();
							}
						});
				AlertDialog alert = builder.create();
				alert.show();
			}

		});
		String searchStatus = SessionManager.getInstance(
				EventsContainerFragment.this.getActivity())
				.getSearchVisibility();
		/*if (!searchStatus.equals(SessionManager.DEFAULT_STRING)
				&& searchStatus != null)
			b.setText(searchStatus);
		else
		
		
			b.setText(privacyItemsView[0]);*/
		
		int i = 0;

		b.setText(privacyItemsView[0]);
		for (; i < privacyItems.length; i++) {
			if (searchStatus != null && privacyItems[i].equals(searchStatus)) {
				b.setText(privacyItemsView[i]);
			}
		}
	}

	// setUP

	public static final String DATEPICKER_START_TAG = "datepicker_start";
	public static final String TIMEPICKER_START_TAG = "timepicker_start";
	Button start_date, start_time;

	private void setUpTime(View rootView) {
		final Calendar calendar = Calendar.getInstance();
		final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
				this, calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH), false);
		final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
				this, calendar.get(Calendar.HOUR_OF_DAY),
				calendar.get(Calendar.MINUTE), false, false);
		Calendar c = Calendar.getInstance();
		int seconds = c.get(Calendar.SECOND);
		start_date = (Button) rootView.findViewById(R.id.start_date);
		start_date.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				datePickerDialog.setVibrate(false);
				datePickerDialog.setYearRange(1985, 2028);
				datePickerDialog.show(EventsContainerFragment.this
						.getActivity().getSupportFragmentManager(),
						DATEPICKER_START_TAG);
			}

		});

		start_time = (Button) rootView.findViewById(R.id.start_time);
		start_time.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				timePickerDialog.setVibrate(false);
				startTimeLastCalled = true;
				timePickerDialog.show(EventsContainerFragment.this
						.getActivity().getSupportFragmentManager(),
						TIMEPICKER_START_TAG);
			}
		});

		DatePickerDialog dpd = (DatePickerDialog) this.getActivity()
				.getSupportFragmentManager()
				.findFragmentByTag(DATEPICKER_START_TAG);
		if (dpd != null) {
			dpd.setOnDateSetListener(this);
		}
		TimePickerDialog tpd = (TimePickerDialog) this.getActivity()
				.getSupportFragmentManager()
				.findFragmentByTag(TIMEPICKER_START_TAG);
		if (tpd != null) {
			tpd.setOnTimeSetListener(this);
		}

		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH); // Note: zero based!
		int day = now.get(Calendar.DAY_OF_MONTH);
		int hour = now.get(Calendar.HOUR_OF_DAY);
		int minute = now.get(Calendar.MINUTE);
		String monthName = new DateFormatSymbols().getMonths()[month];

		start_date.setText(monthName + " " + day + ", " + year);
		// end_date.setText(monthName + " " + day + ", " + year);
		if (now.get(Calendar.AM_PM) == Calendar.PM) {
			start_time.setText(hour + ":" + String.format("%02d", minute));
			// end_time.setText(hour + ":" + String.format("%02d", minute + 1));
		} else {
			start_time.setText(hour + ":" + String.format("%02d", minute));
			// end_time.setText(hour + ":" + String.format("%02d", minute + 1));
		}
	}

	boolean startTimeLastCalled = true;

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

								// set items from all our field
								// categories
								if (selected_category != -1) {
									boolean categoryIsSelected = listOfCategoryFilter
											.get(selected_category)
											.isSelected();
									String categoryterm = SessionManager.DEFAULT_STRING;
									if (categoryIsSelected) {
										categoryterm = listOfCategoryFilter
												.get(selected_category)
												.getSearchTerm();

									}

									SessionManager.getInstance(
											EventsContainerFragment.this
													.getActivity())
											.setEventCategory(categoryterm);
								}

								// set private or public
								if (privacyPosition != -1) {

									SessionManager
											.getInstance(
													EventsContainerFragment.this
															.getActivity())
											.setSearchVisibility(
													privacyItems[privacyPosition]);
								}
								// Log.d("Privacy Position",
								// String.valueOf(privacyPosition));
								// start_date, start_time;
								// start time
								// raidus
								/*long epoch = FieldsParsingUtils.getTime(
										start_date.getText().toString(),
										start_time.getText().toString());
								SessionManager.getInstance(
										EventsContainerFragment.this
												.getActivity())
										.setEventStartTime(epoch);*/
								if (radiusLocation != -1) {
									boolean radiusSelected = listOfRadiusFilter
											.get(radiusLocation).isSelected();
									float radius = SessionManager.DEFAULT_SEARCH_RADIUS;
									if (radiusSelected) {
										radius = Float
												.valueOf(listOfRadiusFilter
														.get(radiusLocation)
														.getSearchTerm());
									}
									SessionManager.getInstance(
											EventsContainerFragment.this
													.getActivity())
											.setSearchRadius(radius);
								}

								// Log.d("Search Term", s);

								requestUpdate();
							}
						});
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View dialoglayout = inflater.inflate(R.layout.events_filters_popup,
				null);

		// setUpCategories
		setUpFilters(dialoglayout);
		// set Up Private/public
		setUpStatus(dialoglayout);
		// set Up Radius
		setUpRadius(dialoglayout);
		// set up time start
		setUpTime(dialoglayout);

		builder.setView(dialoglayout);
		builder.show();
	}

	public void requestUpdate() {
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
				b.putString(EventsDetailsRequest.BUNDLE_LOCATION,
						url_encode_location);
			}

			b.putString(EventsDetailsRequest.BUNDLE_LOCATION_TYPE,
					EventsDetailsRequest.BUNDLE_LOCATION_TYPE_ADDRESS);

			float f = SessionManager.getInstance(this.getActivity())
					.getSearchRadius();
			b.putFloat(EventsDetailsRequest.BUNDLE_RADIUS, f);
		} else if (currentLocation != null) {
			b.putString(EventsDetailsRequest.BUNDLE_LOCATION_TYPE,
					EventsDetailsRequest.BUNDLE_LOCATION_TYPE_COORDINATES);
			b.putString(EventsDetailsRequest.BUNDLE_LOCATION_LATITUDE,
					String.valueOf(currentLocation.getLatitude()));
			b.putString(EventsDetailsRequest.BUNDLE_LOCATION_LONGITUDE,
					String.valueOf(currentLocation.getLongitude()));

			float f = SessionManager.getInstance(this.getActivity())
					.getSearchRadius();
			b.putFloat(EventsDetailsRequest.BUNDLE_RADIUS, f);
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
		if (sm.getEventCategory() != null
				&& !sm.getEventCategory().equals(sm.DEFAULT_STRING)) {
			b.putString(EventsDetailsRequest.BUNDLE_CATEGORY,
					sm.getEventCategory());
		}
		if (sm.getSearchVisibility() != null
				&& !sm.getSearchVisibility().equals("")) {
			b.putString(EventsDetailsRequest.BUNDLE_VISIBILITY,
					sm.getSearchVisibility());
		} else {
			b.putString(EventsDetailsRequest.BUNDLE_VISIBILITY, "public");
		}

		if (sm.getEventTimeStart() != SessionManager.DEFAULT_VALUE) {
			b.putLong(EventsDetailsRequest.BUNDLE_TIME_START,
					sm.getEventTimeStart());
		}
		super.onRefresh(b);
	}

	@Override
	public Fragment mapViewFragment() {
		return new EventsMapViewFragment();
	}

	@Override
	public Fragment listViewFragment() {
		return new EventsListFragment();
	}

	@Override
	public void onDateSet(DatePickerDialog datePickerDialog, int year,
			int month, int day) {

		String monthName = new DateFormatSymbols().getMonths()[month];
		String total = monthName + " " + day + ", " + year;
		if (datePickerDialog.getTag().equals(DATEPICKER_START_TAG)) {
			start_date.setText(total);
		}
	}

	@Override
	public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
		String total = hourOfDay + ":" + String.format("%02d", minute);

		if (startTimeLastCalled) {
			start_time.setText(total);
		}
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
		helpers.add(SendRequestStrategyManager
				.getHelper(EventsDetailsRequest.class));

	}
}