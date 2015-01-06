package swipe.android.nearlings.viewAdapters;


import swipe.android.DatabaseHelpers.EventsDatabaseHelper;
import swipe.android.DatabaseHelpers.GroupsDatabaseHelper;
import swipe.android.DatabaseHelpers.NeedsDetailsDatabaseHelper;
import swipe.android.nearlings.DummyWebTask;
import swipe.android.nearlings.JsonChangeStateResponse;
import swipe.android.nearlings.NearlingsApplication;
import swipe.android.nearlings.NearlingsContentProvider;
import swipe.android.nearlings.NeedsDetailsFragment;
import swipe.android.nearlings.R;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.edbert.library.database.DatabaseCommandManager;
import com.edbert.library.network.AsyncTaskCompleteListener;
import com.edbert.library.network.PostDataWebTask;
import com.edbert.library.utils.MapUtils;
import com.gabesechan.android.reusable.location.ProviderLocationTracker;
import com.gabesechan.android.reusable.location.ProviderLocationTracker.ProviderType;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class EventsDetailAdapter implements
		AsyncTaskCompleteListener<JsonChangeStateResponse>,
		LoaderCallbacks<Cursor> {

	private Context context;

	private Cursor cr;
	public TextView title, event_time_date, rsvp_count, fee, visibility, category, description,location;
	// this needs to have a live adapter attached.
	public ScrollView fullScrollView;
	public MapFragment mapFragment;
	//may or may not need
	//public ImageView personRequestingImage;
	private String idOfEvent;
	private Cursor cursor;
private Button attend_event_btn, getDirections;
	public EventsDetailAdapter(View userDataView, Context context,
			String idOfEvent, Cursor cursor, Bundle savedInstanceState) {
		this.context = context;
		this.idOfEvent = idOfEvent;
		this.cursor = cursor;
		MapsInitializer.initialize(context);
		initializeView(userDataView, savedInstanceState);
		reloadData();
	}
	
	

	public View initializeView(View view, Bundle savedInstanceState) {
		fullScrollView = (ScrollView) view.findViewById(R.id.scroll_frame);
		
		title = (TextView) view.findViewById(R.id.event_title);
		event_time_date = (TextView) view.findViewById(R.id.event_time);
	
		rsvp_count= (TextView) view.findViewById(R.id.event_rsvp_count);
		fee= (TextView) view.findViewById(R.id.event_fee);
		visibility = (TextView) view.findViewById(R.id.event_visibility);
		category = (TextView) view.findViewById(R.id.event_category);
		description = (TextView) view.findViewById(R.id.event_description );
		location = (TextView) view.findViewById(R.id.event_location );
	
		MapsInitializer.initialize(((Activity) context));

		mapFragment = (MapFragment) ((Activity) this.context)
				.getFragmentManager().findFragmentById(R.id.mapview);

		ImageView transparentImageView = (ImageView) view
				.findViewById(R.id.transparent_image);

		transparentImageView.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					// Disallow ScrollView to intercept touch events.
					fullScrollView.requestDisallowInterceptTouchEvent(true);
					// Disable touch on transparent view
					return false;

				case MotionEvent.ACTION_UP:
					// Allow ScrollView to intercept touch events.
					fullScrollView.requestDisallowInterceptTouchEvent(false);
					return true;

				case MotionEvent.ACTION_MOVE:
					fullScrollView.requestDisallowInterceptTouchEvent(true);
					return false;

				default:
					return true;
				}
			}
		});
		// setUpMapIfNeeded(view);
	
		return null;
	}

	GoogleMap mMap;

	public void reloadData() {

		cursor.requery();

		cursor.moveToFirst();
		
		int title_index = cursor
				.getColumnIndexOrThrow(EventsDatabaseHelper.COLUMN_EVENT_NAME);
		int fee_index = cursor
				.getColumnIndexOrThrow(EventsDatabaseHelper.COLUMN_FEE);
		int visibility_index = cursor
				.getColumnIndexOrThrow(EventsDatabaseHelper.COLUMN_VISIBILITY);
		int description_index = cursor
				.getColumnIndexOrThrow(EventsDatabaseHelper.COLUMN_DESCRIPTION);
		
		int category_index = cursor
				.getColumnIndexOrThrow(EventsDatabaseHelper.COLUMN_CATEGORY);
		
		int date_index = cursor
				.getColumnIndexOrThrow(EventsDatabaseHelper.COLUMN_DATE_OF_EVENT);
		int time_index = cursor
				.getColumnIndexOrThrow(EventsDatabaseHelper.COLUMN_TIME_OF_EVENT);
		int rsvp_count_index = cursor
				.getColumnIndexOrThrow(EventsDatabaseHelper.COLUMN_RSVP_COUNT);
		int latitude_index = cursor
				.getColumnIndexOrThrow(EventsDatabaseHelper.COLUMN_LOCATION_LATITUDE);
		int longitude_index = cursor
				.getColumnIndexOrThrow(EventsDatabaseHelper.COLUMN_LOCATION_LONGITUDE);
		
		
		String titleString = cursor.getString(title_index);
		String description_string = cursor.getString(description_index);
		String visibility_string = cursor.getString(visibility_index);
		int rsvp_count_int = cursor.getInt(rsvp_count_index);
		double fee_amount = cursor.getDouble(fee_index);
		String category_string = cursor.getString(category_index);
		String date_string = cursor.getString(date_index);
		String time_string = cursor.getString(time_index);
		
		title.setText(titleString);
		description.setText(description_string);
		visibility.setText(visibility_string);
		event_time_date.setText(time_string + " on " + date_string);
		rsvp_count.setText(Integer.valueOf(rsvp_count_int));
		fee.setText(String.valueOf(fee_amount));
		category.setText(category_string);
		
		//MAP
		//location.setText(cursor.getString(location_name_index));
		// for now
		location.setVisibility(View.GONE);
		final double latitude = cursor.getDouble(latitude_index);
		final double longitude = cursor.getDouble(longitude_index);
		LatLng locationOfRequest = new LatLng(latitude, longitude);

		if (mapFragment != null) {
			mMap = mapFragment.getMap();

			mMap.getUiSettings().setMyLocationButtonEnabled(false);

			mMap.setMyLocationEnabled(true);

			mMap.getUiSettings().setZoomControlsEnabled(true);
			addUpMapMarker(latitude, longitude, titleString);

		}
		getDirections.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Location l = ((NearlingsApplication) EventsDetailAdapter.this.context
						.getApplicationContext()).getLastLocation();

				String url = "http://maps.google.com/maps?saddr="
						+ l.getLatitude() + "," + l.getLongitude() + "&daddr="
						+ latitude + "," + longitude;
				Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
						Uri.parse(url));
				EventsDetailAdapter.this.context.startActivity(intent);
			}

		});
		
		
		
		/*
		ImageLoader.getInstance()
				.displayImage(cursor.getString(personRequestImage_index),
						personRequestingImage,
						NearlingsApplication.getDefaultOptions());
		state = cursor.getString(status_index);
		if (state.equals(Needs.NOT_ACCEPTED_YET)) {
			changeState.setText(this.context.getString(
					R.string.mark_as_accepted,
					String.valueOf(cursor.getDouble(price_index))));
		} else if (state.equals(Needs.PENDING)) {
			changeState.setText(this.context
					.getString(R.string.mark_as_finished_task));
		} else if (state.equals(Needs.DONE_WAITING_FOR_REVIEW)) {
			changeState.setText(this.context
					.getString(R.string.mark_as_reviewed));
		}

		changeState.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// launch the request
				new DummyWebTask<JsonChangeStateResponse>((Activity) context,
						(AsyncTaskCompleteListener) GroupsViewAdapter.this,
						JsonChangeStateResponse.class).execute();

			}

		});
*/
		// ContentValues retVal = new ContentValues();
		//??
		valuesOfNeed = new ContentValues();
		DatabaseUtils.cursorRowToContentValues(cursor, valuesOfNeed);

	}

	String state;
	ContentValues valuesOfNeed;

	// this one should only have 1.
	private void addUpMapMarker(double lat, double log, String title) {
		Marker m = mMap.addMarker(new MarkerOptions().position(
				new LatLng(lat, log)).title(title));
		LatLngBounds.Builder b = new LatLngBounds.Builder();

		b.include(m.getPosition());

		LatLngBounds bounds = b.build();
		// Change the padding as per needed
		CameraUpdate cu = CameraUpdateFactory
				.newLatLngBounds(bounds, 25, 25, 5);
		CameraUpdate zoom = CameraUpdateFactory.zoomTo(14);
		mMap.moveCamera(cu);
		mMap.animateCamera(zoom);

	}

	@Override
	public void onTaskComplete(JsonChangeStateResponse result) {

		// write out
		if (valuesOfNeed != null && valuesOfNeed.size() != 0) {

			valuesOfNeed.put(NeedsDetailsDatabaseHelper.COLUMN_STATUS,
					JsonChangeStateResponse.getStatus(state));
			valuesOfNeed
					.put(DatabaseCommandManager.SQL_INSERT_OR_REPLACE, true);
			context.getContentResolver()
					.insert(NearlingsContentProvider
							.contentURIbyTableName(GroupsDatabaseHelper.TABLE_NAME),
							valuesOfNeed);
			((Activity) context).finish();

		}

	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {

	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {

	}
}