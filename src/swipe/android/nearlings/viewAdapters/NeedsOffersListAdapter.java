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

public class NeedsOffersListAdapter extends CursorAdapter {

	private Context mContext;

	private Cursor cr;
	private final LayoutInflater inflater;

	public NeedsOffersListAdapter(Context context, Cursor c) {
		super(context, c);

		this.mContext = context;
		this.inflater = LayoutInflater.from(context);
		this.cr = c;
	}
	private static final long NOW = new Date().getTime();
	
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {

		ViewHolder holder = new ViewHolder();
		View view = inflater.inflate(R.layout.bid_item, parent, false);
		
		holder.bidNumber = (TextView) view.findViewById(R.id.bid);
		holder.dateBid = (TextView) view.findViewById(R.id.time);
		holder.user = (TextView) view.findViewById(R.id.name);
		holder.message = (TextView) view.findViewById(R.id.message);
		
		view.setTag(holder);
		return view;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {

		final ViewHolder holder = (ViewHolder) view.getTag();
	
		
		int bid_index = cursor
				.getColumnIndexOrThrow(NeedsOfferDatabaseHelper.COLUMN_OFFER_PRICE);
		int user_index = cursor
				.getColumnIndexOrThrow(NeedsOfferDatabaseHelper.COLUMN_USERNAME);
		int message_index = cursor
				.getColumnIndexOrThrow(NeedsOfferDatabaseHelper.COLUMN_MESSAGE);

		int date_index = cursor
				.getColumnIndexOrThrow(NeedsOfferDatabaseHelper.COLUMN_CREATED_AT);

	
		// problem is processing. this should only happen once.
		long s = cursor.getLong(date_index);

		holder.dateBid.setText(FieldsParsingUtils.getTime(s));
holder.user.setText(cursor.getString(user_index));
		holder.message.setText(cursor.getString(message_index));
		holder.bidNumber.setText(cursor.getString(bid_index));

	
	}

	public static class ViewHolder {
		TextView message, dateBid, user,bidNumber;
		public ImageView userIcon;

	}

}