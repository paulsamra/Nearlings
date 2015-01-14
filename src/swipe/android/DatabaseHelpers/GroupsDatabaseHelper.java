package swipe.android.DatabaseHelpers;

import com.edbert.library.database.DatabaseHelperInterface;

public class GroupsDatabaseHelper implements DatabaseHelperInterface {

	public static final String AUTHORITY = "";

	public static final String TABLE_NAME = "groups";

	public static final String COLUMN_ID = "_id";

	public static final String COLUMN_GROUP_NAME = "group_name";
	
	public static final String COLUMN_LOCATION_LATITUDE = "latitude";
	public static final String COLUMN_LOCATION_LONGITUDE = "longitude";

	
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_VISIBILITY = "visibility";
	public static final String COLUMN_MEMBER_COUNT = "member_count";
	public static final String COLUMN_EVENT_COUNT = "event_count";
	public static final String COLUMN_NEED_COUNT = "need_count";
	
	public static final String COLUMN_DATE = "COLUMN_DATE";
	
	
	public static final String[] COLUMNS = { COLUMN_ID, COLUMN_DESCRIPTION,
			COLUMN_LOCATION_LATITUDE, COLUMN_LOCATION_LONGITUDE,
			COLUMN_DATE,COLUMN_GROUP_NAME,COLUMN_VISIBILITY,COLUMN_MEMBER_COUNT,
			COLUMN_DESCRIPTION, COLUMN_DESCRIPTION, COLUMN_EVENT_COUNT,COLUMN_NEED_COUNT };

	public static final String TABLE_CREATE_ROUTES = "CREATE TABLE "
			+ TABLE_NAME + "(" + COLUMN_ID + " TEXT NOT NULL primary key, "
			 + COLUMN_LOCATION_LATITUDE
			+ " DOUBLE, " + COLUMN_LOCATION_LONGITUDE + " DOUBLE, "+
			COLUMN_VISIBILITY + " TEXT NOT NULL, " 
			+COLUMN_DATE+ " TEXT NOT NULL, " 
			+ COLUMN_MEMBER_COUNT + " INTEGER, " 
			+ COLUMN_EVENT_COUNT + " INTEGER, " 
			+ COLUMN_NEED_COUNT + " INTEGER, " 
			+COLUMN_GROUP_NAME + " TEXT NOT NULL, " 
			+ COLUMN_DESCRIPTION +  " TEXT NOT NULL);";

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
