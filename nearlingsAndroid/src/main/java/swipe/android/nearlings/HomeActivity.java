package swipe.android.nearlings;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
	//    ActionBar actionBar = this.getSupportActionBar();

		getSupportActionBar().setHomeButtonEnabled(false);
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		
		LayoutInflater mInflater = LayoutInflater.from(this);
		View mCustomView = mInflater.inflate(R.layout.home_action_bar, null);
		getSupportActionBar().setDisplayUseLogoEnabled(false);
		
		ImageView iconOfAlert = (ImageView) mCustomView.findViewById(R.id.action_bar_icon);
		TextView titleOfAlert = (TextView) mCustomView.findViewById(R.id.action_bar_title);
		iconOfAlert.setImageResource(R.drawable.nearlings_32x_icon);
		titleOfAlert.setText("Dashboard");


		android.support.v7.app.ActionBar.LayoutParams lp = new android.support.v7.app.ActionBar.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);

		getSupportActionBar().setCustomView(mCustomView, lp);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		//requestWindowFeature(Window.FEATURE_ACTION_BAR);
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
				c = UserSettingsActivity.class;
				break;
			case 6:
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