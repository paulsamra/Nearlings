package swipe.android.nearlings.viewAdapters;

import java.util.Date;

import swipe.android.DatabaseHelpers.MessagesDatabaseHelper;
import swipe.android.DatabaseHelpers.NeedsOfferDatabaseHelper;
import swipe.android.nearlings.FieldsParsingUtils;
import swipe.android.nearlings.R;
import swipe.android.nearlings.MessagesSync.NeedsDetailsRequest;
import swipe.android.nearlings.MessagesSync.NeedsOffersRequest;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.curioustechizen.ago.RelativeTimeTextView;

public class NeedsReviewsAdapter extends CursorAdapter {

	private Context mContext;

	private Cursor cr;
	private final LayoutInflater inflater;

	public NeedsReviewsAdapter(Context context, Cursor c) {
		super(context, c);

		this.mContext = context;
		this.inflater = LayoutInflater.from(context);
		this.cr = c;
	}
	private static final long NOW = new Date().getTime();
	
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {

		ViewHolder holder = new ViewHolder();
		/*View view = inflater.inflate(R.layout.review_item, parent, false);
		
		holder.time_ago = (RelativeTimeTextView) view.findViewById(R.id.time_ago);
		holder.rating = (TextView) view.findViewById(R.id.rating);
		holder.user = (TextView) view.findViewById(R.id.user);
		holder.review = (TextView) view.findViewById(R.id.review);
		
		view.setTag(holder);*/
		return null;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {

		final ViewHolder holder = (ViewHolder) view.getTag();
	
		
	/*	int bid_index = cursor
				.getColumnIndexOrThrow(UserReviewDatabaseHelper.COLUMN_OFFER_PRICE);
		int user_index = cursor
				.getColumnIndexOrThrow(UserReviewDatabaseHelper.COLUMN_USERNAME);
		int message_index = cursor
				.getColumnIndexOrThrow(UserReviewDatabaseHelper.COLUMN_MESSAGE);

		int date_index = cursor
				.getColumnIndexOrThrow(UserReviewDatabaseHelper.COLUMN_CREATED_AT);

	
		// problem is processing. this should only happen once.
		long s = cursor.getLong(date_index);
		
		holder.time_ago.setReferenceTime(NOW - s);

		//holder.time_ago.setText(FieldsParsingUtils.getTime(s));
holder.user.setText(cursor.getString(user_index));
		holder.review.setText(cursor.getString(message_index));
		holder.rating.setText(cursor.getString(bid_index));*/

	
	}

	public static class ViewHolder {
		 RelativeTimeTextView time_ago;
		TextView rating, user,review;
	}

}