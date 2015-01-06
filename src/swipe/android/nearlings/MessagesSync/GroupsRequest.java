package swipe.android.nearlings.MessagesSync;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import swipe.android.DatabaseHelpers.EventsDatabaseHelper;
import swipe.android.DatabaseHelpers.GroupsDatabaseHelper;
import swipe.android.DatabaseHelpers.MessagesDatabaseHelper;
import swipe.android.DatabaseHelpers.NeedsDetailsDatabaseHelper;
import swipe.android.nearlings.NearlingsContentProvider;
import swipe.android.nearlings.NearlingsRequest;
import swipe.android.nearlings.SessionManager;
import swipe.android.nearlings.groups.Groups;
import swipe.android.nearlings.json.events.Events;
import swipe.android.nearlings.json.events.JsonEventsResponse;
import swipe.android.nearlings.json.groups.JsonGroupsResponse;
import swipe.android.nearlings.jsonResponses.explore.Tasks;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.edbert.library.database.DatabaseCommandManager;
import com.edbert.library.network.SocketOperator;
import com.edbert.library.network.sync.JsonResponseInterface;

public class GroupsRequest extends NearlingsRequest<JsonGroupsResponse> {
	public GroupsRequest(Context c) {
		super(c);
	}
	
	
	public static final String BUNDLE_KEYWORDS = "KEYWORDS";
	public static final String BUNDLE_CATEGORY = "CATEGORY";

	public static final String BUNDLE_RADIUS = "RADIUS";
	public static final String BUNDLE_LOCATION_TYPE = "LOCATION_TYPE";
	public static final String BUNDLE_LOCATION = "LOCATION";

	// we will have a base URL as wel
	@Override
	public JsonGroupsResponse makeRequest(Bundle b) {
		// this is a request to a url via the socket operator.
		
		Map<String, String> headers = SessionManager.getInstance(c)
				.defaultSessionHeaders();
		String url = SessionManager.getInstance(c).exploreGroupsURL() + "?";

		if (b.containsKey(BUNDLE_RADIUS)) {
			url += ("radius=" + b.getFloat(BUNDLE_RADIUS));
		}
	
		if (b.containsKey(BUNDLE_CATEGORY)) {
			url += ("&category=" + b.getString(BUNDLE_CATEGORY));
		}
		if (b.containsKey(BUNDLE_KEYWORDS)) {
			url += ("&keywords=" + b.getString(BUNDLE_KEYWORDS));
		}

		if (b.containsKey(BUNDLE_LOCATION_TYPE)) {
			url += ("&location_type=" + b.getString(BUNDLE_LOCATION_TYPE));
		}
		if (b.containsKey(BUNDLE_LOCATION)) {
			url += ("&location=" + b.getString(BUNDLE_LOCATION));
		}
		
		Object o = SocketOperator.getInstance(getJSONclass()).getResponse(c, url,
				headers);
		if (o == null)
			return null;
		return (JsonGroupsResponse) o;
	}

	@Override
	public Class getJSONclass() {
		return JsonGroupsResponse.class;
	}

	@Override
	public boolean writeToDatabase(Context c, JsonGroupsResponse o) {
		// for now we will write random dummy stuff to the database

		if (o == null)
			return false;

		NearlingsContentProvider
				.clearSingleTable(new GroupsDatabaseHelper());
	
		List<ContentValues> mValueList = new LinkedList<ContentValues>();
		for (int i = 0; i < o.getGroups().size(); i++) {
			
			Groups tempGroups = o.getGroups().get(i);
			ContentValues cv = new ContentValues();
cv.put(GroupsDatabaseHelper.COLUMN_ID, tempGroups.getId());
	//	cv.put(EventsDatabaseHelper.COLUMN_AUTHOR, tempEvent.get);
		//cv.put(EventsDatabaseHelper.COLUMN_LOCATION_NAME, tempEvent.get);
		
		cv.put(GroupsDatabaseHelper.COLUMN_LOCATION_LATITUDE, tempGroups.getLatitude());
		cv.put(GroupsDatabaseHelper.COLUMN_LOCATION_LONGITUDE,tempGroups.getLongitude());
		
		
		String myString = tempGroups.getCreated_at().getDate();
		DateFormat format = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss.SSSSSS");
		Date date;
		try {
			date = format.parse(myString);

			DateFormat df2 = new SimpleDateFormat("MM/dd/yyyy HH:mm a");
			String formattedDate = df2.format(date);

			cv.put(GroupsDatabaseHelper.COLUMN_DATE, formattedDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		cv.put(GroupsDatabaseHelper.COLUMN_VISIBILITY, tempGroups.getVisibility());
		cv.put(GroupsDatabaseHelper.COLUMN_NEED_COUNT, Integer.valueOf(tempGroups.getNeedcount()));
		cv.put(GroupsDatabaseHelper.COLUMN_MEMBER_COUNT,Integer.valueOf(tempGroups.getMembercount()));
		cv.put(GroupsDatabaseHelper.COLUMN_EVENT_COUNT, Integer.valueOf(tempGroups.getEventcount()));
		
		cv.put(GroupsDatabaseHelper.COLUMN_GROUP_NAME, tempGroups.getName());
		cv.put(GroupsDatabaseHelper.COLUMN_DESCRIPTION, tempGroups.getDescription());

		
		cv.put(DatabaseCommandManager.SQL_INSERT_OR_REPLACE, true);
		c.getContentResolver()
				.insert(NearlingsContentProvider
						.contentURIbyTableName(GroupsDatabaseHelper.TABLE_NAME),
						cv);
		mValueList.add(cv);

	}
		ContentValues[] bulkToInsert;
		bulkToInsert = new ContentValues[mValueList.size()];
		mValueList.toArray(bulkToInsert);
		
		c.getContentResolver()
				.bulkInsert(
						NearlingsContentProvider
								.contentURIbyTableName(GroupsDatabaseHelper.TABLE_NAME),
						bulkToInsert);
		return false;
	}

	// for testing purposes
	public static int counter = 0;
}