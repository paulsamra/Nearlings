package swipe.android.nearlings.MessagesSync;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import swipe.android.DatabaseHelpers.EventsDatabaseHelper;
import swipe.android.DatabaseHelpers.GroupsDatabaseHelper;
import swipe.android.nearlings.BaseContainerFragment;
import swipe.android.nearlings.NearlingsContentProvider;
import swipe.android.nearlings.NearlingsRequest;
import swipe.android.nearlings.SessionManager;
import swipe.android.nearlings.json.events.Events;
import swipe.android.nearlings.json.events.JsonEventsResponse;
import swipe.android.nearlings.sync.NearlingsSyncAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.edbert.library.database.DatabaseCommandManager;
import com.edbert.library.network.SocketOperator;

public class EventsDetailsRequest extends NearlingsRequest<JsonEventsResponse> {
	public EventsDetailsRequest(Context c) {
		super(c);
	}

	public static final String SEARCH_PARAMS_TAG = "SEARCH_PARAMS_TAG";
	public static final String BUNDLE_KEYWORDS = "KEYWORDS";
	public static final String BUNDLE_VISIBILITY = "VISIBILITY";
	public static final String BUNDLE_START_TIME = "START_TIME";
	public static final String BUNDLE_CATEGORY = "CATEGORY";
	public static final String BUNDLE_TIME_START = "TIME_START";

	public static final String BUNDLE_RADIUS = "RADIUS";
	public static final String BUNDLE_LOCATION_TYPE = "LOCATION_TYPE";
	public static final String BUNDLE_LOCATION = "LOCATION";

	public static final String BUNDLE_LOCATION_TYPE_ADDRESS = "address";
	public static final String BUNDLE_LOCATION_TYPE_COORDINATES = "latlng";

	public static final String BUNDLE_LOCATION_LATITUDE = "lat";
	public static final String BUNDLE_LOCATION_LONGITUDE = "lng";

