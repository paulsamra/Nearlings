package de.metagear.android.view.map;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

// reviewed
public class LocationSelectorOverlay extends LocationIndicatorOverlay {
	private OnLocationSelectedListener listener;

	public LocationSelectorOverlay(GeoPoint initialLocation,
			OnLocationSelectedListener listener) {
		super(initialLocation);
		this.listener = listener;
	}

	@Override
	public boolean onTap(GeoPoint geoPoint, MapView mapView) {
		listener.onLocationSelected(geoPoint);
		return true;
	}

	public interface OnLocationSelectedListener {
		void onLocationSelected(GeoPoint newLocation);
	}
}
