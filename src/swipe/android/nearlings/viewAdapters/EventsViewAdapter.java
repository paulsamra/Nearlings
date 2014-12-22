package swipe.android.nearlings.viewAdapters;

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
import android.widget.ListView;
import android.widget.TextView;

public class EventsViewAdapter extends CursorAdapter {

	private Context mContext;
ListView lView;
	private Cursor cr;
	private final LayoutInflater inflater;

	public EventsViewAdapter(Context context, Cursor c, ListView lView) {
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

		holder.event = (TextView) view.findViewById(R.id.event_name);
		holder.eventLocation = (TextView) view
				.findViewById(R.id.event_location);
		holder.timeSent = (TextView) view.findViewById(R.id.event_time_sent);
		holder.timeOfEvent = (TextView) view.findViewById(R.id.event_time_of);
		holder.author = (TextView) view.findViewById(R.id.event_author);

		holder.bAction1 = (Button) view.findViewById(R.id.event_yes);
		holder.bAction2 = (Button) view.findViewById(R.id.event_dont_know);
		holder.bAction3 = (Button) view.findViewById(R.id.event_no);
		
		view.setTag(holder);
		return view;
	}

	@Override
	public void bindView(View view, Context context, final Cursor cursor) {

		final ViewHolder holder = (ViewHolder) view.getTag();
		// ((SwipeListView) view).recycle(view, cursor.getPosition());
		int sender_index = cursor
				.getColumnIndexOrThrow(EventsDatabaseHelper.COLUMN_AUTHOR);
		int location_name_index = cursor
				.getColumnIndexOrThrow(EventsDatabaseHelper.COLUMN_LOCATION_NAME);
		int time_event_index = cursor
				.getColumnIndexOrThrow(EventsDatabaseHelper.COLUMN_TIME_OF_EVENT);
		int time_sent_index = cursor
				.getColumnIndexOrThrow(EventsDatabaseHelper.COLUMN_TIME_SENT);
		int rsvp_index = cursor
				.getColumnIndexOrThrow(EventsDatabaseHelper.COLUMN_RSVP_STATUS);
		int event_name_index = cursor
				.getColumnIndexOrThrow(EventsDatabaseHelper.COLUMN_EVENT_NAME);

		String event_name = cursor.getString(event_name_index);
		String time_event = cursor.getString(time_event_index);

		String time_event_sent = cursor.getString(time_sent_index);
		String sender = cursor.getString(sender_index);
		String location_name = cursor.getString(location_name_index);
		String rsvp = cursor.getString(rsvp_index);

		holder.event.setText(event_name);
		holder.eventLocation.setText(location_name);
		holder.timeSent.setText(time_event_sent);
		holder.timeOfEvent.setText(time_event);
		holder.author.setText(sender);

		int color = R.color.rsvp_unknown;
		
		if (rsvp.equals(NearlingEvent.RSVP_NO)) {
			
			color = R.color.rsvp_no;
			resetAllButtons(holder);
			holder.bAction3.setBackgroundColor(context.getResources().getColor(color));
		} else if (rsvp.equals(NearlingEvent.RSVP_YES)) { 
			color = R.color.rsvp_yes;
			resetAllButtons(holder);
			holder.bAction1.setBackgroundColor(context.getResources().getColor(color));
		}else{
			resetAllButtons(holder);
		}
		int textColor = context.getResources().getColor(color);
	
		holder.event.setTextColor(textColor);
		holder.timeOfEvent.setTextColor(textColor);
		holder.eventLocation.setTextColor(textColor);
		final int currentPosition = cursor.getPosition();
		holder.bAction1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ContentValues cv = new ContentValues();
				cursor.moveToPosition(currentPosition);
				DatabaseUtils.cursorRowToContentValues(cursor, cv);
				cv.put(EventsDatabaseHelper.COLUMN_RSVP_STATUS,
						NearlingEvent.RSVP_YES);
				cv.put(DatabaseCommandManager.SQL_INSERT_OR_REPLACE, true);
				mContext.getContentResolver()
						.insert(NearlingsContentProvider
								.contentURIbyTableName(EventsDatabaseHelper.TABLE_NAME),
								cv);
				lView.invalidateViews();
				if(lView instanceof SwipeListView ){
					SwipeListView slv = (SwipeListView) lView;
					slv.closeOpenedItems();
				}
				
			}
		});

		holder.bAction2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ContentValues cv = new ContentValues();

				cursor.moveToPosition(currentPosition);
				DatabaseUtils.cursorRowToContentValues(cursor, cv);
				cv.put(EventsDatabaseHelper.COLUMN_RSVP_STATUS,
						NearlingEvent.RSVP_DONT_KNOW);
				cv.put(DatabaseCommandManager.SQL_INSERT_OR_REPLACE, true);
				mContext.getContentResolver()
						.insert(NearlingsContentProvider
								.contentURIbyTableName(EventsDatabaseHelper.TABLE_NAME),
								cv);
				lView.invalidateViews();
				if(lView instanceof SwipeListView ){
					SwipeListView slv = (SwipeListView) lView;
					slv.closeOpenedItems();
				}	}
		});

		holder.bAction3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ContentValues cv = new ContentValues();
				cursor.moveToPosition(currentPosition);
				DatabaseUtils.cursorRowToContentValues(cursor, cv);
				
				cv.put(EventsDatabaseHelper.COLUMN_RSVP_STATUS,
						NearlingEvent.RSVP_NO);
				cv.put(DatabaseCommandManager.SQL_INSERT_OR_REPLACE, true);
				mContext.getContentResolver()
						.insert(NearlingsContentProvider
								.contentURIbyTableName(EventsDatabaseHelper.TABLE_NAME),
								cv);
				lView.invalidateViews();
				if(lView instanceof SwipeListView ){
					SwipeListView slv = (SwipeListView) lView;
					slv.closeOpenedItems();
				}}
		});
	}

	public static class ViewHolder {
		public TextView event, eventLocation, timeSent, timeOfEvent, author;
		public Button bAction1, bAction2, bAction3;
	}
	private void resetAllButtons(ViewHolder h){
		h.bAction1.setBackgroundResource(android.R.drawable.btn_default);

		h.bAction2.setBackgroundResource(android.R.drawable.btn_default);

		h.bAction3.setBackgroundResource(android.R.drawable.btn_default);
	}
}