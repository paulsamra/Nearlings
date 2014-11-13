package swipe.android.nearlings.MessagesSync;

import java.util.Calendar;

import swipe.android.DatabaseHelpers.MessagesDatabaseHelper;
import swipe.android.nearlings.NearlingsContentProvider;
import swipe.android.nearlings.NearlingsRequest;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;

import com.edbert.library.network.sync.JsonResponseInterface;

public class FollowersRequest extends NearlingsRequest {

	public FollowersRequest(Context c) {
		super(c);
		// TODO Auto-generated constructor stub
	}

	@Override
	public JsonResponseInterface makeRequest(Bundle b) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class getJSONclass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean writeToDatabase(Context c, Object o) {
		// for now we will write random dummy stuff to the database
	
		ContentValues cv = new ContentValues();

	/*	cv.put(FollowersDatabaseHelper.-+0,0
				98/9+"Author");
		cv.put(MessagesDatabaseHelper.COLUMN_DATE, System.currentTimeMillis());
		cv.put(MessagesDatabaseHelper.COLUMN_ID, System.currentTimeMillis());
		cv.put(MessagesDatabaseHelper.COLUMN_MESSAGE, "Message " + counter);

		cv.put(MessagesDatabaseHelper.COLUMN_UNREAD, "true");

		cv.put(MessagesDatabaseHelper.COLUMN_TRIGGERED_ON_FORMATTED,
				"Formatted time");

		c.getContentResolver()
				.insert(NearlingsContentProvider
						.contentURIbyTableName(MessagesDatabaseHelper.TABLE_NAME),
						cv);
*/
		counter++;
		return false;
	}

	// for testing purposes
	public static int counter = 0;
}