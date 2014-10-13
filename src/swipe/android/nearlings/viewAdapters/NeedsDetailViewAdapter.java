package swipe.android.nearlings.viewAdapters;

import swipe.android.DatabaseHelpers.NeedsCommentsDatabaseHelper;
import swipe.android.DatabaseHelpers.NeedsDetailsDatabaseHelper;
import swipe.android.nearlings.NearlingsContentProvider;
import swipe.android.nearlings.R;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class NeedsDetailViewAdapter extends CursorAdapter {

	private Context mContext;

	private Cursor cr;
	private final LayoutInflater inflater;

	public NeedsDetailViewAdapter(Context context, Cursor c) {
		super(context, c);

		this.mContext = context;
		this.inflater = LayoutInflater.from(context);
		this.cr = c;
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {

		ViewHolder holder = new ViewHolder();
		/*View view = inflater.inflate(R.layout.needs_details, parent, false);

		holder.title = (TextView) view.findViewById(R.id.needs_details_title);
		holder.price = (TextView) view.findViewById(R.id.needs_details_price);
		holder.date = (TextView) view.findViewById(R.id.needs_details_date);
		holder.personRequesting = (TextView) view
				.findViewById(R.id.needs_details_personRequesting);
		holder.description = (TextView) view
				.findViewById(R.id.needs_details_description);
		holder.location = (TextView) view
				.findViewById(R.id.needs_details_location);
		holder.numOfComments = (TextView) view
				.findViewById(R.id.needs_details_numOfComments);
		holder.mapFragment = (Fragment) view
				.findViewById(R.id.needs_details_map);
		holder.personRequestingImage = (ImageView) view
				.findViewById(R.id.needs_details_personRequestingImage);
		// this needs to have a live adapter attached.

		// view.setTag(holder);*/
		return null;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {

		final ViewHolder holder = (ViewHolder) view.getTag();

		int title_index = cursor
				.getColumnIndexOrThrow(NeedsDetailsDatabaseHelper.COLUMN_TITLE);
		int price_index = cursor
				.getColumnIndexOrThrow(NeedsDetailsDatabaseHelper.COLUMN_PRICE);
		int date_index = cursor
				.getColumnIndexOrThrow(NeedsDetailsDatabaseHelper.COLUMN_DATE);
		int author_index = cursor
				.getColumnIndexOrThrow(NeedsDetailsDatabaseHelper.COLUMN_AUTHOR);

		int description_index = cursor
				.getColumnIndexOrThrow(NeedsDetailsDatabaseHelper.COLUMN_DESCRIPTION);
		int latitude_index = cursor
				.getColumnIndexOrThrow(NeedsDetailsDatabaseHelper.COLUMN_LOCATION_GEOPOINT_LATITUDE);
		int longitude_index = cursor
				.getColumnIndexOrThrow(NeedsDetailsDatabaseHelper.COLUMN_LOCATION_GEOPOINT_LONGITUDE);

		int needs_id_index = cursor
				.getColumnIndexOrThrow(NeedsDetailsDatabaseHelper.COLUMN_ID_OF_NEED);

		// number of comments should be queried on the
		// NeedsCommentsDatabaseHelper

		String selectionClause = NeedsCommentsDatabaseHelper.COLUMN_NEEDS_ID
				+ " = '" + cursor.getString(needs_id_index) + "'";

		Cursor commentCursor = context
				.getContentResolver()
				.query(NearlingsContentProvider
						.contentURIbyTableName(NeedsCommentsDatabaseHelper.TABLE_NAME),
						NeedsCommentsDatabaseHelper.COLUMNS, selectionClause,
						null, null);
		// commentCursor.moveToFirst();

		String count = String.valueOf(commentCursor.getCount());

		int personRequestImage_index = cursor
				.getColumnIndexOrThrow(NeedsDetailsDatabaseHelper.COLUMN_AUTHOR_IMAGE_PREVIEW_URL);

		/*
		 * int sender_index = cursor
		 * .getColumnIndexOrThrow(BroadcastDBHelper.COLUMN_AUTHOR); int
		 * time_index = cursor
		 * .getColumnIndexOrThrow(BroadcastDBHelper.COLUMN_SENT); int
		 * message_index = cursor
		 * .getColumnIndexOrThrow(BroadcastDBHelper.COLUMN_MESSAGE);
		 * 
		 * int read_index = cursor
		 * .getColumnIndexOrThrow(BroadcastDBHelper.COLUMN_READ);
		 * 
		 * holder.sender.setText(cursor.getString(sender_index));
		 * 
		 * // problem is processing. this should only happen once.
		 * 
		 * holder.timeStamp.setText(cursor.getString(time_index));
		 * 
		 * holder.message.setText(cursor.getString(message_index));
		 * 
		 * boolean unreadBroadcast =
		 * Boolean.valueOf(cursor.getString(read_index)); if (unreadBroadcast) {
		 * holder.unreadMarker.setVisibility(View.VISIBLE); } else {
		 * holder.unreadMarker.setVisibility(View.GONE); }
		 */

	}

	public static class ViewHolder {
		public TextView title, price, date, personRequesting, description,
				location, numOfComments;

		// this needs to have a live adapter attached.
		public ScrollView commentView;
		public Fragment mapFragment;
		public ImageView personRequestingImage;
	}

}