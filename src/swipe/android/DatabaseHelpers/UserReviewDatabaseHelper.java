package swipe.android.DatabaseHelpers;

import com.edbert.library.database.DatabaseHelperInterface;

public class UserReviewDatabaseHelper implements DatabaseHelperInterface {

	public static final String AUTHORITY = "";

	public static final String TABLE_NAME = "user_review";
//TODO
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TIME = "time";
	public static final String COLUMN_AUTHOR = "author";
	public static final String COLUMN_RATING = "rating";
	public static final String COLUMN_REVIEW = "review";

	public static final String[] COLUMNS = { COLUMN_ID, COLUMN_TIME,
		COLUMN_AUTHOR, COLUMN_RATING, COLUMN_REVIEW };

	public static final String TABLE_CREATE_ROUTES = "CREATE TABLE "
			+ TABLE_NAME + "(" + COLUMN_ID + " TEXT NOT NULL primary key, "
			+ COLUMN_RATING + " FLOAT, " + COLUMN_TIME + " BIGINT, "
			+ COLUMN_REVIEW + " TEXT, " + COLUMN_AUTHOR + " TEXT);";

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
