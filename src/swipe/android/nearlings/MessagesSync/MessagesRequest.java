package swipe.android.nearlings.MessagesSync;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import swipe.android.DatabaseHelpers.MessagesDatabaseHelper;
import swipe.android.DatabaseHelpers.NeedsDetailsDatabaseHelper;
import swipe.android.nearlings.NearlingsContentProvider;
import swipe.android.nearlings.NearlingsRequest;
import swipe.android.nearlings.SessionManager;
import swipe.android.nearlings.json.alerts.Alerts;
import swipe.android.nearlings.json.alerts.JsonMessagesResponse;
import swipe.android.nearlings.jsonResponses.explore.JsonExploreResponse;
import swipe.android.nearlings.jsonResponses.explore.Tasks;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.edbert.library.network.SocketOperator;
import com.edbert.library.network.sync.JsonResponseInterface;

public class MessagesRequest extends NearlingsRequest <JsonMessagesResponse>{
	public MessagesRequest(Context c) {
		super(c);
		// TODO Auto-generated constructor stub
	}

	public static final String SEARCH_PARAMS_TAG = "SEARCH_PARAMS_TAG";

	// we will have a base URL as wel
	@Override
	public JsonMessagesResponse makeRequest(Bundle b) {
	Map<String, String> headers = SessionManager.getInstance(c)
				.defaultSessionHeaders();
		String url = SessionManager.getInstance(c).alertsURL();
Log.d("URL", url);
		Object o = SocketOperator.getInstance(JsonMessagesResponse.class).getResponse(c, url,
				headers);
		if (o == null)
			return null;

		return (JsonMessagesResponse) o;
	
	}

	@Override
	public Class getJSONclass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean writeToDatabase(Context c, JsonMessagesResponse o) {
		// for now we will write random dummy stuff to the database

		if (o == null)
			return false;

		NearlingsContentProvider
				.clearSingleTable(new MessagesDatabaseHelper());
		List<ContentValues> mValueList = new LinkedList<ContentValues>();
		for (int i = 0; i < o.getAlerts().size(); i++) {
			
			
Alerts a = o.getAlerts().get(i);
			ContentValues cv = new ContentValues();

			cv.put(MessagesDatabaseHelper.COLUMN_ID, a.getMessage_id());
			cv.put(MessagesDatabaseHelper.COLUMN_MESSAGE, a.getMessage());
			cv.put(MessagesDatabaseHelper.COLUMN_DATE, Long.valueOf(a.getTimestamp()));
	
			cv.put(MessagesDatabaseHelper.COLUMN_UNREAD, "true");


			c.getContentResolver()
					.insert(NearlingsContentProvider
							.contentURIbyTableName(MessagesDatabaseHelper.TABLE_NAME),
							cv);

			
			
			mValueList.add(cv);

		}
		ContentValues[] bulkToInsert;
		bulkToInsert = new ContentValues[mValueList.size()];
		mValueList.toArray(bulkToInsert);
		c.getContentResolver()
				.bulkInsert(
						NearlingsContentProvider
								.contentURIbyTableName(MessagesDatabaseHelper.TABLE_NAME),
						bulkToInsert);

		return true;
		
	}

	// for testing purposes
	public static int counter = 0;
}