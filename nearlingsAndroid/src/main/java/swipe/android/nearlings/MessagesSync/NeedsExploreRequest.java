package swipe.android.nearlings.MessagesSync;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import swipe.android.DatabaseHelpers.MessagesDatabaseHelper;
import swipe.android.DatabaseHelpers.NeedsDetailsDatabaseHelper;
import swipe.android.nearlings.BaseContainerFragment;
import swipe.android.nearlings.NearlingsContentProvider;
import swipe.android.nearlings.NearlingsRequest;
import swipe.android.nearlings.SessionManager;
import swipe.android.nearlings.jsonResponses.explore.JsonExploreResponse;
import swipe.android.nearlings.jsonResponses.explore.Needs;
import swipe.android.nearlings.sync.NearlingsSyncAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.edbert.library.database.DatabaseCommandManager;
import com.edbert.library.network.SocketOperator;
import com.gabesechan.android.reusable.location.ProviderLocationTracker;

public class NeedsExploreRequest extends NearlingsRequest<JsonExploreResponse> {

	public static final String BUNDLE_KEYWORDS = "KEYWORDS";
	public static final String BUNDLE_REWARD = "REWARD";
	public static final String BUNDLE_STATUS = "STATUS";
	public static final String BUNDLE_CATEGORY = "CATEGORY";
	public static final String BUNDLE_TIME_AGO = "TIME_AGO";
	public static final String BUNDLE_TIME_END = "TIME_END";
	public static final String BUNDLE_VISIBILITY = "BUNDLE_VISIBILITY";

	public static final String BUNDLE_RADIUS = "RADIUS";
	public static final String BUNDLE_LOCATION_TYPE = "LOCATION_TYPE";
	public static final String BUNDLE_LOCATION = "LOCATION";

	public static final String BUNDLE_LOCATION_TYPE_ADDRESS = "address";
	public static final String BUNDLE_LOCATION_TYPE_COORDINATES = "latlng";

	public static final String BUNDLE_LOCATION_LATITUDE = "lat";
	public static final String BUNDLE_LOCATION_LONGITUDE = "lng";

	public NeedsExploreRequest(Context c) {
		super(c);
	}

	@Override
	public JsonExploreResponse makeRequest(Bundle b) {
		Map<String, String> headers = SessionManager.getInstance(c)
				.defaultSessionHeaders();
		String url = SessionManager.getInstance(c).exploreNeedsURL() + "?";

		// default the statuses to whatever
		if (b.containsKey(BUNDLE_STATUS)) {
			url += ("&status=" + b.getString(BUNDLE_STATUS));
		}
		if (b.containsKey(BUNDLE_TIME_END)) {
			url += ("&time_end=" + b.getLong(BUNDLE_TIME_END));
		}
		if (b.containsKey(BUNDLE_TIME_AGO)) {
			url += ("&time_ago=" + b.getLong(BUNDLE_TIME_AGO));
		}

		if (b.containsKey(BUNDLE_REWARD)) {
			url += ("&reward=" + b.getFloat(BUNDLE_REWARD));
		}

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

		url += ("&visibility=" + "public");
		//url += ("&limit=" + SessionManager.SEARCH_LIMIT);
		/*
		 * if (b.containsKey(BUNDLE_VISIBILITY)) { url += ("&visibility=" +
		 * b.getString(BUNDLE_VISIBILITY)); }
		 */
		int page_number = 1;
			if(b.getInt(BaseContainerFragment.STATUS) == BaseContainerFragment.is_reload_and_blank){
			url +=( "&limit=" + (SessionManager.SEARCH_LIMIT));
		}else if(b.getInt(BaseContainerFragment.STATUS) == BaseContainerFragment.is_maintain){
			page_number =  (b.getInt(NearlingsSyncAdapter.LIMIT) / (SessionManager.SEARCH_LIMIT));
			url +=( "&page=" +page_number);
			url += ("&limit="+ (SessionManager.SEARCH_LIMIT));
		}else if(b.getInt(BaseContainerFragment.STATUS) == BaseContainerFragment.is_loadMore){
			Log.d("IS_LOAD_MORE", String.valueOf(b.getInt(NearlingsSyncAdapter.LIMIT) ));
			page_number =  (b.getInt(NearlingsSyncAdapter.LIMIT) / (SessionManager.SEARCH_LIMIT))+1;
			url +=( "&page=" +page_number);
		}else{
			url +=( "&limit=" + (SessionManager.SEARCH_LIMIT));
		}
		Object o = SocketOperator.getInstance(getJSONclass()).getResponse(c,
				url, headers);
		if (o == null)
			return null;

		return (JsonExploreResponse) o;
	}

