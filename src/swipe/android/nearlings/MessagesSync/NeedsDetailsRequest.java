package swipe.android.nearlings.MessagesSync;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import swipe.android.DatabaseHelpers.EventsDatabaseHelper;
import swipe.android.DatabaseHelpers.MessagesDatabaseHelper;
import swipe.android.DatabaseHelpers.NeedsDetailsDatabaseHelper;
import swipe.android.nearlings.NearlingsContentProvider;
import swipe.android.nearlings.NearlingsRequest;
import swipe.android.nearlings.R;
import swipe.android.nearlings.SessionManager;
import swipe.android.nearlings.jsonResponses.explore.JsonExploreResponse;
import swipe.android.nearlings.jsonResponses.explore.Tasks;
import swipe.android.nearlings.jsonResponses.login.JsonLoginResponse;
import android.content.ContentValues;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.edbert.library.database.DatabaseCommandManager;
import com.edbert.library.network.PostDataWebTask;
import com.edbert.library.network.SocketOperator;
import com.edbert.library.network.sync.JsonResponseInterface;
import com.edbert.library.utils.MapUtils;
import com.gabesechan.android.reusable.location.ProviderLocationTracker;
import com.gabesechan.android.reusable.location.ProviderLocationTracker.ProviderType;

public class NeedsDetailsRequest extends NearlingsRequest<JsonExploreResponse> {

	Class classType;
	public static final String BUNDLE_KEYWORDS = "KEYWORDS";
	public static final String BUNDLE_REWARD = "REWARD";
	public static final String BUNDLE_STATUS = "STATUS";
	public static final String BUNDLE_CATEGORY = "CATEGORY";
	public static final String BUNDLE_TIME_AGO = "TIME_AGO";
	public static final String BUNDLE_TIME_END = "TIME_END";

	public static final String BUNDLE_RADIUS = "RADIUS";
	public static final String BUNDLE_LOCATION_TYPE = "LOCATION_TYPE";
	public static final String BUNDLE_LOCATION = "LOCATION";

	public static final String BUNDLE_LOCATION_TYPE_ADDRESS = "address";
	public static final String BUNDLE_LOCATION_TYPE_LATITUDE = "latlng";

	public NeedsDetailsRequest(Context c, Class classType) {
		super(c);
		this.classType = classType;
	}

	String id;

	public NeedsDetailsRequest(Context c, String id) {
		super(c);
		this.id = id;
	}

	@Override
	public JsonExploreResponse makeRequest(Bundle b) {
		Map<String, String> headers = SessionManager.getInstance(c)
				.defaultSessionHeaders();
		String url = SessionManager.getInstance(c).exploreNeedsURL() + "?";

		if (b.containsKey(BUNDLE_RADIUS)) {
			url += ("radius=" + b.getFloat(BUNDLE_RADIUS));
		}
		if (b.containsKey(BUNDLE_REWARD)) {
			url += ("&reward=" + b.getFloat(BUNDLE_REWARD));
		}
		if (b.containsKey(BUNDLE_STATUS)) {
			url += ("&status=" + b.getString(BUNDLE_STATUS));
		}
		if (b.containsKey(BUNDLE_CATEGORY)) {
			url += ("&category=" + b.getString(BUNDLE_CATEGORY));
		}
		if (b.containsKey(BUNDLE_TIME_AGO)) {
			url += ("&time_ago=" + b.getString(BUNDLE_TIME_AGO));
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
		if (b.containsKey(BUNDLE_TIME_END)) {
			url += ("&time_end=" + b.getString(BUNDLE_TIME_END));
		}
		Log.e("URL", url);
		Object o = SocketOperator.getInstance(classType).getResponse(c, url,
				headers);
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
	public boolean writeToDatabase(Context context, JsonExploreResponse o) {
		// for now we will write random dummy stuff to the database

		if (o == null)
			return false;

		NearlingsContentProvider
				.clearSingleTable(new NeedsDetailsDatabaseHelper());
		
	
		List<ContentValues> mValueList = new LinkedList<ContentValues>();
		for (int i = 0; i < o.getTasks().size(); i++) {
			Tasks tempNearlingTask = o.getTasks().get(i);
			ContentValues cv = new ContentValues();

			cv.put(NeedsDetailsDatabaseHelper.COLUMN_ID,
					tempNearlingTask.getId());
			cv.put(NeedsDetailsDatabaseHelper.COLUMN_TITLE,
					tempNearlingTask.getTitle());
			
			String myString = tempNearlingTask.getDue_date().getDate();
			DateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss.SSSSSS");
			Date date;
			try {
				date = format.parse(myString);

				DateFormat df2 = new SimpleDateFormat("MM/dd/yyyy HH:mm a");
				String formattedDate = df2.format(date);

				cv.put(MessagesDatabaseHelper.COLUMN_DATE, formattedDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			cv.put(NeedsDetailsDatabaseHelper.COLUMN_AUTHOR,
					tempNearlingTask.getUser());

			cv.put(NeedsDetailsDatabaseHelper.COLUMN_PRICE,
					String.valueOf(tempNearlingTask.getReward()));

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