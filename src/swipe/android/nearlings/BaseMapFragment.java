package swipe.android.nearlings;

import java.util.LinkedList;

import swipe.android.nearlings.events.EventsContainerFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public abstract class BaseMapFragment extends NearlingsSwipeToRefreshFragment
		implements LoaderCallbacks<Cursor>, OnMapLoadedCallback {

	protected GoogleMap mMap;
	MapView mMapView;
	String MESSAGES_START_FLAG = EventsContainerFragment.MESSAGES_START_FLAG;
	String MESSAGES_FINISH_FLAG = EventsContainerFragment.MESSAGES_FINISH_FLAG;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.discover_needs_map_layout,
				container, false);

		MapsInitializer.initialize(getActivity());

		mMapView = (MapView) view.findViewById(R.id.needs_map_view_map);

		mMapView.onCreate(savedInstanceState);

		setUpMapIfNeeded(view);

		if (mMapView != null) {
			mMap = mMapView.getMap();
			if(mMap != null){
				
			
			mMap.setOnMapLoadedCallback(this);

			mMap.getUiSettings().setMyLocationButtonEnabled(false);

			mMap.setMyLocationEnabled(true);

			mMap.getUiSettings().setZoomControlsEnabled(true);

			this.getLoaderManager().initLoader(10, null, this);
			}
		}
		return view;

	}

	protected abstract void attachInfoWindowAdapter();

	protected abstract void attachInfoWindowClickListener();

	protected void drawMarker(LatLng point, String title,
			String... snippetStuff) {
		// Creating an instance of MarkerOptions
		MarkerOptions markerOptions = new MarkerOptions();

		// Setting latitude and longitude for the marker
		markerOptions.position(point);
		markerOptions.title(title);
		String newString = "";
		for (String s : snippetStuff) {
			newString = newString + "," + s;
		}
		markerOptions.snippet(newString);
		// Adding marker on the Google Map
		mMap.addMarker(markerOptions);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		return generateCursorLoader();
	}

	/*
	 * @Override public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
	 * // should clear before loading mMap.clear();
	 * 
	 * int locationCount = 0; double lat = 0; double lng = 0; // float zoom = 0;
	 * 
	 * // Number of locations available in the SQLite database table
	 * locationCount = arg1.getCount(); // Move the current record pointer to
	 * the first row of the table arg1.moveToFirst();
	 * 
	 * LatLngBounds.Builder bc = new LatLngBounds.Builder(); for (int i = 0; i <
	 * locationCount; i++) {
	 * 
	 * // pull from needs DB lat = arg1 .getDouble(arg1
	 * .getColumnIndex(EventsDatabaseHelper.COLUMN_LOCATION_LATITUDE)); lng =
	 * arg1 .getDouble(arg1
	 * .getColumnIndex(EventsDatabaseHelper.COLUMN_LOCATION_LONGITUDE));
	 * double[] latLng = coordinateForMarker(lat, lng);
	 * this.addLocation(latLng[0], latLng[1]); LatLng location = new
	 * LatLng(latLng[0], latLng[1]); String title = arg1.getString(arg1
	 * .getColumnIndex(EventsDatabaseHelper.COLUMN_EVENT_NAME)); String
	 * description = arg1.getString(arg1
	 * .getColumnIndex(EventsDatabaseHelper.COLUMN_DESCRIPTION));
	 * drawMarker(location, title, description, String.valueOf(i));
	 * bc.include(location); arg1.moveToNext(); // fx zoom }
	 * 
	 * // bc needs to include your current location as well as the default
	 * Location l = ((NearlingsApplication) this.getActivity()
	 * .getApplication()).getCurrentLocation(); bc.include(new
	 * LatLng(l.getLatitude(), l.getLongitude()));
	 * 
	 * mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bc.build(), 50)); }
	 */

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// Nullify current cursor before reloading

	}

	protected void setUpMapIfNeeded(View inflatedView) {
		if (mMap == null && inflatedView != null) {
			mMap = ((MapView) inflatedView
					.findViewById(R.id.needs_map_view_map)).getMap();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		setUpMapIfNeeded(mMapView);
		mMapView.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		mMapView.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mMapView.onDestroy();
	}
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		setUpMapIfNeeded(mMapView);
	}

	public abstract void setSourceRequestHelper();

	// obsolete since we're using a cursor callback

	public abstract CursorLoader generateCursorLoader();

	// obsolete since we're not using a listview.
	@Override
	public void reloadData() {
	}

	public abstract String syncStartedFlag();

	public abstract String syncFinishedFlag();

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		menu.clear();
	}

	@Override
	public void reloadAdapter() {
		// TODO Auto-generated method stub

	}

	protected LinkedList<String> markerLocation = new LinkedList<String>();
	static final float COORDINATE_OFFSET = 0.00076f;
	static final int MAX_NUMBER_OF_MARKERS = 99;

	// Check if any marker is displayed on given coordinate. If yes then decide
	// another appropriate coordinate to display this marker. It returns an
	// array with latitude(at index 0) and longitude(at index 1).
	protected double[] coordinateForMarker(double latitude, double longitude) {

		double[] location = new double[2];

		for (int i = 0; i <= MAX_NUMBER_OF_MARKERS; i++) {

			if (mapAlreadyHasMarkerForLocation((latitude + i
					* COORDINATE_OFFSET)
					+ "," + (longitude + i * COORDINATE_OFFSET))) {

				// If i = 0 then below if condition is same as upper one. Hence,
				// no need to execute below if condition.
				if (i == 0)
					continue;

				if (mapAlreadyHasMarkerForLocation((latitude - i
						* COORDINATE_OFFSET)
						+ "," + (longitude - i * COORDINATE_OFFSET))) {

					continue;

				} else {
					location[0] = latitude - (i * COORDINATE_OFFSET);
					location[1] = longitude - (i * COORDINATE_OFFSET);
					break;
				}

			} else {
				location[0] = latitude + (i * COORDINATE_OFFSET);
				location[1] = longitude + (i * COORDINATE_OFFSET);
				break;
			}
		}

		return location;
	}

	// Return whether marker with same location is already on map
	protected boolean mapAlreadyHasMarkerForLocation(String location) {

		return (markerLocation.contains(location));
	}

	protected void addLocation(double latitude, double longitude) {

		markerLocation.add(latitude + "," + longitude);
	}

	@Override
	public void onMapLoaded() {
		if (mMap != null) {
			attachInfoWindowAdapter();
			attachInfoWindowClickListener();
		}
	}

}