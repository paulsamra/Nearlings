package swipe.android.nearlings.viewAdapters;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import swipe.android.DatabaseHelpers.NeedsCommentsDatabaseHelper;
import swipe.android.DatabaseHelpers.NeedsDetailsDatabaseHelper;
import swipe.android.nearlings.NearlingsApplication;
import swipe.android.nearlings.NearlingsContentProvider;
import swipe.android.nearlings.R;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

public class NeedsDetailViewAdapter {

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
		mapFragment = (MapView) view.findViewById(R.id.needs_details_map);
		mapFragment.onCreate(savedInstanceState);

				setUpMapIfNeeded(view);
		personRequestingImage = (ImageView) view
				.findViewById(R.id.needs_details_author_image_preview);
		listOfComments = (ListView) view
				.findViewById(R.id.needs_details_comments_list);
		// this needs to have a live adapter attached.
		String selectionClause = NeedsCommentsDatabaseHelper.COLUMN_ID + " = '"
				+ idOfDetail + "'";
		Cursor commentCursor = context
				.getContentResolver()
				.query(NearlingsContentProvider
						.contentURIbyTableName(NeedsCommentsDatabaseHelper.TABLE_NAME),
						NeedsCommentsDatabaseHelper.COLUMNS, selectionClause,
						null, null);
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
	/*	Cursor cursor = context
				.getContentResolver()
				.query(NearlingsContentProvider
						.contentURIbyTableName(NeedsDetailsDatabaseHelper.TABLE_NAME),
						NeedsDetailsDatabaseHelper.COLUMNS, null, null, null);*/
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

		int personRequestImage_index = cursor
				.getColumnIndexOrThrow(NeedsDetailsDatabaseHelper.COLUMN_AUTHOR_IMAGE_PREVIEW_URL);

		title.setText(cursor.getString(title_index));
		price.setText(cursor.getString(price_index));

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
		/*
		 * personRequestingImage = (ImageView) view
		 * .findViewById(R.id.needs_details_author_image_preview);
		 */

		reloadCommentData();
	}

	private void drawMarker(LatLng point) {
		// Creating an instance of MarkerOptions
		MarkerOptions markerOptions = new MarkerOptions();

		// Setting latitude and longitude for the marker
		markerOptions.position(point);

		// Adding marker on the Google Map
		mMap.addMarker(markerOptions);
	}

	public void reloadCommentData() {

		// commentCursor.moveToFirst();
		// /String count = String.valueOf(commentCursor.getCount());

	}

	private void setUpMapIfNeeded(View inflatedView) {
        if (mMap == null) {
            mMap = ((MapView) inflatedView.findViewById(R.id.needs_details_map)).getMap();
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }
}