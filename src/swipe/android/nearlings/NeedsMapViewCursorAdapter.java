package swipe.android.nearlings;

import android.view.LayoutInflater;
import android.view.View;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

public class NeedsMapViewCursorAdapter implements InfoWindowAdapter {

	private View myContentsView;

	public NeedsMapViewCursorAdapter(LayoutInflater inflater, int layout) {

		myContentsView = inflater.inflate(layout, null);
	}

	public NeedsMapViewCursorAdapter(LayoutInflater inflater) {

		myContentsView = inflater.inflate(R.layout.list_of_needs_map_layout,
				null);
	}

	@Override
	public View getInfoContents(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		// TODO Auto-generated method stub
		return null;
	}

}