package swipe.android.nearlings.viewAdapters;

import java.util.Date;

import swipe.android.DatabaseHelpers.BalanceDatabaseHelper;
import swipe.android.DatabaseHelpers.MessagesDatabaseHelper;
import swipe.android.nearlings.FieldsParsingUtils;
import swipe.android.nearlings.R;
import swipe.android.nearlings.json.UserHistory.PaymentHistory;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.curioustechizen.ago.RelativeTimeTextView;

public class BalanceViewAdapter extends CursorAdapter {

    private Context mContext;

    private Cursor cr;
    private final LayoutInflater inflater;

    public BalanceViewAdapter(Context context, Cursor c) {
        super(context, c);

        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.cr = c;
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        ViewHolder holder = new ViewHolder();

        View rowView = inflater.inflate(R.layout.transaction_item, parent, false);
        holder.price = (TextView) rowView.findViewById(R.id.price);
        holder.name = (TextView) rowView.findViewById(R.id.name);
        holder.time = (TextView) rowView.findViewById(R.id.time);
       holder.title = (TextView) rowView.findViewById(R.id.title);

        rowView.setTag(holder);
        return rowView;

    }

    //	private static final long NOW = new Date().getTime();
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        final ViewHolder holder = (ViewHolder) view.getTag();

        int status_index = cursor
                .getColumnIndexOrThrow(BalanceDatabaseHelper.TXN_TYPE);
        if(cursor.getString(status_index).equals("need")){
          setUpNeedView(holder,cursor);
        }else{
           setUpWithdrawView(holder, cursor);
        }


    }

    public void setUpNeedView(	ViewHolder holder, Cursor c ){
// if is we're doing the need, we set it to green
        int earnings_index = c
                .getColumnIndexOrThrow(BalanceDatabaseHelper.TXN_DETAIL_earnings);
        if (c.getFloat(earnings_index)<= 0.0) {
            // no earnings so it must be neg

            int total_charge_index = c
                    .getColumnIndexOrThrow(BalanceDatabaseHelper.TXN_DETAIL_total_charge);
            holder.price.setText("-$"
                    + String.valueOf(c.getFloat(total_charge_index)));
            holder.price.setTextColor(Color.RED);
        } else {
            holder.price.setText("+$"
                    + String.valueOf((c.getFloat(earnings_index))));
            holder.price.setTextColor(Color.GREEN);
        }

        int recipient_index = c
                .getColumnIndexOrThrow(BalanceDatabaseHelper.TXN_DETAIL_RECIPIENT_NAME);

        holder.name.setText(c.getString(recipient_index));
        holder.time.setText(FieldsParsingUtils.getTime(mContext, c.getLong(c.getColumnIndexOrThrow(BalanceDatabaseHelper.CREATED_AT))));
        holder.title.setText(c.getString(c.getColumnIndexOrThrow(BalanceDatabaseHelper.TITLE)));

    }
    public void setUpWithdrawView(	ViewHolder holder, Cursor c ){
        int amount_deposited = c
                .getColumnIndexOrThrow(BalanceDatabaseHelper.TXN_DETAIL_amount_deposited);

        holder.price.setText("-$"
                + String.valueOf(c.getFloat(amount_deposited)));
        holder.price.setTextColor(Color.RED);

        int recipient_index = c
                .getColumnIndexOrThrow(BalanceDatabaseHelper.TXN_DETAIL_RECIPIENT_NAME);

        holder.name.setText(c.getString(recipient_index));
        holder.time.setText(FieldsParsingUtils.getTime(mContext, c.getLong(c.getColumnIndexOrThrow(BalanceDatabaseHelper.CREATED_AT))));
        holder.title.setText(c.getString(c.getColumnIndexOrThrow(BalanceDatabaseHelper.TITLE)));

    }
    public static class ViewHolder {
        TextView price, name,time,title;

    }

}