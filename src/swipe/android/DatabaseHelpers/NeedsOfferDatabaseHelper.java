package swipe.android.DatabaseHelpers;

import com.edbert.library.database.DatabaseHelperInterface;

public class NeedsOfferDatabaseHelper implements DatabaseHelperInterface {

	public static final String AUTHORITY = "";

	public static final String TABLE_NAME = "needs_offers";

	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_MESSAGE = "message";
	public static final String COLUMN_CREATED_BY = "created_by";
	public static final String COLUMN_CHANGED_BY = "changed_by";
	public static final String COLUMN_STATUS = "status";
	public static final String COLUMN_OFFER_PRICE = "offerprice";
	public static final String COLUMN_CREATED_AT = "created_at";
	public static final String COLUMN_USERNAME = "username";
	public static final String COLUMN_THUMBNAIL = "thumbnail";

	public static final String[] COLUMNS = { COLUMN_THUMBNAIL, COLUMN_USERNAME,
			COLUMN_CREATED_AT, COLUMN_OFFER_PRICE, COLUMN_STATUS, COLUMN_ID,
			COLUMN_MESSAGE, COLUMN_CREATED_BY, };

	public static final String TABLE_CREATE_ROUTES = "CREATE TABLE "
			+ TABLE_NAME + "(" + COLUMN_ID + " TEXT NOT NULL primary key, "
			+ COLUMN_MESSAGE + " TEXT, " + COLUMN_CREATED_BY + " INTEGER, "
			+ COLUMN_STATUS + " TEXT, " + COLUMN_CHANGED_BY + " INTEGER"
			+ COLUMN_OFFER_PRICE + " INTEGER" + COLUMN_CREATED_AT + " BIGINT"
			+ COLUMN_USERNAME + " TEXT" + COLUMN_THUMBNAIL + " TEXT);";

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
