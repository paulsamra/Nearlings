package swipe.android.nearlings.viewAdapters;

import java.util.Random;

import swipe.android.DatabaseHelpers.MessagesDatabaseHelper;
import swipe.android.DatabaseHelpers.NeedsCommentsDatabaseHelper;
import swipe.android.DatabaseHelpers.NeedsDetailsDatabaseHelper;
import swipe.android.nearlings.DummyWebTask;
import swipe.android.nearlings.JsonChangeStateResponse;
import swipe.android.nearlings.NearlingsApplication;
import swipe.android.nearlings.NearlingsContentProvider;
import swipe.android.nearlings.R;
import swipe.android.nearlings.MessagesSync.Needs;
import swipe.android.nearlings.jsonResponses.login.JsonBidsResponse;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.location.Location;
import android.location.LocationManager;
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
import com.gabesechan.android.reusable.location.ProviderLocationTracker;
import com.gabesechan.android.reusable.location.ProviderLocationTracker.ProviderType;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class NeedsDetailViewAdapter implements AsyncTaskCompleteListener<JsonChangeStateResponse>
, LoaderCallbacks<Cursor>{

	private Context context;

	private Cursor cr;
	public TextView title, price, date, personRequesting, description,
			location, numOfComments;

	// this needs to have a live adapter attached.
	public ScrollView commentView;
	public MapView mapFragment;
	public ImageView personRequestingImage;
	public ListView listOfComments;
	private String idOfDetail;
	private Cursor cursor;
	private Cursor commentCursor;
	public Button changeState;

	public NeedsDetailViewAdapter(View userDataView, Context context,
			String idOfDetail, Cursor cursor, Bundle savedInstanceState) {
		this.context = context;
		this.idOfDetail = idOfDetail;
		this.cursor = cursor;
		MapsInitializer.initialize(context);
		initializeView(userDataView, savedInstanceState);
		reloadData();
	}

	public View initializeView(View view, Bundle savedInstanceState) {
		changeState = (Button) view.findViewById(R.id.needs_change_state);
		title = (TextView) view.findViewById(R.id.needs_details_title);
		price = (TextView) view.findViewById(R.id.needs_details_price);
		date = (TextView) view.findViewById(R.id.needs_details_date);
		personRequesting = (TextView) view
				.findViewById(R.id.needs_details_author);
		description = (TextView) view
				.findViewById(R.id.needs_details_description);
		location = (TextView) view.findViewById(R.id.needs_details_location);
		numOfComments = (TextView) view
				.findViewById(R.id.needs_details_numOfComments);
		

		MapsInitializer.initialize(((Activity) context));
		mapFragment = (MapView) view.findViewById(R.id.needs_details_map);

		mapFragment.onCreate(savedInstanceState);

		setUpMapIfNeeded(view);

		if (mapFragment != null) {
			mMap = mapFragment.getMap();

			mMap.getUiSettings().setMyLocationButtonEnabled(false);

			mMap.setMyLocationEnabled(true);

			mMap.getUiSettings().setZoomControlsEnabled(true);

	//	((Activity)context).getLoaderManager().initLoader(10, null, this);
		}
		
		mapFragment.onResume();
		//mapFragment = (MapView) view.findViewById(R.id.needs_details_map);
	//	mapFragment.onCreate(savedInstanceState);

	//	setUpMapIfNeeded(view);
		personRequestingImage = (ImageView) view
				.findViewById(R.id.needs_details_author_image_preview);
		listOfComments = (ListView) view
				.findViewById(R.id.needs_details_comments_list);
		listOfComments.setOnTouchListener(new OnTouchListener() {
			// Setting on Touch Listener for handling the touch inside
			// ScrollView
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// Disallow the touch request for parent scroll on touch of
				// child view
				v.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
		// setListViewHeightBasedOnChildren(listOfComments);
		// this needs to have a live adapter attached.
		/*
		 * String selectionClause = NeedsCommentsDatabaseHelper.COLUMN_ID +
		 * " = '" + idOfDetail + "'"; commentCursor = context
		 * .getContentResolver() .query(NearlingsContentProvider
		 * .contentURIbyTableName(NeedsCommentsDatabaseHelper.TABLE_NAME),
		 * NeedsCommentsDatabaseHelper.COLUMNS, selectionClause, null, null);
		 */
		commentCursor = context
				.getContentResolver()
				.query(NearlingsContentProvider
						.contentURIbyTableName(NeedsCommentsDatabaseHelper.TABLE_NAME),
						NeedsCommentsDatabaseHelper.COLUMNS, null, null, null);
		commentAdapter = new NeedsDetailCommentsAdapter(context, commentCursor);
		commentAdapter.notifyDataSetChanged();
		listOfComments.setAdapter(commentAdapter);
		// view.setTag(holder);*/
		return null;
	}

	NeedsDetailCommentsAdapter commentAdapter;
	GoogleMap mMap;

	public void reloadData() {

		cursor.requery();

		cursor.moveToFirst();
		int title_index = cursor
				.getColumnIndexOrThrow(NeedsDetailsDatabaseHelper.COLUMN_TITLE);
		int price_index = cursor
				.getColumnIndexOrThrow(NeedsDetailsDatabaseHelper.COLUMN_PRICE);
		int date_index = cursor
				.getColumnIndexOrThrow(NeedsDetailsDatabaseHelper.COLUMN_DATE);
		int author_index = cursor
				.getColumnIndexOrThrow(NeedsDetailsDatabaseHelper.COLUMN_AUTHOR);

		int description_index = cursor
				.getColumnIndexOrThrow(NeedsDetailsDatabaseHelper.COLUMN_DESCRIPTION);
		int latitude_index = cursor
				.getColumnIndexOrThrow(NeedsDetailsDatabaseHelper.COLUMN_LOCATION_GEOPOINT_LATITUDE);
		int longitude_index = cursor
				.getColumnIndexOrThrow(NeedsDetailsDatabaseHelper.COLUMN_LOCATION_GEOPOINT_LONGITUDE);
		int location_name_index = cursor
				.getColumnIndexOrThrow(NeedsDetailsDatabaseHelper.COLUMN_LOCATION_NAME);
		int needs_id_index = cursor
				.getColumnIndexOrThrow(NeedsDetailsDatabaseHelper.COLUMN_ID);
		int status_index = cursor
				.getColumnIndexOrThrow(NeedsDetailsDatabaseHelper.COLUMN_STATUS);

		int personRequestImage_index = cursor
				.getColumnIndexOrThrow(NeedsDetailsDatabaseHelper.COLUMN_AUTHOR_IMAGE_PREVIEW_URL);

		title.setText(cursor.getString(title_index));
		price.setText(String.valueOf(cursor.getDouble(price_index)));

		date.setText(cursor.getString(date_index));

		personRequesting.setText(cursor.getString(author_index));
		description.setText(cursor.getString(description_index));
		location.setText(cursor.getString(location_name_index));

		double latitude = cursor.getDouble(latitude_index);
		double longitude = cursor.getDouble(longitude_index);
		LatLng locationOfRequest = new LatLng(0.0, 0.0);
		
		if (mapFragment != null) {
			mMap = mapFragment.getMap();

			mMap.getUiSettings().setMyLocationButtonEnabled(false);

			mMap.setMyLocationEnabled(true);

			mMap.getUiSettings().setZoomControlsEnabled(true);

			drawMarker(locationOfRequest);

		}
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
		
		changeState.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//launch the request
				 new DummyWebTask<JsonChangeStateResponse>((Activity) context,(AsyncTaskCompleteListener) NeedsDetailViewAdapter.this,
						 JsonChangeStateResponse.class).execute();

			}
			
		});

//ContentValues retVal = new ContentValues();
		valuesOfNeed =  new ContentValues();
DatabaseUtils.cursorRowToContentValues(cursor, valuesOfNeed);

		reloadCommentData();
	}
	String state;
