package swipe.android.nearlings.discover.options;

import java.util.ArrayList;

import swipe.android.nearlings.R;
import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

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
		if (row == null) {
			LayoutInflater ll = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = ll.inflate(resLayout, parent, false);
		}

		SearchOptionsFilter item = myTeams.get(position); // Produce a row for
															// each Team.
		ImageView searchOptionsIcon = (ImageView) row
				.findViewById(R.id.search_option_item_image);
		// need to check checked
		// imageView.setImageResource(itemToggled[position] ? R.drawable.on :
		// R.drawable.off);
		int icon = (item.isSelected() ? item.getSelectedIcon() : item
				.getUnselectedIcon());
		if(item.isSelected() && icon == -1){
			//if we selected and we dont have an icon, we hilight
			
			int tint = Color.parseColor("#ff0000");
			ColorFilter cf = new PorterDuffColorFilter(tint,Mode.OVERLAY);

			searchOptionsIcon.setImageResource(item.getUnselectedIcon());
			searchOptionsIcon.setColorFilter(cf);
					
		}else if (item.isSelected()){
		
			searchOptionsIcon.setImageResource(item.getSelectedIcon());
		}else{
			//if we're not selected, restore it
			searchOptionsIcon.setColorFilter(0xFFFFFFFF, Mode.MULTIPLY);
			searchOptionsIcon.setImageResource(item.getUnselectedIcon());
			//searchOptionsIcon.setColorFilter(cf);
		}
		return row;
	}

}