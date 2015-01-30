package swipe.android.nearlings.MessagesSync;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import swipe.android.DatabaseHelpers.EventsDatabaseHelper;
import swipe.android.DatabaseHelpers.MessagesDatabaseHelper;
import swipe.android.DatabaseHelpers.NeedsDetailsDatabaseHelper;
import swipe.android.nearlings.NearlingsContentProvider;
import swipe.android.nearlings.NearlingsRequest;
import swipe.android.nearlings.SessionManager;
import swipe.android.nearlings.json.needs.needsdetailsresponse.JsonNeedsDetailResponse;
import swipe.android.nearlings.json.needs.needsdetailsresponse.NeedsDetails;
import swipe.android.nearlings.jsonResponses.explore.JsonExploreResponse;
import swipe.android.nearlings.jsonResponses.explore.Needs;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.util.Log;

import com.edbert.library.database.DatabaseCommandManager;
import com.edbert.library.network.SocketOperator;
import com.gabesechan.android.reusable.location.ProviderLocationTracker;

public class NeedsDetailsRequest extends
		NearlingsRequest<JsonNeedsDetailResponse> {

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
	public static final String BUNDLE_LOCATION_TYPE_LATITUDE = "latlng";

	public NeedsDetailsRequest(Context c) {
		super(c);
	}

	String id;

	public NeedsDetailsRequest(Context c, String id) {
		super(c);
		this.id = id;
	}

	@Override
	public JsonNeedsDetailResponse makeRequest(Bundle b) {
		Map<String, String> headers = SessionManager.getInstance(c)
				.defaultSessionHeaders();
		String url = SessionManager.getInstance(c).needsDetailsURL(id);

		Log.e("URL", url);
		Object o = SocketOperator.getInstance(getJSONclass()).getResponse(c, url,
				headers);
		if (o == null)
			return null;

		return (JsonNeedsDetailResponse) o;
	}

	@Override
	public Class getJSONclass() {
		return JsonNeedsDetailResponse.class;
	}

	ProviderLocationTracker tracker;

	@Override
	public boolean writeToDatabase(Context context, JsonNeedsDetailResponse o) {
		// for now we will write random dummy stuff to the database
		if (o == null)
			return false;
		NeedsDetails needsDetails = o.getDetails();
		// get from the database
		String selectionClause = NeedsDetailsDatabaseHelper.COLUMN_ID + " = ?";
		String[] selectionArgs = { "" };
		selectionArgs[0] = id;
		Cursor cursor = c.getContentResolver()
		.query(NearlingsContentProvider
						.contentURIbyTableName(NeedsDetailsDatabaseHelper.TABLE_NAME), NeedsDetailsDatabaseHelper.COLUMNS, selectionClause, selectionArgs, null);
		ContentValues cv = new ContentValues();
		  DatabaseUtils.cursorRowToContentValues(cursor, cv);  
		  
		cv.put(NeedsDetailsDatabaseHelper.COLUMN_TITLE, needsDetails.getTitle());
		cv.put(NeedsDetailsDatabaseHelper.COLUMN_DESCRIPTION,
				needsDetails.getDescription());
		cv.put(NeedsDetailsDatabaseHelper.COLUMN_STATUS,
				needsDetails.getStatus());
		cv.put(NeedsDetailsDatabaseHelper.COLUMN_CATEGORY,
				needsDetails.getCategory());
		cv.put(NeedsDetailsDatabaseHelper.COLUMN_REWARD,
				needsDetails.getReward());
		cv.put(NeedsDetailsDatabaseHelper.COLUMN_RATE_TYPE,
				needsDetails.getRatetype());
		cv.put(NeedsDetailsDatabaseHelper.COLUMN_OFFER_COUNT,
				needsDetails.getOffercount());
		cv.put(NeedsDetailsDatabaseHelper.COLUMN_STARTING,
				needsDetails.getStarting());
		cv.put(NeedsDetailsDatabaseHelper.COLUMN_ENDING,
				needsDetails.getEnding());
		cv.put(NeedsDetailsDatabaseHelper.COLUMN_GROUP_ID,
				needsDetails.getGroup_id());
		cv.put(NeedsDetailsDatabaseHelper.COLUMN_CREATED_BY,
				needsDetails.getCreated_by());
		cv.put(NeedsDetailsDatabaseHelper.COLUMN_ASSIGNED_TO,
				needsDetails.getAssigned_to());
		cv.put(NeedsDetailsDatabaseHelper.COLUMN_COMMENT_COUNT,
				needsDetails.getCommentcount());
		cv.put(NeedsDetailsDatabaseHelper.COLUMN_ADDRESS_1,
				needsDetails.getAddress1());
		cv.put(NeedsDetailsDatabaseHelper.COLUMN_ADDRESS_2,
				needsDetails.getAddress2());
		cv.put(NeedsDetailsDatabaseHelper.COLUMN_CITY, needsDetails.getCity());
		cv.put(NeedsDetailsDatabaseHelper.COLUMN_STATE, needsDetails.getState());
		cv.put(NeedsDetailsDatabaseHelper.COLUMN_ZIP, needsDetails.getZip());
		cv.put(NeedsDetailsDatabaseHelper.COLUMN_LOCATION_GEOPOINT_LATITUDE,
				needsDetails.getLatitude());
		cv.put(NeedsDetailsDatabaseHelper.COLUMN_LOCATION_GEOPOINT_LONGITUDE,
				needsDetails.getLongitude());
		cv.put(DatabaseCommandManager.SQL_INSERT_OR_REPLACE, true);

		c.getContentResolver()
				.insert(NearlingsContentProvider
						.contentURIbyTableName(NeedsDetailsDatabaseHelper.TABLE_NAME),
						cv);

		return true;

	}
}