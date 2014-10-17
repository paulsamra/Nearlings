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

public class SearchFilterCategoryOptionsListAdapter extends
		ArrayAdapter<SearchOptionsFilter> {
	View row;
	ArrayList<SearchOptionsFilter> myTeams;
	int resLayout;
	Context context;

	public SearchFilterCategoryOptionsListAdapter(Context context,
			int textViewResourceId, ArrayList<SearchOptionsFilter> myTeams) {
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

		SearchOptionsFilter item = myTeams.get(position); // Produce a row for
															// each Team.
		ImageView searchOptionsIcon = (ImageView) row
				.findViewById(R.id.search_option_item_image);
		// need to check checked
		if (!item.isSelected()) {
			searchOptionsIcon.setImageResource(item.getUnselectedIcon());
		} else {
			searchOptionsIcon.setImageResource(item.getSelectedIcon());
		}

		return row;
	
	}
	private static class ViewHolder {
	    ImageView searchOptionsIcon;
	}
}