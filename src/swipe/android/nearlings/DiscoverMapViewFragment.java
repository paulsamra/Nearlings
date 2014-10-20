package swipe.android.nearlings;

import swipe.android.DatabaseHelpers.NeedsDetailsDatabaseHelper;
import swipe.android.nearlings.MessagesSync.NeedsDetailsRequest;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DiscoverMapViewFragment extends NearlingsSwipeToRefreshFragment implements LoaderCallbacks<Cursor> {

	private GoogleMap mMap;
	MapView mMapView;
	String MESSAGES_START_FLAG = DiscoverMapViewFragment.class
			.getCanonicalName() + "_MESSAGES_START_FLAG";
	String MESSAGES_FINISH_FLAG = DiscoverMapViewFragment.class
			.getCanonicalName() + "_MESSAGES_FINISH_FLAG";
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

			mMap.getUiSettings().setMyLocationButtonEnabled(false);

			mMap.setMyLocationEnabled(true);

			mMap.getUiSettings().setZoomControlsEnabled(true);
			
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
		mMap.addMarker(markerOptions);
	}

	

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {

		// Uri to the content provider LocationsContentProvider
		Uri uri = NearlingsContentProvider.contentURIbyTableName(NeedsDetailsDatabaseHelper.TABLE_NAME);
		// Fetches all the rows from locations table
		return new CursorLoader(this.getActivity(), uri, null, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		//should clear before loading
		mMap.clear();
		
		int locationCount = 0;
		double lat = 0;
		double lng = 0;
		//float zoom = 0;

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
		 CameraUpdate center=
			        CameraUpdateFactory.newLatLng(new LatLng(lat,
			                                                lng));
			    CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);

			    mMap.moveCamera(center);
			    mMap.animateCamera(zoom, 0, null);
			  //  mMap.animateCamera(zoom);
//		mMap.animateCamera( CameraUpdateFactory.zoomTo( 17.0f ) );   
		//we want to encompass all points. Remodify code for this
		/*if (locationCount > 0) {
		
			mMap.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null);
			// Setting the zoom level in the map on last position is clicked
			//mMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));
		}*/
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// Nullify current cursor before reloading
		
	}
	
	private void setUpMapIfNeeded(View inflatedView) {
        if (mMap == null) {
            mMap = ((MapView) inflatedView.findViewById(R.id.needs_map_view_map)).getMap();
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

	@Override
	public void setSourceRequestHelper() {
		super.helper = new NeedsDetailsRequest();
	}
//obsolete since we're using a cursor callback
	@Override
	public CursorLoader generateCursorLoader() {
		return null;
	}

	//obsolete since we're not using a listview.
	@Override
	public void reloadData() {
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
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		super.onCreateOptionsMenu(menu, inflater);
		 menu.clear();
		inflater.inflate(R.menu.switch_to_list_view, menu);
	}
}