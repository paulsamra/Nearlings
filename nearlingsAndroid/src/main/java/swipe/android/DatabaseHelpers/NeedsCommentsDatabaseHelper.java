package swipe.android.DatabaseHelpers;

import com.edbert.library.database.DatabaseHelperInterface;

public class NeedsCommentsDatabaseHelper implements DatabaseHelperInterface {

	public static final String AUTHORITY = "";

	public static final String TABLE_NAME = "needs_comments";

	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_DATE = "date";
	public static final String COLUMN_AUTHOR = "author";

	public static final String COLUMN_AUTHOR_IMAGE_PREVIEW_URL = "author_image_preview";
	
	public static final String COLUMN_MESSAGE = "message";

	// the comments messages should be stored elsewhere.

	public static final String[] COLUMNS = { COLUMN_ID,
			COLUMN_DATE, COLUMN_AUTHOR, COLUMN_MESSAGE, COLUMN_AUTHOR_IMAGE_PREVIEW_URL};

	public static final String TABLE_CREATE_ROUTES = "CREATE TABLE "
			+ TABLE_NAME + "(" + COLUMN_ID + " TEXT NOT NULL primary key, "
			+ COLUMN_AUTHOR + " TEXT NOT NULL, " + COLUMN_DATE
			+ " TEXT NOT NULL, " + COLUMN_AUTHOR_IMAGE_PREVIEW_URL + " TEXT NOT NULL, "
			+ COLUMN_MESSAGE + " TEXT NOT NULL);";

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
