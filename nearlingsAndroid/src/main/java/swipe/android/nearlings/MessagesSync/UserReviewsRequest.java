package swipe.android.nearlings.MessagesSync;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import swipe.android.DatabaseHelpers.NeedsOfferDatabaseHelper;
import swipe.android.DatabaseHelpers.UserReviewDatabaseHelper;
import swipe.android.nearlings.NearlingsContentProvider;
import swipe.android.nearlings.NearlingsRequest;
import swipe.android.nearlings.SessionManager;
import swipe.android.nearlings.json.jsonUserReviewsResponse.JsonUserReviewsResponse;
import swipe.android.nearlings.json.jsonUserReviewsResponse.Review;
import swipe.android.nearlings.sync.NearlingsSyncAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;

import com.edbert.library.database.DatabaseCommandManager;
import com.edbert.library.network.SocketOperator;
import com.gabesechan.android.reusable.location.ProviderLocationTracker;

public class UserReviewsRequest extends
		NearlingsRequest<JsonUserReviewsResponse> {
	public static final String BUNDLE_ID = "reviews_id";

	public UserReviewsRequest(Context c) {
		super(c);
	}

	@Override
	public JsonUserReviewsResponse makeRequest(Bundle b) {
		Map<String, String> headers = SessionManager.getInstance(c)
				.defaultSessionHeaders();
		String id = b.getString(BUNDLE_ID);
		String url = SessionManager.getInstance(c).userReviewsURL(id);
		if(b.getInt(NearlingsSyncAdapter.LIMIT)>0){
			int page_number =  (b.getInt(NearlingsSyncAdapter.LIMIT) / (SessionManager.SEARCH_LIMIT))+1;
			url +=( "?page=" +page_number);
			url += ("&limit="+ (SessionManager.SEARCH_LIMIT));

		}else {
			url +=( "?limit=" + (SessionManager.SEARCH_LIMIT));
			//url +=( "?limit=" + 9999);
		}
	//	Log.e("URL", url);
		Object o = SocketOperator.getInstance(getJSONclass()).getResponse(c,
				url, headers);
		if (o == null)
			return null;

		return (JsonUserReviewsResponse) o;
	}

	@Override
	public Class getJSONclass() {
		return JsonUserReviewsResponse.class;
	}

	ProviderLocationTracker tracker;

	@Override
	public boolean writeToDatabase(Bundle b, Context context,
			JsonUserReviewsResponse o) {
		// for now we will write random dummy stuff to the database
		if (o == null)
			return false;


			NearlingsContentProvider
					.clearSingleTable(new UserReviewDatabaseHelper());


		List<ContentValues> mValueList = new LinkedList<ContentValues>();
		for (int i = 0; i < o.getReviews().size(); i++) {
			Review tempOffer = o.getReviews().get(i);
			ContentValues cv = new ContentValues();
			cv.put(UserReviewDatabaseHelper.COLUMN_ID, tempOffer.getId());
			cv.put(UserReviewDatabaseHelper.COLUMN_MESSAGE,
					tempOffer.getMessage());
			cv.put(UserReviewDatabaseHelper.COLUMN_CREATED_BY,
					tempOffer.getCreated_by());
			cv.put(UserReviewDatabaseHelper.COLUMN_NEED_ID,
					tempOffer.getNeed_id());
			cv.put(UserReviewDatabaseHelper.COLUMN_EFFORT_RATING,
					tempOffer.getEffort_rating());
			cv.put(UserReviewDatabaseHelper.COLUMN_QUALITY_RATING,
					tempOffer.getQuality_rating());
			cv.put(UserReviewDatabaseHelper.COLUMN_TIMELINESS_RATING,
					tempOffer.getTimeliness_rating());
			cv.put(UserReviewDatabaseHelper.COLUMN_MESSAGE,
					tempOffer.getMessage());

			cv.put(UserReviewDatabaseHelper.COLUMN_CREATED_BY,
					tempOffer.getCreator_username());
			cv.put(DatabaseCommandManager.SQL_INSERT_OR_REPLACE, true);
			mValueList.add(cv);
			
		}

		ContentValues[] bulkToInsert;
		bulkToInsert = new ContentValues[mValueList.size()];
		mValueList.toArray(bulkToInsert);
		c.getContentResolver()
				.bulkInsert(
						NearlingsContentProvider
								.contentURIbyTableName(UserReviewDatabaseHelper.TABLE_NAME),
						bulkToInsert);

		return true;

	}
}