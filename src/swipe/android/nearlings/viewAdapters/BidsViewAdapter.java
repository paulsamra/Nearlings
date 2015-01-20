package swipe.android.nearlings.viewAdapters;

import java.util.ArrayList;

import swipe.android.nearlings.R;
import swipe.android.nearlings.jsonResponses.register.Bids;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class BidsViewAdapter extends ArrayAdapter<Bids> {
	View row;
	ArrayList<Bids> myTeams;
	int resLayout;
	Context context;

	public BidsViewAdapter(Context context, int textViewResourceId,
			ArrayList<Bids> myTeams) {
		super(context, textViewResourceId, myTeams);
		this.myTeams = myTeams;
		resLayout = textViewResourceId;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		row = convertView;
		if (row == null) {
			LayoutInflater ll = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = ll.inflate(resLayout, parent, false);
		}

		Bids item = myTeams.get(position);
		TextView bidNumber = (TextView) row.findViewById(R.id.bid);
		TextView dateBid = (TextView) row.findViewById(R.id.time);

		TextView user = (TextView) row.findViewById(R.id.name);

		bidNumber.setText(String.valueOf(item.getBid()));
		dateBid.setText(item.getDate());
		user.setText(item.getBidder().getName());

		return row;
	}

	public void sortArrayList() {
		// TODO: sort arraylist
	}

}