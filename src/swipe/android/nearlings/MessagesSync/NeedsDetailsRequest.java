package swipe.android.nearlings.MessagesSync;

import java.util.Calendar;
import java.util.Map;
import java.util.Random;

import swipe.android.DatabaseHelpers.MessagesDatabaseHelper;
import swipe.android.DatabaseHelpers.NeedsDetailsDatabaseHelper;
import swipe.android.nearlings.NearlingsContentProvider;
import swipe.android.nearlings.NearlingsRequest;
import swipe.android.nearlings.SessionManager;
import swipe.android.nearlings.jsonResponses.login.JsonLoginResponse;
import android.content.ContentValues;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.edbert.library.network.PostDataWebTask;
import com.edbert.library.network.SocketOperator;
import com.edbert.library.network.sync.JsonResponseInterface;
import com.edbert.library.utils.MapUtils;
import com.gabesechan.android.reusable.location.ProviderLocationTracker;
import com.gabesechan.android.reusable.location.ProviderLocationTracker.ProviderType;

public class NeedsDetailsRequest<T extends JsonResponseInterface> extends NearlingsRequest {

	Class classType;
	public NeedsDetailsRequest(Context c, Class classType) {
		super(c);
		this.classType = classType;
	}

	String id;

	public NeedsDetailsRequest(Context c, String id) {
		super(c);
		this.id = id;
	}

	@Override
	public JsonResponseInterface makeRequest(Bundle b) {
		Map<String, String> headers = SessionManager.getInstance(c).defaultSessionHeaders();

	/*	headers.put("username", "ramsin");
		headers.put("password", "ramsin");
		new PostDataWebTask<JsonLoginResponse>(this.getActivity(), this,
				JsonLoginResponse.class).execute(
				SessionManager.getInstance(this.getActivity()).loginURL(),
				MapUtils.mapToString(headers));
		*/
return (T) SocketOperator.getInstance(classType).getResponse(c, SessionManager.getInstance(c).exploreNeedsURL(), headers);
		//return null;
	}

	@Override
	public Class getJSONclass() {
		// TODO Auto-generated method stub
		return null;
	}

	ProviderLocationTracker tracker;

	@Override
	public boolean writeToDatabase(Context context, Object o) {
		// for now we will write random dummy stuff to the database

		ContentValues cv = new ContentValues();
		// for now we insert with

		cv.put(NeedsDetailsDatabaseHelper.COLUMN_ID, System.currentTimeMillis());
		cv.put(NeedsDetailsDatabaseHelper.COLUMN_TITLE, "Title data");

		cv.put(MessagesDatabaseHelper.COLUMN_DATE, System.currentTimeMillis());
		cv.put(NeedsDetailsDatabaseHelper.COLUMN_AUTHOR, "Author data");

		cv.put(NeedsDetailsDatabaseHelper.COLUMN_PRICE, 25.0);

		cv.put(NeedsDetailsDatabaseHelper.COLUMN_LOCATION_NAME, "Location data");

		if (tracker == null) {
			tracker = new ProviderLocationTracker(context, ProviderType.NETWORK);
		}

		LocationManager lm = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);

		Location location = lm
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		// Location location = tracker.getLocation();
		// tracker.stop();
		double longitude = location.getLongitude();
		double latitude = location.getLatitude();

		Random rand = new Random();

		int n = rand.nextInt(5000) + 1;
		double offset = n / 100000.0;

		int choice = rand.nextInt(2);
		if (choice % 2 == 0) {
			longitude += offset;
		} else {
			longitude -= offset;
		}

		int choice1 = rand.nextInt(2);
		if (choice % 2 == 0) {
			latitude += offset;
		} else {
			latitude -= offset;
		}

		cv.put(NeedsDetailsDatabaseHelper.COLUMN_LOCATION_GEOPOINT_LATITUDE,
				latitude);
		cv.put(NeedsDetailsDatabaseHelper.COLUMN_LOCATION_GEOPOINT_LONGITUDE,
				longitude);

		cv.put(NeedsDetailsDatabaseHelper.COLUMN_DESCRIPTION,
				"Description data");
		cv.put(NeedsDetailsDatabaseHelper.COLUMN_AUTHOR_IMAGE_PREVIEW_URL,
				"http://epicappsolutions.com/wp-content/uploads/2013/10/Android-icon.png");
		cv.put(NeedsDetailsDatabaseHelper.COLUMN_ID, System.currentTimeMillis());
		cv.put(NeedsDetailsDatabaseHelper.COLUMN_STATUS, Needs.NOT_ACCEPTED_YET);

		context.getContentResolver()
				.insert(NearlingsContentProvider
						.contentURIbyTableName(NeedsDetailsDatabaseHelper.TABLE_NAME),
						cv);
		return false;
	}

}