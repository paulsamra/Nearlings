package swipe.android.nearlings;

import java.util.ArrayList;
import java.util.List;

import swipe.android.nearlings.json.UserHistory.PaymentHistory;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class PaymentHistoryListActivity extends ListActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simple_list);

		Intent intent = getIntent();

		updateHistory(intent.getExtras()
				.getParcelableArrayList("list"));

	}

	private void updateHistory(ArrayList<Parcelable> list) {
		TransactionAdapter adapter = new TransactionAdapter(this,
				R.layout.transaction_item, list);
		listOfTransactions = list;
		setListAdapter(adapter);
	}
	List<Parcelable> listOfTransactions;
	// when an item of the list is clicked
	@Override
	protected void onListItemClick(ListView list, View view, int position,
			long id) {
		PaymentHistory item = (PaymentHistory) listOfTransactions.get(position);
		String message;
		if(item.getTxn_type().equals("withdrawal")){
			message = withdrawalClick(item);
		}else{
			message = needClick(item);
		}
		new AlertDialog.Builder(this)
				.setTitle( item.getTitle())
				.setMessage(message)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// continue with delete
						dialog.cancel();
					}
				})
				.show();
	}
	private String withdrawalClick(PaymentHistory item){
		return
				"Created At: " + FieldsParsingUtils.getTime(PaymentHistoryListActivity.this, item.getCreated_at()) + "\n" +
				"Email: " + item.getTxn_details().getPaypal_email() + "\n" +
				"Withdraw Amount: " + item.getTxn_details().getTotal_amount() + "\n" +
				"Fee: " + item.getTxn_details().getFee() + "\n" +
				"Total: " + item.getTxn_details().getAmount_deposited();


	}

	private String needClick(PaymentHistory item){
	String s =
				"Created At: " + FieldsParsingUtils.getTime(PaymentHistoryListActivity.this, item.getCreated_at()) + "\n" +
				"Recipient Username: " + item.getTxn_details().getRecipient_username() + "\n" +
				"Recipient Name: " + item.getTxn_details().getRecipient_name() + "\n" +
				"Task Title: " + item.getTxn_details().getTask_title() + "\n" ;
		if(item.getTxn_details().getEarnings() < 0) {
			s += "Task Cost: " + item.getTxn_details().getTask_cost() + "\n" +

					"Task Fee: " + item.getTxn_details().getFee() + "\n" +

					"Total Charge: " + item.getTxn_details().getTotal_charge() + "\n";
		}else{
			s += "Total Earnings: " + item.getTxn_details().getEarnings() + "\n";
		}
		return s;
	}
	private class TransactionAdapter extends ArrayAdapter<Parcelable> {

		int layout;

		public TransactionAdapter(Context context, int textViewResourceId,
				List<Parcelable> objects) {
			super(context, textViewResourceId, objects);
			this.layout = textViewResourceId;

		}
		TextView price, name, time, title;
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			PaymentHistory item = (PaymentHistory) getItem(position);
			LayoutInflater inflater = (LayoutInflater) super.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View rowView = inflater.inflate(layout, parent, false);
			 price = (TextView) rowView.findViewById(R.id.price);
			 name = (TextView) rowView.findViewById(R.id.name);
			 time = (TextView) rowView.findViewById(R.id.time);
			title = (TextView) rowView.findViewById(R.id.title);
			if(item.getTxn_type().equals("need")){
				return setUpNeedView(rowView,item);
			}else{
				return setUpWithdrawView(rowView,item);
			}

		}

		public View setUpNeedView(	View rowView, PaymentHistory item ){
// if is we're doing the need, we set it to green
			if (item.getTxn_details().getEarnings() == 0.0) {
				// no earnings so it must be neg
				price.setText("-$"
						+ String.valueOf(item.getTxn_details()
						.getTotal_charge()));
				price.setTextColor(Color.RED);
			} else {
				price.setText("+$"
						+ String.valueOf(item.getTxn_details().getEarnings()));
				price.setTextColor(Color.GREEN);
			}

			name.setText(item.getTxn_details().getRecipient_name());
			time.setText(FieldsParsingUtils.getTime(PaymentHistoryListActivity.this, item.getCreated_at()));
			title.setText(item.getTitle());
			return rowView;
		}
		public View setUpWithdrawView(	View rowView, PaymentHistory item ){

				price.setText("-$"
						+ String.valueOf(item.getTxn_details()
						.getAmount_deposited()));
				price.setTextColor(Color.RED);

			name.setText(item.getTxn_details().getRecipient_name());
			time.setText(FieldsParsingUtils.getTime(PaymentHistoryListActivity.this, item.getCreated_at()));
			title.setText(item.getTitle());
			return rowView;
		}
		@Override
		public boolean hasStableIds() {
			return true;
		}

	}

}