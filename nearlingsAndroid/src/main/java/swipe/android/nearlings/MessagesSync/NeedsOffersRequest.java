package swipe.android.nearlings.MessagesSync;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import swipe.android.DatabaseHelpers.NeedsOfferDatabaseHelper;
import swipe.android.nearlings.NearlingsContentProvider;
import swipe.android.nearlings.NearlingsRequest;
import swipe.android.nearlings.SessionManager;
import swipe.android.nearlings.json.needs.needsdetailsoffersresponse.JsonNeedsOffersResponse;
import swipe.android.nearlings.json.needs.needsdetailsoffersresponse.NeedsOffers;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;

import com.edbert.library.database.DatabaseCommandManager;
import com.edbert.library.network.SocketOperator;
import com.gabesechan.android.reusable.location.ProviderLocationTracker;

public class NeedsOffersRequest extends
		NearlingsRequest<JsonNeedsOffersResponse> {
	public static final String BUNDLE_ID = "offer_id";

	public NeedsOffersRequest(Context c) {
		super(c);
	}

	@Override
	public JsonNeedsOffersResponse makeRequest(Bundle b) {
		Map<String, String> headers = SessionManager.getInstance(c)
				.defaultSessionHeaders();
		String id = b.getString(BUNDLE_ID);
		String url = SessionManager.getInstance(c).needsOffersURL(id);

	//	Log.e("URL", url);
		Object o = SocketOperator.getInstance(getJSONclass()).getResponse(c,
				url, headers);
		if (o == null)
			return null;

		return (JsonNeedsOffersResponse) o;
	}

	@Override
	public Class getJSONclass() {
		return JsonNeedsOffersResponse.class;
	}

	ProviderLocationTracker tracker;

	@Override
	public boolean writeToDatabase(Bundle b, Context context,
			JsonNeedsOffersResponse o) {
		// for now we will write random dummy stuff to the database
		if (o == null)
			return false;
		NearlingsContentProvider
				.clearSingleTable(new NeedsOfferDatabaseHelper());

		List<ContentValues> mValueList = new LinkedList<ContentValues>();
		for (int i = 0; i < o.getOffers().size(); i++) {
			NeedsOffers tempOffer = o.getOffers().get(i);
			ContentValues cv = new ContentValues();
			cv.put(NeedsOfferDatabaseHelper.COLUMN_ID, tempOffer.getId());
			cv.put(NeedsOfferDatabaseHelper.COLUMN_MESSAGE,
					tempOffer.getMessage());
			cv.put(NeedsOfferDatabaseHelper.COLUMN_CREATED_BY,
					tempOffer.getCreated_by());
			cv.put(NeedsOfferDatabaseHelper.COLUMN_CHANGED_BY,
					tempOffer.getChanged_by());
			cv.put(NeedsOfferDatabaseHelper.COLUMN_STATUS,
					tempOffer.getStatus());
			cv.put(NeedsOfferDatabaseHelper.COLUMN_OFFER_PRICE,
					tempOffer.getOfferprice());
			cv.put(NeedsOfferDatabaseHelper.COLUMN_CREATED_AT,
					tempOffer.getCreated_at());
			cv.put(NeedsOfferDatabaseHelper.COLUMN_USERNAME,
					tempOffer.getUsername());
			cv.put(NeedsOfferDatabaseHelper.COLUMN_THUMBNAIL,
					tempOffer.getThumbnail());

			cv.put(DatabaseCommandManager.SQL_INSERT_OR_REPLACE, true);
			mValueList.add(cv);

		}

		ContentValues[] bulkToInsert;
		bulkToInsert = new ContentValues[mValueList.size()];
		mValueList.toArray(bulkToInsert);
		c.getContentResolver()
				.bulkInsert(
						NearlingsContentProvider
								.contentURIbyTableName(NeedsOfferDatabaseHelper.TABLE_NAME),
						bulkToInsert);

		return true;

	}
}