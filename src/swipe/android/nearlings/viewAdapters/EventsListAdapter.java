package swipe.android.nearlings.viewAdapters;

import swipe.android.DatabaseHelpers.EventsDatabaseHelper;
import swipe.android.nearlings.R;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class EventsListAdapter extends CursorAdapter {

	private Context mContext;
ListView lView;
	private Cursor cr;
	private final LayoutInflater inflater;

	public EventsListAdapter(Context context, Cursor c, ListView lView) {
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
		holder.title = (TextView) view.findViewById(R.id.event_title);
		holder.description = (TextView) view
				.findViewById(R.id.event_description);
		holder.timeOfEvent = (TextView) view.findViewById(R.id.event_time);
		holder.price = (TextView) view.findViewById(R.id.event_price);
		holder.bAction1 = (Button) view.findViewById(R.id.event_yes);
		holder.bAction2 = (Button) view.findViewById(R.id.event_no);
holder.front_layout = (RelativeLayout) view.findViewById(R.id.front);

		view.setTag(holder);
		return view;
	}

	@Override
	public void bindView(View view, Context context, final Cursor cursor) {

		final ViewHolder holder = (ViewHolder) view.getTag();

		int latitude_index = cursor
				.getColumnIndexOrThrow(EventsDatabaseHelper.COLUMN_LOCATION_LATITUDE);
	int longitude_index = cursor
				.getColumnIndexOrThrow(EventsDatabaseHelper.COLUMN_LOCATION_LONGITUDE);
	
		int date_of_event_index = cursor
				.getColumnIndexOrThrow(EventsDatabaseHelper.COLUMN_DATE_OF_EVENT);
		int rsvp_count_index = cursor
				.getColumnIndexOrThrow(EventsDatabaseHelper.COLUMN_RSVP_COUNT);
		
		int time_event_index = cursor
				.getColumnIndexOrThrow(EventsDatabaseHelper.COLUMN_TIME_OF_EVENT);
		int event_name_index = cursor
				.getColumnIndexOrThrow(EventsDatabaseHelper.COLUMN_EVENT_NAME);
		
		int fee_index = cursor
				.getColumnIndexOrThrow(EventsDatabaseHelper.COLUMN_FEE);
		int visibility_index = cursor
				.getColumnIndexOrThrow(EventsDatabaseHelper.COLUMN_VISIBILITY);
		int description_index = cursor
				.getColumnIndexOrThrow(EventsDatabaseHelper.COLUMN_DESCRIPTION);
		int category_index = cursor
				.getColumnIndexOrThrow(EventsDatabaseHelper.COLUMN_CATEGORY);
		
		
		String event_name = cursor.getString(event_name_index);
		String time_event = cursor.getString(time_event_index);
		String description = cursor.getString(description_index);
		float price = cursor.getFloat(fee_index);
		
		holder.title.setText(event_name);
		holder.description.setText(description);
		holder.timeOfEvent.setText(time_event);
		holder.price.setText("$"+ String.valueOf(price));

		
		int color = R.color.rsvp_unknown;
		/*
		if (rsvp.equals(NearlingEvent.RSVP_YES)) {
			
			color = R.color.rsvp_no;
			resetAllButtons(holder);
			holder.bAction3.setBackgroundColor(context.getResources().getColor(color));
		} else {
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
*/
		
	}

	public static class ViewHolder {
		public TextView title, description, timeOfEvent, price;
		public Button bAction1, bAction2;
		
		public RelativeLayout front_layout;
	}
	private void resetAllButtons(ViewHolder h){
		h.bAction1.setBackgroundResource(android.R.drawable.btn_default);

		h.bAction2.setBackgroundResource(android.R.drawable.btn_default);

	}
}