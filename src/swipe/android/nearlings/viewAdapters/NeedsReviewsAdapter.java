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

		holder.quality_rating = (RatingBar) view.findViewById(R.id.quality_ratingBar);

		holder.effort_rating = (RatingBar) view.findViewById(R.id.effort_ratingBar);
		holder.timeliness_rating = (RatingBar) view.findViewById(R.id.timeliness_ratingBar);
		
		holder.message = (TextView) view.findViewById(R.id.message);

		view.setTag(holder);
		return view;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {

		final ViewHolder holder = (ViewHolder) view.getTag();

		int effort_index = cursor
				.getColumnIndexOrThrow(UserReviewDatabaseHelper.COLUMN_EFFORT_RATING);
		int message_index = cursor
				.getColumnIndexOrThrow(UserReviewDatabaseHelper.COLUMN_MESSAGE);
		int quality_rating_index = cursor
				.getColumnIndexOrThrow(UserReviewDatabaseHelper.COLUMN_QUALITY_RATING);
		int timeliness_rating_index = cursor
				.getColumnIndexOrThrow(UserReviewDatabaseHelper.COLUMN_TIMELINESS_RATING);



		// holder.time_ago.setText(FieldsParsingUtils.getTime(s));
		holder.timeliness_rating.setRating(cursor.getInt(timeliness_rating_index));
		holder.quality_rating.setRating(cursor.getInt(quality_rating_index));
		holder.message.setText(cursor.getString(message_index));
		holder.effort_rating.setRating(cursor.getInt(effort_index));

	}

	public static class ViewHolder {
		///RelativeTimeTextView time_ago;
		TextView message;
		RatingBar quality_rating, timeliness_rating, effort_rating;
	}

}