	@Override
	public Class getJSONclass() {
		return JsonExploreResponse.class;
	}

	ProviderLocationTracker tracker;

	@Override
	public boolean writeToDatabase(Bundle b, Context context,
			JsonExploreResponse o) {
		// for now we will write random dummy stuff to the database

		if (o == null)
			return false;
		if(b.getBoolean(NearlingsSyncAdapter.CLEAR_DB)) {
			NearlingsContentProvider
					.clearSingleTable(new NeedsDetailsDatabaseHelper());
		}
		List<ContentValues> mValueList = new LinkedList<ContentValues>();
		for (int i = 0; i < o.getTasks().size(); i++) {
			Needs tempNearlingTask = o.getTasks().get(i);
			ContentValues cv = new ContentValues();

			cv.put(NeedsDetailsDatabaseHelper.COLUMN_ID,
					tempNearlingTask.getId());
			cv.put(NeedsDetailsDatabaseHelper.COLUMN_TITLE,
					tempNearlingTask.getTitle());

			/*
			 * String myString = tempNearlingTask.getDue_date().getDate();
			 * DateFormat format = new SimpleDateFormat(
			 * "yyyy-MM-dd HH:mm:ss.SSSSSS"); Date date; try { date =
			 * format.parse(myString);
			 * 
			 * DateFormat df2 = new SimpleDateFormat("MM/dd/yyyy HH:mm a");
			 * String formattedDate = df2.format(date);
			 * 
			 * cv.put(MessagesDatabaseHelper.COLUMN_DATE, formattedDate); }
			 * catch (ParseException e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); }
			 */
			cv.put(NeedsDetailsDatabaseHelper.COLUMN_DUE_DATE,
					tempNearlingTask.getDue_date());
			cv.put(NeedsDetailsDatabaseHelper.COLUMN_USER,
					tempNearlingTask.getUser());

			cv.put(NeedsDetailsDatabaseHelper.COLUMN_REWARD,
					tempNearlingTask.getReward());
			Log.d("Task", Float.toString(tempNearlingTask.getReward()));

			cv.put(NeedsDetailsDatabaseHelper.COLUMN_LOCATION_NAME,
					"Location data");

			cv.put(NeedsDetailsDatabaseHelper.COLUMN_LOCATION_GEOPOINT_LATITUDE,
					tempNearlingTask.getLatitude());
			cv.put(NeedsDetailsDatabaseHelper.COLUMN_LOCATION_GEOPOINT_LONGITUDE,
					tempNearlingTask.getLongitude());

			cv.put(NeedsDetailsDatabaseHelper.COLUMN_DESCRIPTION,
					tempNearlingTask.getDescription());
			cv.put(NeedsDetailsDatabaseHelper.COLUMN_AUTHOR_IMAGE_PREVIEW_URL,
					tempNearlingTask.getUser_thumbnail());
			cv.put(NeedsDetailsDatabaseHelper.COLUMN_STATUS,
					tempNearlingTask.getStatus());
			cv.put(DatabaseCommandManager.SQL_INSERT_OR_REPLACE,
					true);
			mValueList.add(cv);

		}
		ContentValues[] bulkToInsert;
		bulkToInsert = new ContentValues[mValueList.size()];
		mValueList.toArray(bulkToInsert);
		c.getContentResolver()
				.bulkInsert(
						NearlingsContentProvider
								.contentURIbyTableName(NeedsDetailsDatabaseHelper.TABLE_NAME),
						bulkToInsert);

		return true;

	}
}