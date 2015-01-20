/**
 * Class is the container for a listview and mapview. Both listview and mapview represent
 * 2 views of the same action. 
 */
package swipe.android.nearlings.events;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;

import swipe.android.nearlings.BaseContainerFragment;
import swipe.android.nearlings.NearlingsApplication;
import swipe.android.nearlings.R;
import swipe.android.nearlings.SessionManager;
import swipe.android.nearlings.MessagesSync.EventsDetailsRequest;
import swipe.android.nearlings.MessagesSync.NeedsDetailsRequest;
import swipe.android.nearlings.discover.options.SearchFilterCategoryOptionsListAdapter;
import swipe.android.nearlings.discover.options.SearchOptionsFilter;
import swipe.android.nearlings.needs.DiscoverContainerFragment;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
	
	// categories
	private void setUpFilters(View rootView) {
		
		final HorizontalListView listview = (HorizontalListView) rootView
				.findViewById(R.id.search_options_listview_categories);
		final ArrayList<SearchOptionsFilter> listOfFilter = new ArrayList();
		Resources res = getResources();
		TypedArray icons = res.obtainTypedArray(R.array.event_types_unchecked);
		String[] terms = res.getStringArray(R.array.event_types);
		// Drawable drawable = icons.getDrawable(0);

		for (int i = 0; i < icons.length(); i++) {
			int resource = icons.getResourceId(i, 0);
			listOfFilter
					.add(new SearchOptionsFilter(false, resource, terms[i]));
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
					term = SessionManager.DEFAULT_STRING;
				}
				// searchTerm.setText(term);
				adapter.notifyDataSetChanged();
				// requeue with search filters. We should pass in filters at
				// some point
				SessionManager.getInstance(
						EventsContainerFragment.this.getActivity())
						.setEventCategory(term);
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
		Resources res = getResources();
		TypedArray selectedIcons = res
				.obtainTypedArray(R.array.radius_selected_icons);
		TypedArray unselectedIcons = res
				.obtainTypedArray(R.array.radius_unselected_icons);

		String[] radius = res.getStringArray(R.array.radius);
		for (int i = 0; i < radius.length; i++) {

			int selectedResource = selectedIcons.getResourceId(i, 0);
			int unselectedResource = unselectedIcons.getResourceId(i, 0);

			listOfFilter.add(new SearchOptionsFilter(false, unselectedResource,
					selectedResource, radius[i]));
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
					term = String.valueOf(SessionManager.DEFAULT_VALUE);
				}
				// searchTerm.setText(term);
				adapter.notifyDataSetChanged();
				// requeue with search filters. We should pass in filters at
				// some point
				SessionManager.getInstance(
						EventsContainerFragment.this.getActivity())
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

	// privacy
	private void setUpStatus(View rootView) {
		final Button b = (Button) rootView.findViewById(R.id.private_public_btn);
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				final String[] items = getResources().getStringArray(
						R.array.event_privacy);
				AlertDialog.Builder builder = new AlertDialog.Builder(
						EventsContainerFragment.this.getActivity());

				builder.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						// change this
						SessionManager.getInstance(
								EventsContainerFragment.this.getActivity())
								.setSearchString(items[item]);

						b.setText(items[item]);
						dialog.cancel();
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
			}

		});
		String searchStatus = SessionManager.getInstance(
				EventsContainerFragment.this.getActivity()).getSearchString();
		if (!searchStatus.equals(SessionManager.DEFAULT_STRING))
			b.setText(searchStatus);
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

			DatePickerDialog dpd = (DatePickerDialog) this.getActivity().getSupportFragmentManager()
					.findFragmentByTag(DATEPICKER_START_TAG);
			if (dpd != null) {
				dpd.setOnDateSetListener(this);
			}
			TimePickerDialog tpd = (TimePickerDialog)  this.getActivity().getSupportFragmentManager()
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
		AlertDialog.Builder builder = new AlertDialog.Builder(
				super.context); 
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
								SessionManager.getInstance(
										EventsContainerFragment.this
												.getActivity())
										.commitPendingChanges();
								EventsContainerFragment.this
										.updateSearchString();

								requestUpdate();
							}
						});
LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		if (!sm.getSearchLocation().equals("")) {
			b.putString(EventsDetailsRequest.BUNDLE_LOCATION,
					sm.getSearchLocation());
			b.putString(EventsDetailsRequest.BUNDLE_LOCATION_TYPE,
					NeedsDetailsRequest.BUNDLE_LOCATION_TYPE_ADDRESS);
			b.putFloat(EventsDetailsRequest.BUNDLE_RADIUS, 20.0f);
		}
	     if(sm.getSearchString() != null && !sm.getSearchString().equals( sm.DEFAULT_STRING)){
				b.putString(EventsDetailsRequest.BUNDLE_KEYWORDS, sm.getSearchString());
			}
	     
	     if(sm.getEventCategory() != null && !sm.getEventCategory().equals(sm.DEFAULT_STRING)){
				b.putString(EventsDetailsRequest.BUNDLE_CATEGORY, sm.getEventCategory());
			}
	     
	   /*  if(sm.getTimeStart() != null && sm.getTimeStart() != sm.DEFAULT_VALUE){
				b.putString(EventsDetailsRequest.BUNDLE_TIME_START, sm.getTimeStart());
			}
		   */

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
	public void onResume(){
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
			super.helper = SendRequestStrategyManager
					.getHelper(EventsDetailsRequest.class);
		
	}
}