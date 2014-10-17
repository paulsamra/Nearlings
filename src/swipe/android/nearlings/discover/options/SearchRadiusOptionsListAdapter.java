package swipe.android.nearlings.discover.options;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

import swipe.android.nearlings.R;
import uk.co.senab.photup.views.CheckableImageView;

public class SearchRadiusOptionsListAdapter extends
		ArrayAdapter<SearchRadiusFilter> {
	View row;
	ArrayList<SearchRadiusFilter> myTeams;
	int resLayout;
	Context context;

	public SearchRadiusOptionsListAdapter(Context context,
			int textViewResourceId, ArrayList<SearchRadiusFilter> myTeams) {
		super(context, textViewResourceId, myTeams);
		this.myTeams = myTeams;
		resLayout = textViewResourceId;
		this.context = context;
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	row = convertView;
		if (row == null) { // inflate our custom layout. resLayout ==
							// R.layout.row_team_layout.xml
			LayoutInflater ll = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = ll.inflate(resLayout, parent, false);
		}

		SearchRadiusFilter item = myTeams.get(position); // Produce a row for
															// each Team.
		TextView searchOptionsIcon = (TextView) row
				.findViewById(R.id.search_options_radius_text);
		// need to check checked
		if (!item.isSelected()) {
			searchOptionsIcon.setText(item.getTag());
		} else {
			searchOptionsIcon.setText(item.getTag());
		}

		return row;
		
	}
	private static class ViewHolder {
	    TextView searchOptionsIcon;
	}
}