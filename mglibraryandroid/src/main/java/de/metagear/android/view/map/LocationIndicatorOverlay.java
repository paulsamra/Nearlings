package de.metagear.android.view.map;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

// reviewed
public class LocationIndicatorOverlay extends Overlay {
	protected GeoPoint geoPoint;
	protected Canvas canvas;
	protected MapView mapView;
	protected boolean shadow;

	public LocationIndicatorOverlay(GeoPoint geoPoint) {
		this.geoPoint = geoPoint;
	}

	public GeoPoint getGeoPoint() {
		return geoPoint;
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		super.draw(canvas, mapView, shadow);
		
		this.canvas = canvas;
		this.mapView = mapView;
		this.shadow = shadow;
		
		Projection projection = mapView.getProjection();
		Point point = new Point();
		projection.toPixels(geoPoint, point);

		Paint paint = new Paint();
		paint.setARGB(250, 0, 0, 255);

		canvas.drawCircle(point.x, point.y, 5, paint);
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other == null) || !(other instanceof LocationSelectorOverlay)) {
			return false;
		}
		
		LocationSelectorOverlay otherOverlay = (LocationSelectorOverlay) other;

		return otherOverlay.getGeoPoint().getLatitudeE6() == geoPoint
				.getLatitudeE6()
				&& otherOverlay.getGeoPoint().getLongitudeE6() == geoPoint
						.getLongitudeE6();
	}

	@Override
	public int hashCode() {
		return geoPoint.getLatitudeE6() | geoPoint.getLongitudeE6();
	}
}