ContentValues valuesOfNeed;

	private void drawMarker(LatLng point) {
		// Creating an instance of MarkerOptions
		MarkerOptions markerOptions = new MarkerOptions();

		// Setting latitude and longitude for the marker
		markerOptions.position(point);

		// Adding marker on the Google Map
		mMap.addMarker(markerOptions);
	}

	public void reloadCommentData() {
		commentCursor.requery();

		commentAdapter = new NeedsDetailCommentsAdapter(context, commentCursor);
		// commentAdapter.notifyDataSetChanged();
		listOfComments.setAdapter(commentAdapter);

		// commentCursor.moveToFirst();
		// /String count = String.valueOf(commentCursor.getCount());

	}

	private void setUpMapIfNeeded(View inflatedView) {
		if (mMap == null) {
			mMap = ((MapView) inflatedView.findViewById(R.id.needs_details_map))
					.getMap();
			if (mMap != null) {
				setUpMap();
			}
		}
	}

	private void setUpMap() {
		mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title(
				"Marker"));
	}

	@Override
	public void onTaskComplete(JsonChangeStateResponse result) {

		//write out
		Log.e("DONE","DONE");
		if(valuesOfNeed != null && valuesOfNeed.size() != 0){
			Log.e("STATE","state");
			Log.e("NEW STATe", JsonChangeStateResponse.getStatus(state));
			
		valuesOfNeed.put(NeedsDetailsDatabaseHelper.COLUMN_STATUS, JsonChangeStateResponse.getStatus(state));
		valuesOfNeed.put(DatabaseCommandManager.SQL_INSERT_OR_REPLACE, true);
		context.getContentResolver()
				.insert(NearlingsContentProvider
						.contentURIbyTableName(NeedsDetailsDatabaseHelper.TABLE_NAME),
						valuesOfNeed);
		((Activity)context).finish();

		}
	
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		
	}
}