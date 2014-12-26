package swipe.android.nearlings;

/**
 * Abstracted database command manager. We use this if we want to do speicfic operations with
 * a table
 */

import java.util.Arrays;
import java.util.HashSet;

import com.edbert.library.database.DatabaseCommandManager;
import com.edbert.library.database.DatabaseHelperInterface;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class NearlingsContentProvider extends ContentProvider {

	// database
	// private AlertDBHelper database;
	public static  DBHelper dbHelper;
	private static final int DATABASE_VERSION = 1;

	// used for the UriMacher
	private static final String DATABASE_NAME = "Nearlings.db";

	public static final String AUTHORITY = "swipe.android.nearlings.NearlingsContentProvider";

	private static final String BASE_PATH = "nearlings";


	private static SQLiteDatabase database;

	// class that creates and manages the provider's database
	public static class DBHelper extends SQLiteOpenHelper {

		public DBHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			

		}

		@Override
		public void onCreate(SQLiteDatabase db) {

			DatabaseCommandManager.deleteAllTables(db);
			DatabaseCommandManager.createAllTables(db);

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			//we want to put all the stuff into a temporary table and then move it
			

			Log.w(DBHelper.class.getName(), "Upgrading database from version "
					+ oldVersion + " to " + newVersion
					+ ". Old data will be destroyed");
			//DatabaseCommandManager.applyCommandtoAllTables(db, "DROP TABLE IF EXISTS ", "");
			DatabaseCommandManager.upgradeAll(db);
		}

	}
	public static synchronized DBHelper getDBHelperInstance(Context context) {
	    if (dbHelper == null)
	    	dbHelper = new DBHelper(context);
	    return dbHelper;
	  }
	@Override
	public boolean onCreate() {

		// database = new AlertDBHelper(getContext());
		Context context = getContext();
		dbHelper = new DBHelper(context);
		database = dbHelper.getWritableDatabase();
		// TableCommandManager.register(database);
		if (database == null)
			return false;
		else
			return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		// check if the caller has requested a column which does not exists
		checkColumns(projection, uri);

		// Set the table

		SQLiteQueryBuilder queryBuilder = DatabaseCommandManager.getInstance(
				this.getContext(), AUTHORITY).queryAllTables(uri);

		SQLiteDatabase db = dbHelper.getWritableDatabase();

		Cursor cursor = queryBuilder.query(db, projection, selection,
				selectionArgs, null, null, sortOrder);
		// make sure that potential listeners are getting notified
		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		return cursor;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	public static final String SQL_INSERT_OR_REPLACE = DatabaseCommandManager.SQL_INSERT_OR_REPLACE;

	@Override
	public Uri insert(Uri uri, ContentValues values) {

		SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
		long id = DatabaseCommandManager.getInstance(this.getContext(),
				AUTHORITY).insert(uri, values, sqlDB);
		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.parse(BASE_PATH + "/" + id);
		
	}

	@Override
	public int bulkInsert(Uri uri, ContentValues[] values){

		SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
		long id = DatabaseCommandManager.getInstance(this.getContext(),
				AUTHORITY).bulkInsert(uri, values, sqlDB);
		//getContext().getContentResolver().notifyChange(uri, null);
		  getContext().getContentResolver().notifyChange(uri, null);
	        return values.length;
	//	return Uri.parse(BASE_PATH + "/" + id);
		
	}
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
		int rowsDeleted = DatabaseCommandManager.getInstance(this.getContext(),
				AUTHORITY).delete(uri, selection, selectionArgs, sqlDB);

		getContext().getContentResolver().notifyChange(uri, null);
		return rowsDeleted;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
		int rowsUpdated = DatabaseCommandManager.getInstance(this.getContext(),
				AUTHORITY).update(uri, values, selection, selectionArgs, sqlDB);
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
	}

	private void checkColumns(String[] projection, Uri uri) {
		String[] available = DatabaseCommandManager.getInstance(
				this.getContext(), AUTHORITY).checkColumns(uri);

		if (projection != null) {
			HashSet<String> requestedColumns = new HashSet<String>(
					Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(
					Arrays.asList(available));
			// check if all columns which are requested are available
			if (!availableColumns.containsAll(requestedColumns)) {
				throw new IllegalArgumentException(
						"Unknown columns in projection");
			}
		}
	}

	public void clearAllTables() {
		DatabaseCommandManager.applyCommandtoAllTables(database,
				"DROP TABLE IF EXISTS ", "");
	}

	public void addAllTables(ContentResolver p) {
		DatabaseCommandManager.createAllTables(database);
	}
	public static Uri contentURIbyTableName(String tableName) {
		return Uri.parse("content://" + AUTHORITY + "/"
				+ tableName);
	}
	public static void clearSingleTable(DatabaseHelperInterface tableName){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		  db.execSQL("delete from " + tableName.getTableName()); //delete all rows in a table

	}
}