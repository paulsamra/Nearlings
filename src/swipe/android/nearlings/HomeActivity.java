package swipe.android.nearlings;

import swipe.android.nearlings.MessagesSync.NearlingsSyncHelper;
import android.app.ActionBar;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.throrinstudio.android.library.widgets.dashboard.DashBoardElement.OnClickListener;
import com.throrinstudio.android.library.widgets.dashboard.DashBoardLayout;

public class HomeActivity extends ActionBarActivity {
	DashBoardLayout dashboard;
	int width = 0;
	static int height = 0;

	public void logout() {
		NearlingsApplication nap = (NearlingsApplication) this
				.getApplicationContext();
		nap.logout();
	}
	private void setupActionBar() {
	//    ActionBar actionBar = this.getActionBar();
	  
	    getActionBar().setHomeButtonEnabled(false);
		getActionBar().setDisplayHomeAsUpEnabled(false);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayShowTitleEnabled(false);
		
		LayoutInflater mInflater = LayoutInflater.from(this);
		View mCustomView = mInflater.inflate(R.layout.home_action_bar, null);
		getActionBar().setDisplayUseLogoEnabled(false);
		
		ImageView iconOfAlert = (ImageView) mCustomView.findViewById(R.id.action_bar_icon);
		TextView titleOfAlert = (TextView) mCustomView.findViewById(R.id.action_bar_title);
		iconOfAlert.setImageResource(R.drawable.nearlings_32x_icon);
		titleOfAlert.setText("Dashboard");

		
		ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);

		getActionBar().setCustomView(mCustomView, lp);
		getActionBar().setDisplayShowCustomEnabled(true);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_select_activity);
		Resources res = getResources();
		TypedArray icons = res.obtainTypedArray(R.array.home_options_icon);
		String[] home_options = res.getStringArray(R.array.home_options);
		// int[] home_icons = new int[icons.length()];
		dashboard = (DashBoardLayout) findViewById(R.id.grid);

		for (int i = 0; i < icons.length(); i++) {
			// home_icons[i] = icons.getResourceId(i, 0);
			dashboard.addElement(icons.getResourceId(i, 0), home_options[i],
					testListener);
		}
		setupActionBar();
	}

	private OnClickListener testListener = new OnClickListener() {

		@Override
		public void onClick(View v, int position) {
			
			Class c = null;

			switch (position) {
			case 0:
				c = MessagesActivity.class;
				break;
			case 1:
				c = MainActivity.class;
				break;
			case 2:
				c = CreateNeedActivity.class;
				break;
			case 3:
				c = CreateEventActivity.class;
				break;
			case 4:
				c = CreateGroupActivity.class;
				break;
			case 5:
				HomeActivity.this.logout();
				break;

			}
			if (c != null) {
				Intent intent = new Intent(HomeActivity.this, c);
				HomeActivity.this.startActivity(intent);
			}
		}

	};
}