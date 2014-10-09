package swipe.android.nearlings;

import swipe.android.DatabaseHelpers.NeedsDetailsDatabaseHelper;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class NeedsMapView extends Fragment implements LoaderCallbacks<Cursor> {

	private GoogleMap googleMap;
	MapView mapView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.list_of_needs_map_layout,
				container, false);

		mapView = (MapView) view.findViewById(R.id.needs_map_view_map);

		mapView.onCreate(savedInstanceState);

		if (mapView != null) {
			googleMap = mapView.getMap();

			googleMap.getUiSettings().setMyLocationButtonEnabled(false);

			googleMap.setMyLocationEnabled(true);

			googleMap.getUiSettings().setZoomControlsEnabled(true);
			
			this.getLoaderManager().initLoader(0, null, this);
		}
		return view;

	}
 
 
    
	

	private void drawMarker(LatLng point) {
		// Creating an instance of MarkerOptions
		MarkerOptions markerOptions = new MarkerOptions();

		// Setting latitude and longitude for the marker
		markerOptions.position(point);

		// Adding marker on the Google Map
		googleMap.addMarker(markerOptions);
	}

	

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {

		// Uri to the content provider LocationsContentProvider
		Uri uri = LocationsContentProvider.CONTENT_URI;

		// Fetches all the rows from locations table
		return new CursorLoader(this, uri, null, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		int locationCount = 0;
		double lat = 0;
		double lng = 0;
		float zoom = 0;

		// Number of locations available in the SQLite database table
		locationCount = arg1.getCount();

		// Move the current record pointer to the first row of the table
		arg1.moveToFirst();

		for (int i = 0; i < locationCount; i++) {

			//pull from needs DB
			lat = arg1.getDouble(arg1.getColumnIndex(NeedsDetailsDatabaseHelper.COLUMN_LOCATION_GEOPOINT_LATITUDE));
			// Get the latitude
			lng = arg1.getDouble(arg1.getColumnIndex(NeedsDetailsDatabaseHelper.COLUMN_LOCATION_GEOPOINT_LONGITUDE));

			// Creating an instance of LatLng to plot the location in Google
			// Maps
			LatLng location = new LatLng(lat, lng);

			// Drawing the marker in the Google Maps
			drawMarker(location);

			// Traverse the pointer to the next row
			arg1.moveToNext();
		}

		//we want to encompass all points. Remodify code for this
		if (locationCount > 0) {
			// Moving CameraPosition to last clicked position
			googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat,
					lng)));

			// Setting the zoom level in the map on last position is clicked
			googleMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));
		}
	}

}