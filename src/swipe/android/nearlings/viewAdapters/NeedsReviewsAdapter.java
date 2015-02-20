package swipe.android.nearlings.viewAdapters;

import java.util.Date;

import swipe.android.DatabaseHelpers.MessagesDatabaseHelper;
import swipe.android.DatabaseHelpers.NeedsOfferDatabaseHelper;
import swipe.android.DatabaseHelpers.UserReviewDatabaseHelper;
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
import android.widget.RatingBar;
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
		View view = inflater.inflate(R.layout.review_item, parent, false);

		holder.time_ago = (RelativeTimeTextView) view
				.findViewById(R.id.time_ago);
		holder.rating = (RatingBar) view.findViewById(R.id.ratingBar);
		holder.rating_author = (TextView) view.findViewById(R.id.rating_author);
		holder.review = (TextView) view.findViewById(R.id.review);

		view.setTag(holder);
		return null;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {

		final ViewHolder holder = (ViewHolder) view.getTag();

		int date_index = cursor
				.getColumnIndexOrThrow(UserReviewDatabaseHelper.COLUMN_TIME);
		int author_index = cursor
				.getColumnIndexOrThrow(UserReviewDatabaseHelper.COLUMN_AUTHOR);
		int rating_index = cursor
				.getColumnIndexOrThrow(UserReviewDatabaseHelper.COLUMN_RATING);

		int review_index = cursor
				.getColumnIndexOrThrow(UserReviewDatabaseHelper.COLUMN_REVIEW);

		// problem is processing. this should only happen once.
		long s = cursor.getLong(date_index);

		holder.time_ago.setReferenceTime(NOW - s);

		// holder.time_ago.setText(FieldsParsingUtils.getTime(s));
		holder.rating_author.setText(cursor.getString(author_index));
		holder.review.setText(cursor.getString(review_index));
		
		holder.rating.setRating(cursor.getFloat(rating_index));

	}

	public static class ViewHolder {
		RelativeTimeTextView time_ago;
		TextView rating_author, review;
		RatingBar rating;
	}

}