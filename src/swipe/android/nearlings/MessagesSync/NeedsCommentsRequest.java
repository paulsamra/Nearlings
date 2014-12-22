package swipe.android.nearlings.MessagesSync;

import java.util.Calendar;
import java.util.Random;

import swipe.android.DatabaseHelpers.MessagesDatabaseHelper;
import swipe.android.DatabaseHelpers.NeedsCommentsDatabaseHelper;
import swipe.android.DatabaseHelpers.NeedsDetailsDatabaseHelper;
import swipe.android.nearlings.NearlingsContentProvider;
import swipe.android.nearlings.NearlingsRequest;
import android.content.ContentValues;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.edbert.library.network.sync.JsonResponseInterface;
import com.gabesechan.android.reusable.location.ProviderLocationTracker;
import com.gabesechan.android.reusable.location.ProviderLocationTracker.ProviderType;

public class NeedsCommentsRequest extends NearlingsRequest {

	public NeedsCommentsRequest(Context c) {
		super(c);
	}

	String id;

	public NeedsCommentsRequest(Context c, String id) {
		super(c);
		this.id = id;
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

	ProviderLocationTracker tracker;

	@Override
	public boolean writeToDatabase(Context context, Object o) {
		// for now we will write random dummy stuff to the database

		ContentValues cv = new ContentValues();
		// for now we insert with

		cv.put(NeedsCommentsDatabaseHelper.COLUMN_ID,
				System.currentTimeMillis());
		cv.put(NeedsCommentsDatabaseHelper.COLUMN_DATE,
				System.currentTimeMillis());
		cv.put(NeedsCommentsDatabaseHelper.COLUMN_AUTHOR, "BOB");
		cv.put(NeedsCommentsDatabaseHelper.COLUMN_AUTHOR_IMAGE_PREVIEW_URL,
				"http://epicappsolutions.com/wp-content/uploads/2013/10/Android-icon.png");

		cv.put(NeedsCommentsDatabaseHelper.COLUMN_MESSAGE, "Message");

		context.getContentResolver()
				.insert(NearlingsContentProvider
						.contentURIbyTableName(NeedsCommentsDatabaseHelper.TABLE_NAME),
						cv);
		return false;
	}

}