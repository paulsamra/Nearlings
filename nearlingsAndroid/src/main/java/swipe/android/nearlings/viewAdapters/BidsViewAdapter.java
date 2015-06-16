package swipe.android.nearlings.viewAdapters;

import java.util.ArrayList;

import swipe.android.nearlings.FieldsParsingUtils;
import swipe.android.nearlings.R;
import swipe.android.nearlings.json.needs.needsdetailsoffersresponse.NeedsOffers;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class BidsViewAdapter extends ArrayAdapter<NeedsOffers> {
	View row;
	ArrayList<NeedsOffers> myTeams;
	int resLayout;
	Context context;

	public BidsViewAdapter(Context context, int textViewResourceId,
			ArrayList<NeedsOffers> myTeams) {
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

		NeedsOffers item = myTeams.get(position);
		TextView bidNumber = (TextView) row.findViewById(R.id.bid);
		TextView dateBid = (TextView) row.findViewById(R.id.time);
		TextView user = (TextView) row.findViewById(R.id.name);

		
		bidNumber.setText(String.valueOf(item.getOfferprice()));
		dateBid.setText(FieldsParsingUtils.getTime(this.context,item.getCreated_at()));
		user.setText(item.getUsername());

		return row;
	}

	public void sortArrayList() {
		// TODO: sort arraylist
	}

}