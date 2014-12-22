package swipe.android.nearlings.MessagesSync;

import java.util.Calendar;

import swipe.android.DatabaseHelpers.EventsDatabaseHelper;
import swipe.android.DatabaseHelpers.MessagesDatabaseHelper;
import swipe.android.nearlings.NearlingsContentProvider;
import swipe.android.nearlings.NearlingsRequest;
import swipe.android.nearlings.SessionManager;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;

import com.edbert.library.network.sync.JsonResponseInterface;

public class EventsRequest extends NearlingsRequest {
	public EventsRequest(Context c) {
		super(c);
		// TODO Auto-generated constructor stub
	}

	public static final String SEARCH_PARAMS_TAG = "SEARCH_PARAMS_TAG";

	// we will have a base URL as wel
	@Override
	public JsonResponseInterface makeRequest(Bundle b) {
		// this is a request to a url via the socket operator.
		return null;
	}

	@Override
	public Class getJSONclass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean writeToDatabase(Context c, Object o) {
		// for now we will write random dummy stuff to the database

		ContentValues cv = new ContentValues();
cv.put(EventsDatabaseHelper.COLUMN_ID, System.currentTimeMillis());
		cv.put(EventsDatabaseHelper.COLUMN_AUTHOR, "Author");
		cv.put(EventsDatabaseHelper.COLUMN_LOCATION_NAME, "UCSD");
		
		cv.put(EventsDatabaseHelper.COLUMN_LOCATION_LATITUDE, 32.8810);
		cv.put(EventsDatabaseHelper.COLUMN_LOCATION_LONGITUDE, 117.2380);

		cv.put(EventsDatabaseHelper.COLUMN_TIME_OF_EVENT, System.currentTimeMillis());

		cv.put(EventsDatabaseHelper.COLUMN_TIME_SENT,
				System.currentTimeMillis());
		cv.put(EventsDatabaseHelper.COLUMN_RSVP_STATUS,
				NearlingEvent.RSVP_DONT_KNOW);
		cv.put(EventsDatabaseHelper.COLUMN_EVENT_NAME,
				"UCSD EVENT");
		c.getContentResolver()
				.insert(NearlingsContentProvider
						.contentURIbyTableName(EventsDatabaseHelper.TABLE_NAME),
						cv);

		counter++;
		return false;
	}

	// for testing purposes
	public static int counter = 0;
}