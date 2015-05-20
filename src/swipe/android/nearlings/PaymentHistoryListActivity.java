package swipe.android.nearlings;

import java.util.ArrayList;
import java.util.List;

import swipe.android.nearlings.json.UserHistory.JsonTransactionHistoryResponse;
import swipe.android.nearlings.json.UserHistory.PaymentHistory;
import android.app.ListActivity;
import android.content.Context;
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
		setListAdapter(adapter);
	}
	// when an item of the list is clicked
	@Override
	protected void onListItemClick(ListView list, View view, int position,
			long id) {

	}

	private class TransactionAdapter extends ArrayAdapter<Parcelable> {

		int layout;

		public TransactionAdapter(Context context, int textViewResourceId,
				List<Parcelable> objects) {
			super(context, textViewResourceId, objects);
			this.layout = textViewResourceId;

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			PaymentHistory item = (PaymentHistory) getItem(position);
			LayoutInflater inflater = (LayoutInflater) super.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View rowView = inflater.inflate(layout, parent, false);
			TextView price = (TextView) rowView.findViewById(R.id.price);
			TextView name = (TextView) rowView.findViewById(R.id.name);
			TextView time = (TextView) rowView.findViewById(R.id.time);

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
			time.setText(FieldsParsingUtils.getTime(item.getCreated_at()));

			return rowView;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

	}

}