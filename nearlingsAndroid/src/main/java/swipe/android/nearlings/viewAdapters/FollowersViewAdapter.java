package swipe.android.nearlings.viewAdapters;

import java.util.ArrayList;

import swipe.android.nearlings.NearlingsApplication;
import swipe.android.nearlings.R;
import swipe.android.nearlings.jsonResponses.register.Users;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

public class FollowersViewAdapter extends ArrayAdapter<Users> {
	View row;
	ArrayList<Users> myTeams;
	int resLayout;
	Context context;

	public FollowersViewAdapter(Context context, int textViewResourceId,
			ArrayList<Users> myTeams) {
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

		Users item = myTeams.get(position);
		ImageView searchOptionsIcon = (ImageView) row
				.findViewById(R.id.users_item_thumbnail);
		
		TextView userName = (TextView) row
				.findViewById(R.id.users_item_name);
		userName.setText(item.getName());
		ImageLoader.getInstance().displayImage(
				item.getIconURL(), searchOptionsIcon,
				NearlingsApplication.getDefaultOptions());

		return row;
	}

}