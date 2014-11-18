package swipe.android.nearlings.jsonResponses.register;
import swipe.android.DatabaseHelpers.MessagesDatabaseHelper;
import swipe.android.nearlings.NearlingsContentProvider;
import android.content.ContentValues;
import android.database.Cursor;

public class Message{
	String author, date, id, message, time;
	boolean unread;
	public static ContentValues fromCursor(Cursor cursor){
		ContentValues cv = new ContentValues();
		int sender_index = cursor
				.getColumnIndexOrThrow(MessagesDatabaseHelper.COLUMN_AUTHOR);
		int time_index = cursor
				.getColumnIndexOrThrow(MessagesDatabaseHelper.COLUMN_DATE);
		int message_index = cursor
				.getColumnIndexOrThrow(MessagesDatabaseHelper.COLUMN_MESSAGE);
		int id_index = cursor
				.getColumnIndexOrThrow(MessagesDatabaseHelper.COLUMN_ID);

		int unread_index = cursor
				.getColumnIndexOrThrow(MessagesDatabaseHelper.COLUMN_UNREAD);
		int formatted_time_index = cursor
				.getColumnIndexOrThrow(MessagesDatabaseHelper.COLUMN_TRIGGERED_ON_FORMATTED);
		cv.put(MessagesDatabaseHelper.COLUMN_AUTHOR, cursor.getString(sender_index));
		cv.put(MessagesDatabaseHelper.COLUMN_DATE, cursor.getString(time_index));
		cv.put(MessagesDatabaseHelper.COLUMN_ID, cursor.getString(id_index));
		cv.put(MessagesDatabaseHelper.COLUMN_MESSAGE, cursor.getString(message_index));

		cv.put(MessagesDatabaseHelper.COLUMN_UNREAD, cursor.getString(unread_index));

		cv.put(MessagesDatabaseHelper.COLUMN_TRIGGERED_ON_FORMATTED,
				cursor.getString(formatted_time_index));
		return cv;

	}
	
}