package swipe.android.nearlings.viewAdapters;

import java.util.ArrayList;

import swipe.android.nearlings.R;
import swipe.android.nearlings.SessionManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edbert.library.navigationdrawer.NavDrawerItemInterface;
import com.edbert.library.navigationdrawer.viewadapter.NavDrawerListAdapter;

public class NearlingsNavDrawerListAdapter extends NavDrawerListAdapter {

Context context;
	public NearlingsNavDrawerListAdapter(Context context,
			ArrayList<NavDrawerItemInterface> navDrawerItems) {
	super(context, navDrawerItems);
	this.context = context;
	}
	@Override
	protected View attachProfileHolder( LayoutInflater mInflater,  ViewGroup parent, View convertView,ViewHolder holder){
		convertView = mInflater.inflate(R.layout.nearlings_profile_navigation_item,
				parent, false);

		holder.title = (TextView) convertView
				.findViewById(R.id.profile_title);
		return convertView;
	}
@Override
protected void setUpProfileItem(ViewHolder holder, int position) {
	String username = SessionManager.getInstance(context).getUserName();
	if(!username.equals(""))
		holder.title.setText(username);
	}
}