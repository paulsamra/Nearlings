/*package swipe.android.nearlings.viewAdapters;

import com.edbert.library.database.DatabaseCommandManager;
import com.fortysevendeg.swipelistview.SwipeListView;

import swipe.android.DatabaseHelpers.EventsDatabaseHelper;
import swipe.android.nearlings.NearlingsContentProvider;
import swipe.android.nearlings.R;
import swipe.android.nearlings.MessagesSync.NearlingEvent;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class GroupsViewAdapter extends CursorAdapter {

	private Context mContext;
ListView lView;
	private Cursor cr;
	private final LayoutInflater inflater;

	public GroupsViewAdapter(Context context, Cursor c, ListView lView) {
		super(context, c);
this.lView = lView;
		this.mContext = context;
		this.inflater = LayoutInflater.from(context);
		this.cr = c;
	}

	@Override
	public View newView(Context context, final Cursor cursor, ViewGroup parent) {

		ViewHolder holder = new ViewHolder();
		View view = inflater.inflate(R.layout.events_options, parent, false);
		
		public ImageView groupIcon, personIcon1, personIcon2, personIcon3, personIcon4;
		public TextView groupName;
		public TextView lastActive;
		holder.groupIcon =  (ImageView) view.findViewById(R.id.group_icon);
		holder.personIcon1 = (ImageView) view.findViewById(R.id.group_person1);
		holder.personIcon2 = (ImageView) view.findViewById(R.id.group_person2);
		holder.personIcon3 = (ImageView) view.findViewById(R.id.group_person3);
		holder.personIcon4 = (ImageView) view.findViewById(R.id.group_person4s);
		holder.groupName =(TextView) view
				.findViewById(R.id.group_name);
				holder.lastActive = (TextView) view
						.findViewById(R.id.group_last_active);
		view.setTag(holder);
		return view;
	}

	@Override
	public void bindView(View view, Context context, final Cursor cursor) {

		final ViewHolder holder = (ViewHolder) view.getTag();
		// ((SwipeListView) view).recycle(view, cursor.getPosition());
		int sender_index = cursor
				.getColumnIndexOrThrow(GroupsDatabaseHelper.COLUMN_AUTHOR);
		int location_name_index = cursor
				.getColumnIndexOrThrow(GroupsDatabaseHelper.COLUMN_LOCATION_NAME);
		int time_event_index = cursor
				.getColumnIndexOrThrow(GroupsDatabaseHelper.COLUMN_TIME_OF_EVENT);
		int time_sent_index = cursor
				.getColumnIndexOrThrow(GroupsDatabaseHelper.COLUMN_TIME_SENT);
		int rsvp_index = cursor
				.getColumnIndexOrThrow(GroupsDatabaseHelper.COLUMN_RSVP_STATUS);
		int event_name_index = cursor
				.getColumnIndexOrThrow(GroupsDatabaseHelper.COLUMN_EVENT_NAME);

		String event_name = cursor.getString(event_name_index);
		String time_event = cursor.getString(time_event_index);

		String time_event_sent = cursor.getString(time_sent_index);
		String sender = cursor.getString(sender_index);
		String location_name = cursor.getString(location_name_index);
		String rsvp = cursor.getString(rsvp_index);

		
	}

	public static class ViewHolder {
		public ImageView groupIcon, personIcon1, personIcon2, personIcon3, personIcon4;
		public TextView groupName;
		public TextView lastActive;
		
	}
}*/