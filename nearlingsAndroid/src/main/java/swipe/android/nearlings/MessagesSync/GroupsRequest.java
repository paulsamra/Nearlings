package swipe.android.nearlings.MessagesSync;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import swipe.android.DatabaseHelpers.GroupsDatabaseHelper;
import swipe.android.nearlings.NearlingsContentProvider;
import swipe.android.nearlings.NearlingsRequest;
import swipe.android.nearlings.SessionManager;
import swipe.android.nearlings.groups.Groups;
import swipe.android.nearlings.json.groups.JsonGroupsResponse;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;

import com.edbert.library.database.DatabaseCommandManager;
import com.edbert.library.network.SocketOperator;

public class GroupsRequest extends NearlingsRequest<JsonGroupsResponse> {
	public GroupsRequest(Context c) {
		super(c);
	}

	public static final String BUNDLE_KEYWORDS = "KEYWORDS";
	public static final String BUNDLE_CATEGORY = "CATEGORY";

	public static final String BUNDLE_RADIUS = "RADIUS";
	public static final String BUNDLE_LOCATION_TYPE = "LOCATION_TYPE";
	public static final String BUNDLE_LOCATION = "LOCATION";

	public static final String BUNDLE_VISIBILITY = "VISIBILITY";
	public static final String BUNDLE_LOCATION_TYPE_ADDRESS = "address";
	public static final String BUNDLE_LOCATION_TYPE_COORDINATES = "latlng";

	public static final String BUNDLE_LOCATION_LATITUDE = "lat";
	public static final String BUNDLE_LOCATION_LONGITUDE = "lng";

	// we will have a base URL as wel
	@Override
	public JsonGroupsResponse makeRequest(Bundle b) {
		// this is a request to a url via the socket operator.

		Map<String, String> headers = SessionManager.getInstance(c)
				.defaultSessionHeaders();
		String url = SessionManager.getInstance(c).exploreGroupsURL() + "?";

		if (b.containsKey(BUNDLE_CATEGORY)) {
			url += ("&category=" + b.getString(BUNDLE_CATEGORY));
		}
		if (b.containsKey(BUNDLE_KEYWORDS)) {
			url += ("&keywords=" + b.getString(BUNDLE_KEYWORDS));
		}

		// location. We must have all these together.
		if (b.containsKey(BUNDLE_LOCATION_TYPE)
				&& b.containsKey(BUNDLE_RADIUS)
				&& (b.containsKey(BUNDLE_LOCATION) || (b
						.containsKey(this.BUNDLE_LOCATION_LATITUDE) && b
						.containsKey(this.BUNDLE_LOCATION_LONGITUDE)))) {
			String locationtype = b.getString(BUNDLE_LOCATION_TYPE);
			url += ("&location_type=" + locationtype);
			// address lat or lng
			if (locationtype.equals(this.BUNDLE_LOCATION_TYPE_COORDINATES)) {
				String latitude = b.getString(this.BUNDLE_LOCATION_LATITUDE);
				String longitude = b.getString(this.BUNDLE_LOCATION_LONGITUDE);
				url += ("&location=" + latitude + "," + longitude);
			} else {
				url += ("&location=" + b.getString(this.BUNDLE_LOCATION));
			}
			// radius
			if (b.containsKey(BUNDLE_RADIUS)) {
				url += ("&radius=" + b.getFloat(BUNDLE_RADIUS));
			} else {
				url += ("&radius=" + SessionManager.DEFAULT_SEARCH_RADIUS);
			}
		}
		if (b.containsKey(BUNDLE_VISIBILITY)) {
			url += ("&visibility=" + b.getString(BUNDLE_VISIBILITY));
		} else {
			url += ("&visibility=" + "public");
		}
		url += ("&limit=" + SessionManager.SEARCH_LIMIT);

		Object o = SocketOperator.getInstance(getJSONclass()).getResponse(c,
				url, headers);
		if (o == null)
			return null;
		return (JsonGroupsResponse) o;
	}

	@Override
	public Class getJSONclass() {
		return JsonGroupsResponse.class;
	}

	@Override
	public boolean writeToDatabase(Bundle b, Context c, JsonGroupsResponse o) {
		// for now we will write random dummy stuff to the database

		if (o == null)
			return false;

		NearlingsContentProvider.clearSingleTable(new GroupsDatabaseHelper());

		List<ContentValues> mValueList = new LinkedList<ContentValues>();
		for (int i = 0; i < o.getGroups().size(); i++) {

			Groups tempGroups = o.getGroups().get(i);
			ContentValues cv = new ContentValues();
			cv.put(GroupsDatabaseHelper.COLUMN_ID, tempGroups.getId());
			// cv.put(EventsDatabaseHelper.COLUMN_AUTHOR, tempEvent.get);
			// cv.put(EventsDatabaseHelper.COLUMN_LOCATION_NAME, tempEvent.get);

			cv.put(GroupsDatabaseHelper.COLUMN_LOCATION_LATITUDE,
					tempGroups.getLatitude());
			cv.put(GroupsDatabaseHelper.COLUMN_LOCATION_LONGITUDE,
					tempGroups.getLongitude());

			/*
			 * String myString = tempGroups.getCreated_at().getDate();
			 * DateFormat format = new SimpleDateFormat(
			 * "yyyy-MM-dd HH:mm:ss.SSSSSS"); Date date; try { date =
			 * format.parse(myString);
			 * 
			 * DateFormat df2 = new SimpleDateFormat("MM/dd/yyyy HH:mm a");
			 * String formattedDate = df2.format(date);
			 * 
			 * cv.put(GroupsDatabaseHelper.COLUMN_DATE, formattedDate); } catch
			 * (ParseException e) { e.printStackTrace(); }
			 */
			cv.put(GroupsDatabaseHelper.COLUMN_DATE, tempGroups.getCreated_at());

			cv.put(GroupsDatabaseHelper.COLUMN_VISIBILITY,
					tempGroups.getVisibility());
			cv.put(GroupsDatabaseHelper.COLUMN_NEED_COUNT,
					Integer.valueOf(tempGroups.getNeedcount()));
			cv.put(GroupsDatabaseHelper.COLUMN_MEMBER_COUNT,
					Integer.valueOf(tempGroups.getMembercount()));
			cv.put(GroupsDatabaseHelper.COLUMN_EVENT_COUNT,
					Integer.valueOf(tempGroups.getEventcount()));

			cv.put(GroupsDatabaseHelper.COLUMN_GROUP_NAME, tempGroups.getName());
			cv.put(GroupsDatabaseHelper.COLUMN_DESCRIPTION,
					tempGroups.getDescription());

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