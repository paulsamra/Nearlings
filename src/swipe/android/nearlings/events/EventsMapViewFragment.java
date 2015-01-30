package swipe.android.nearlings.events;

import swipe.android.DatabaseHelpers.EventsDatabaseHelper;
import swipe.android.nearlings.BaseMapFragment;
import swipe.android.nearlings.EventsDetailsActivity;
import swipe.android.nearlings.NearlingsApplication;
import swipe.android.nearlings.NearlingsContentProvider;
import swipe.android.nearlings.R;
import swipe.android.nearlings.MessagesSync.EventsDetailsRequest;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.edbert.library.sendRequest.SendRequestStrategyManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;

public class EventsMapViewFragment extends BaseMapFragment {

	String MESSAGES_START_FLAG = EventsContainerFragment.MESSAGES_START_FLAG;
	String MESSAGES_FINISH_FLAG = EventsContainerFragment.MESSAGES_FINISH_FLAG;

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		// should clear before loading
		mMap.clear();

		int locationCount = 0;
		double lat = 0;
		double lng = 0;
		// float zoom = 0;

		// Number of locations available in the SQLite database table
		locationCount = arg1.getCount();
		// Move the current record pointer to the first row of the table
		arg1.moveToFirst();

		LatLngBounds.Builder bc = new LatLngBounds.Builder();
		for (int i = 0; i < locationCount; i++) {

			// pull from needs DB
			lat = arg1
					.getDouble(arg1
							.getColumnIndex(EventsDatabaseHelper.COLUMN_LOCATION_LATITUDE));
			lng = arg1
					.getDouble(arg1
							.getColumnIndex(EventsDatabaseHelper.COLUMN_LOCATION_LONGITUDE));
			double[] latLng = coordinateForMarker(lat, lng);
			this.addLocation(latLng[0], latLng[1]);
			LatLng location = new LatLng(latLng[0], latLng[1]);
			String title = arg1.getString(arg1
					.getColumnIndex(EventsDatabaseHelper.COLUMN_EVENT_NAME));
			String description = arg1.getString(arg1
					.getColumnIndex(EventsDatabaseHelper.COLUMN_DESCRIPTION));
			drawMarker(location, title, description, String.valueOf(i));
			bc.include(location);
			arg1.moveToNext();
			// fx zoom
		}
		for (int i = 0; i < markerLocation.size(); i++) {
			Log.d("Testing", markerLocation.get(i));
		}
		// bc needs to include your current location as well as the default
		Location l = ((NearlingsApplication) this.getActivity()
				.getApplication()).getCurrentLocation();
		bc.include(new LatLng(l.getLatitude(), l.getLongitude()));

		mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bc.build(), 50));
	}

	@Override
	public void setSourceRequestHelper() {
		helpers.add(SendRequestStrategyManager
				.getHelper(EventsDetailsRequest.class));// new
												// NeedsDetailsRequest(this.getActivity(),
												// JsonExploreResponse.class);
	}

	// obsolete since we're using a cursor callback
	@Override
	public CursorLoader generateCursorLoader() {
		CursorLoader cursorLoader = new CursorLoader(
				this.getActivity(),
				NearlingsContentProvider
						.contentURIbyTableName(EventsDatabaseHelper.TABLE_NAME),
				EventsDatabaseHelper.COLUMNS, null, null,
				EventsDatabaseHelper.COLUMN_TIME_OF_EVENT + " DESC");

		return cursorLoader;

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
	protected void attachInfoWindowAdapter() {

		mMap.setInfoWindowAdapter(new InfoWindowAdapter() {
			@Override
			public View getInfoWindow(Marker arg0) {
				return null;
			}

			@Override
			public View getInfoContents(Marker marker) {
				View myContentView = EventsMapViewFragment.this.getActivity()
						.getLayoutInflater()
						.inflate(R.layout.needs_marker, null);
				TextView needs_title = ((TextView) myContentView
						.findViewById(R.id.needs_task));
				needs_title.setText(marker.getTitle());

				return myContentView;
			}
		});

	}

	@Override
	protected void attachInfoWindowClickListener() {
		mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

			@Override
			public void onInfoWindowClick(Marker marker) {
				Intent intent = new Intent(EventsMapViewFragment.this
						.getActivity(), EventsDetailsActivity.class);
				Bundle extras = new Bundle();
				Cursor c = generateCursor();
				String snip = marker.getSnippet();

				int position = Integer.valueOf(snip.substring(
						snip.lastIndexOf(",") + 1, snip.length()));
				c.moveToPosition(position);
				String need_id = c.getString(c
						.getColumnIndex(EventsDatabaseHelper.COLUMN_ID));
				extras.putString("id", need_id);
				intent.putExtras(extras);

				startActivity(intent);
			}

		});
	}
}