package swipe.android.DatabaseHelpers;

import com.edbert.library.database.DatabaseHelperInterface;

public class NeedsDetailsDatabaseHelper implements DatabaseHelperInterface {

	public static final String AUTHORITY = "";

	public static final String TABLE_NAME = "needs_details";

	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_DUE_DATE = "due_date";

	public static final String COLUMN_AUTHOR = "author";

	public static final String COLUMN_PRICE = "price";

	// this is a geopoint of osme kind.
	public static final String COLUMN_LOCATION_NAME = "location";

	public static final String COLUMN_LOCATION_GEOPOINT_LATITUDE = "location_geopoint_latitude";

	public static final String COLUMN_LOCATION_GEOPOINT_LONGITUDE = "location_geopoint_longitude";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_STATUS = "status";

	public static final String COLUMN_AUTHOR_IMAGE_PREVIEW_URL = "author_image_url";

	public static final String COLUMN_OFFER_COUNT = "offer_count";
	// the comments messages should be stored elsewhere.
	public static final String COLUMN_CATEGORY = "COLUMN_CATEGORY";
	public static final String COLUMN_REWARD = "COLUMN_REWARD";
	public static final String COLUMN_RATE_TYPE = "COLUMN_RATE_TYPE";
	public static final String COLUMN_STARTING = "COLUMN_STARTING";
	public static final String COLUMN_ENDING = "COLUMN_ENDING";
	public static final String COLUMN_GROUP_ID = "COLUMN_GROUP_ID";
	public static final String COLUMN_CREATED_BY = "COLUMN_CREATED_BY";
	public static final String COLUMN_ASSIGNED_TO = "COLUMN_ASSIGNED_TO";
	public static final String COLUMN_COMMENT_COUNT = "COLUMN_COMMENT_COUNT";
	public static final String COLUMN_ADDRESS_1 = "COLUMN_ADDRESS_1";
	public static final String COLUMN_ADDRESS_2 = "COLUMN_ADDRESS_2";
	public static final String COLUMN_CITY = "COLUMN_CITY";
	public static final String COLUMN_STATE = "COLUMN_STATE";
	public static final String COLUMN_ZIP = "COLUMN_ZIP";
	public static final String[] COLUMNS = { COLUMN_STARTING, COLUMN_ENDING,
			COLUMN_GROUP_ID, COLUMN_CREATED_BY, COLUMN_ASSIGNED_TO,
			COLUMN_COMMENT_COUNT, COLUMN_ADDRESS_1, COLUMN_ADDRESS_2,
			COLUMN_CITY, COLUMN_STATE, COLUMN_ZIP, COLUMN_ID, COLUMN_TITLE,
			COLUMN_DUE_DATE, COLUMN_AUTHOR, COLUMN_PRICE, COLUMN_LOCATION_NAME,
			COLUMN_LOCATION_GEOPOINT_LATITUDE,
			COLUMN_LOCATION_GEOPOINT_LONGITUDE, COLUMN_DESCRIPTION,
			COLUMN_AUTHOR_IMAGE_PREVIEW_URL, COLUMN_STATUS, COLUMN_OFFER_COUNT,
			COLUMN_CATEGORY, COLUMN_RATE_TYPE, COLUMN_REWARD };

	public static final String TABLE_CREATE_ROUTES = "CREATE TABLE "
			+ TABLE_NAME + "(" + COLUMN_ID + " TEXT NOT NULL primary key, "
			+ COLUMN_TITLE + " TEXT, " + COLUMN_DUE_DATE + " LONG, "
			+ COLUMN_AUTHOR + " TEXT, " + COLUMN_PRICE + " FLOAT, "
			+ COLUMN_AUTHOR_IMAGE_PREVIEW_URL + " TEXT, " + COLUMN_STATUS
			+ " TEXT, " + COLUMN_CATEGORY + " TEXT, " + COLUMN_REWARD
			+ " INTEGER, " + COLUMN_RATE_TYPE + " TEXT, " + COLUMN_STARTING
			+ " BIGINT, " + COLUMN_ENDING + " BIGINT, " + COLUMN_GROUP_ID
			+ " INTEGER, " + COLUMN_CREATED_BY + " INTEGER, "
			+ COLUMN_ASSIGNED_TO + " INTEGER, " + COLUMN_COMMENT_COUNT
			+ " INTEGER, " + COLUMN_ADDRESS_1 + " TEXT, " + COLUMN_ADDRESS_2
			+ " TEXT, " + COLUMN_OFFER_COUNT + " INTEGER,"
			+ COLUMN_LOCATION_GEOPOINT_LATITUDE + " DOUBLE, "
			+ COLUMN_LOCATION_GEOPOINT_LONGITUDE + " DOUBLE, " + COLUMN_CITY
			+ " TEXT, " + COLUMN_STATE + " TEXT, " + COLUMN_ZIP + " INTEGER, "
			+ COLUMN_DESCRIPTION + " TEXT, " + COLUMN_LOCATION_NAME + " TEXT);";

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
