package swipe.android.DatabaseHelpers;

import com.edbert.library.database.DatabaseHelperInterface;

public class UserReviewDatabaseHelper implements DatabaseHelperInterface {

	public static final String AUTHORITY = "";

	public static final String TABLE_NAME = "user_review";
//TODO
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_CREATED_BY = "created_by";
	public static final String COLUMN_NEED_ID = "need_id";
	public static final String COLUMN_EFFORT_RATING = "effort_rating";
	public static final String COLUMN_QUALITY_RATING = "quality_rating";
	public static final String COLUMN_TIMELINESS_RATING = "timeliness_rating";
	public static final String COLUMN_MESSAGE = "MESSAGE";
	
	public static final String[] COLUMNS = { COLUMN_ID, COLUMN_CREATED_BY,
		COLUMN_NEED_ID, COLUMN_EFFORT_RATING, COLUMN_TIMELINESS_RATING, COLUMN_MESSAGE, COLUMN_QUALITY_RATING };

	public static final String TABLE_CREATE_ROUTES = "CREATE TABLE "
			+ TABLE_NAME + "(" + COLUMN_ID + " TEXT NOT NULL primary key, "
			+ COLUMN_CREATED_BY + " TEXT NOT NULL, "
			+ COLUMN_NEED_ID + " INTEGER, "
			+ COLUMN_EFFORT_RATING + " INTEGER, "
			+ COLUMN_TIMELINESS_RATING + " INTEGER, "
			+ COLUMN_QUALITY_RATING + " INTEGER, "
			+  COLUMN_MESSAGE + " TEXT);";

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