	// we will have a base URL as wel
	@Override
	public JsonEventsResponse makeRequest(Bundle b) {
		// this is a request to a url via the socket operator.

		Map<String, String> headers = SessionManager.getInstance(c)
				.defaultSessionHeaders();
		String url = SessionManager.getInstance(c).exploreEventsURL() + "?";

		url += ("&limit=" + SessionManager.SEARCH_LIMIT);

		if (b.containsKey(BUNDLE_CATEGORY)) {
			url += ("&category=" + b.getString(BUNDLE_CATEGORY));
		}
	
		if (b.containsKey(BUNDLE_KEYWORDS)) {
			url += ("&keywords=" + b.getString(BUNDLE_KEYWORDS));
		}

		/*if (b.containsKey(BUNDLE_TIME_START)) {
			url += ("&time_start=" + b.getLong(BUNDLE_TIME_START));
		}*/
		if(b.containsKey(BUNDLE_VISIBILITY)){
			url += ("&visibility=" + b.getString(BUNDLE_VISIBILITY));
		}else{
				url += ("&visibility=" + "public");
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


		int page_number = 1;
		if(b.getInt(BaseContainerFragment.STATUS) == BaseContainerFragment.is_reload_and_blank){
			url +=( "&limit=" + (SessionManager.SEARCH_LIMIT));
		}else if(b.getInt(BaseContainerFragment.STATUS) == BaseContainerFragment.is_maintain){
			page_number =  (b.getInt(NearlingsSyncAdapter.LIMIT) / (SessionManager.SEARCH_LIMIT));
			url +=( "&page=" +page_number);
			url += ("&limit="+ (SessionManager.SEARCH_LIMIT));
		}else if(b.getInt(BaseContainerFragment.STATUS) == BaseContainerFragment.is_loadMore){
			Log.d("IS_LOAD_MORE", String.valueOf(b.getInt(NearlingsSyncAdapter.LIMIT)));
			page_number =  (b.getInt(NearlingsSyncAdapter.LIMIT) / (SessionManager.SEARCH_LIMIT))+1;
			url +=( "&page=" +page_number);
		}else{
			url +=( "&limit=" + (SessionManager.SEARCH_LIMIT));
		}
		Object o = SocketOperator.getInstance(getJSONclass()).getResponse(c,
				url, headers);
		if (o == null)
			return null;
		return (JsonEventsResponse) o;
	}

	@Override
	public Class getJSONclass() {
		// TODO Auto-generated method stub
		return JsonEventsResponse.class;
	}

	@Override
	public boolean writeToDatabase(Bundle extras, Context c,
			JsonEventsResponse o) {
		// for now we will write random dummy stuff to the database

		if (o == null)
			return false;
		if(extras.getBoolean(NearlingsSyncAdapter.CLEAR_DB)) {
			NearlingsContentProvider
					.clearSingleTable(new EventsDatabaseHelper());
		}

		List<ContentValues> mValueList = new LinkedList<ContentValues>();
		for (int i = 0; i < o.getEvents().size(); i++) {

			Events tempEvent = o.getEvents().get(i);
			ContentValues cv = new ContentValues();
			cv.put(EventsDatabaseHelper.COLUMN_ID, tempEvent.getId());
			// cv.put(EventsDatabaseHelper.COLUMN_AUTHOR, tempEvent.get);
			// cv.put(EventsDatabaseHelper.COLUMN_LOCATION_NAME, tempEvent.get);


			cv.put(EventsDatabaseHelper.COLUMN_USERNAME,
					tempEvent.getCreator_username());
			cv.put(EventsDatabaseHelper.COLUMN_LOCATION_LATITUDE,
					tempEvent.getLatitude());
			cv.put(EventsDatabaseHelper.COLUMN_LOCATION_LONGITUDE,
					tempEvent.getLongitude());

			/*
			 * String myString = tempEvent.getStartdate().getDate(); DateFormat
			 * format = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss.SSSSSS");
			 * Date date; try { date = format.parse(myString);
			 * 
			 * DateFormat df2 = new SimpleDateFormat("MM/dd/yyyy HH:mm a");
			 * String formattedDate = df2.format(date);
			 * 
			 * cv.put(EventsDatabaseHelper.COLUMN_DATE_OF_EVENT, formattedDate);
			 * } catch (ParseException e) { e.printStackTrace(); }
			 */
			cv.put(EventsDatabaseHelper.COLUMN_DATE_OF_EVENT,
					tempEvent.getStartdate());
			cv.put(EventsDatabaseHelper.COLUMN_TIME_OF_EVENT,
					tempEvent.getStarttime());

			cv.put(EventsDatabaseHelper.COLUMN_RSVP_COUNT,
					Integer.valueOf(tempEvent.getRsvpcount()));
			cv.put(EventsDatabaseHelper.COLUMN_CATEGORY,
					tempEvent.getCategory());

			cv.put(EventsDatabaseHelper.COLUMN_FEE,
					Double.valueOf(tempEvent.getFee()));
			cv.put(EventsDatabaseHelper.COLUMN_DESCRIPTION,
					tempEvent.getDescription());
			cv.put(EventsDatabaseHelper.COLUMN_VISIBILITY,
					tempEvent.getVisibility());

			/*
			 * cv.put(EventsDatabaseHelper.COLUMN_TIME_SENT,
			 * System.currentTimeMillis());
			 */
			/*
			 * cv.put(EventsDatabaseHelper.COLUMN_RSVP_STATUS,
			 * NearlingEvent.RSVP_DONT_KNOW);
			 */
			cv.put(EventsDatabaseHelper.COLUMN_EVENT_NAME, tempEvent.getTitle());

			cv.put(DatabaseCommandManager.SQL_INSERT_OR_REPLACE, true);
			c.getContentResolver()
					.insert(NearlingsContentProvider
							.contentURIbyTableName(EventsDatabaseHelper.TABLE_NAME),
							cv);
			mValueList.add(cv);

		}
		ContentValues[] bulkToInsert;
		bulkToInsert = new ContentValues[mValueList.size()];
		mValueList.toArray(bulkToInsert);

		c.getContentResolver()
				.bulkInsert(
						NearlingsContentProvider
								.contentURIbyTableName(EventsDatabaseHelper.TABLE_NAME),
						bulkToInsert);
		return false;
	}

	// for testing purposes
	public static int counter = 0;
}