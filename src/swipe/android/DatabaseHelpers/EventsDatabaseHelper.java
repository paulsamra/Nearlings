package swipe.android.DatabaseHelpers;

import com.edbert.library.database.DatabaseHelperInterface;

public class EventsDatabaseHelper implements DatabaseHelperInterface {

	public static final String AUTHORITY = "";

	public static final String TABLE_NAME = "events";

	public static final String COLUMN_ID = "_id";

	public static final String COLUMN_EVENT_NAME = "event_name";
	public static final String COLUMN_LOCATION_LATITUDE = "latitude";
	public static final String COLUMN_LOCATION_LONGITUDE = "longitude";

	public static final String COLUMN_TIME_OF_EVENT = "TIME_OF_EVENT";

	public static final String COLUMN_DATE_OF_EVENT = "COLUMN_TIME_SENT";
	// public static final String COLUMN_RSVP_STATUS = "RSVP_STATUS";
	public static final String COLUMN_RSVP_COUNT = "RSVP_COUNT";

	public static final String COLUMN_FEE = "FEE";
	public static final String COLUMN_VISIBILITY = "VISIBILITY";
	public static final String COLUMN_CATEGORY = "CATEGORY";
	public static final String COLUMN_DESCRIPTION = "DESCRIPTION";

	public static final String[] COLUMNS = { COLUMN_ID,
			COLUMN_LOCATION_LATITUDE, COLUMN_LOCATION_LONGITUDE,
			COLUMN_DATE_OF_EVENT, COLUMN_RSVP_COUNT, COLUMN_TIME_OF_EVENT,
			COLUMN_EVENT_NAME, COLUMN_FEE, COLUMN_VISIBILITY,
			COLUMN_DESCRIPTION, COLUMN_CATEGORY };

	public static final String TABLE_CREATE_ROUTES = "CREATE TABLE "
			+ TABLE_NAME + "(" + COLUMN_ID + " TEXT NOT NULL primary key, "
			+ COLUMN_LOCATION_LATITUDE + " DOUBLE, " + COLUMN_RSVP_COUNT
			+ " INTEGER, " + COLUMN_DATE_OF_EVENT + " TEXT NOT NULL, "

			+ COLUMN_CATEGORY + " TEXT NOT NULL, " + COLUMN_FEE + " DOUBLE, "
			+ COLUMN_DESCRIPTION + " TEXT NOT NULL, " + COLUMN_VISIBILITY
			+ " TEXT NOT NULL, "

			+ COLUMN_LOCATION_LONGITUDE + " DOUBLE, " + COLUMN_EVENT_NAME
			+ " TEXT NOT NULL, " + COLUMN_TIME_OF_EVENT + " TEXT NOT NULL);";

	@Override
	public String getTableName() {
		return TABLE_NAME;
	}

	@Override
	public String getColumnID() {
		return COLUMN_ID;
	}

	@Override
	public String getCreateTableCommand() {
		return TABLE_CREATE_ROUTES;
	}

	@Override
	public String[] getColumns() {
		return COLUMNS;
	}
}
