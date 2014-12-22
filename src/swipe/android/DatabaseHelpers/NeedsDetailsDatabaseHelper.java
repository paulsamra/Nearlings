package swipe.android.DatabaseHelpers;

import com.edbert.library.database.DatabaseHelperInterface;

public class NeedsDetailsDatabaseHelper implements DatabaseHelperInterface {

	public static final String AUTHORITY = "";

	public static final String TABLE_NAME = "needs_details";

	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_DATE = "date";

	public static final String COLUMN_AUTHOR = "author";

	public static final String COLUMN_PRICE = "price";

	// this is a geopoint of osme kind.
	public static final String COLUMN_LOCATION_NAME = "location";

	public static final String COLUMN_LOCATION_GEOPOINT_LATITUDE = "location_geopoint_latitude";

	public static final String COLUMN_LOCATION_GEOPOINT_LONGITUDE = "location_geopoint_longitude";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_STATUS = "status";

	public static final String COLUMN_AUTHOR_IMAGE_PREVIEW_URL = "author_image_url";
	
	// the comments messages should be stored elsewhere.

	public static final String[] COLUMNS = { COLUMN_ID, COLUMN_TITLE,
			COLUMN_DATE, COLUMN_AUTHOR, COLUMN_PRICE, COLUMN_LOCATION_NAME,
			COLUMN_LOCATION_GEOPOINT_LATITUDE,COLUMN_LOCATION_GEOPOINT_LONGITUDE , COLUMN_DESCRIPTION,
			COLUMN_AUTHOR_IMAGE_PREVIEW_URL, COLUMN_STATUS};

	public static final String TABLE_CREATE_ROUTES = "CREATE TABLE "
			+ TABLE_NAME + "(" + COLUMN_ID + " TEXT NOT NULL primary key, "
			+ COLUMN_TITLE + " TEXT, " + COLUMN_DATE
			+" TEXT, " + COLUMN_AUTHOR + " TEXT, "
			+ COLUMN_PRICE + " FLOAT, " + COLUMN_AUTHOR_IMAGE_PREVIEW_URL + " TEXT, "
			+ COLUMN_STATUS + " TEXT, "
			
			+ COLUMN_LOCATION_GEOPOINT_LATITUDE + " DOUBLE, " + COLUMN_LOCATION_GEOPOINT_LONGITUDE + " DOUBLE, "
			+ COLUMN_DESCRIPTION +" TEXT, " + COLUMN_LOCATION_NAME
			+ " TEXT);";

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
