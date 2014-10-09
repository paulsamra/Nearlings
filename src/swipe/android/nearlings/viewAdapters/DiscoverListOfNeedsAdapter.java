package swipe.android.nearlings.viewAdapters;

import swipe.android.nearlings.R;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DiscoverListOfNeedsAdapter extends CursorAdapter {

	private Context mContext;

	private Cursor cr;
	private final LayoutInflater inflater;

	private static int NO_LAYOUT = -1;
	private int layout = NO_LAYOUT;

	public DiscoverListOfNeedsAdapter(Context context, Cursor c) {
		super(context, c);

		this.mContext = context;
		this.inflater = LayoutInflater.from(context);
		this.cr = c;
		this.layout = NO_LAYOUT;
	}

	public DiscoverListOfNeedsAdapter(Context context, Cursor c, int layout) {
		super(context, c);

		this.mContext = context;
		this.inflater = LayoutInflater.from(context);
		this.cr = c;
		this.layout = layout;
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {

		ViewHolder holder = new ViewHolder();
		View view;
		if (this.layout == NO_LAYOUT) {
			view = inflater.inflate(R.layout.list_of_needs_list_layout, parent,
					false);
		} else {
			view = inflater.inflate(layout, parent, false);
		}
		holder.dateSent = (TextView) view.findViewById(R.id.needs_date_sent);
		holder.person = (TextView) view.findViewById(R.id.needs_person);
		holder.task = (TextView) view.findViewById(R.id.needs_task);
		holder.location = (TextView) view.findViewById(R.id.needs_location);
		holder.price = (TextView) view.findViewById(R.id.needs_price);
		holder.status = (TextView) view.findViewById(R.id.needs_status);
		
		holder.personImage = (ImageView) view
				.findViewById(R.id.needs_person_image);

		view.setTag(holder);
		return view;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {

		final ViewHolder holder = (ViewHolder) view.getTag();

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
		public TextView dateSent, person, task, location, price, status;

		public ImageView personImage;

	}

}