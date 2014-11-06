package swipe.android.nearlings.navigationdrawer;

import swipe.android.nearlings.DetailFragment;
import swipe.android.nearlings.LoginActivity;
import swipe.android.nearlings.MainActivity;
import swipe.android.nearlings.SessionManager;

import com.edbert.library.navigationdrawer.NavDrawerItem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class LogoutNavDrawerItem extends NavDrawerItem {
private static final String TITLE = "Logout";
	public LogoutNavDrawerItem(int icon, boolean isCounterVisible, int count) {
		super(TITLE, icon, isCounterVisible, count);
		
	}

	public LogoutNavDrawerItem(String title, int icon) {
		super(title, icon);
	}
	public LogoutNavDrawerItem(String title) {
		super(title);
	}
	public LogoutNavDrawerItem() {
		super(TITLE);
	}
	@Override
	public Fragment getFragment() {
		return new DetailFragment();
	}


	@Override
	public Type getNavDrawerType() {
		return Type.LOGOUT_TYPE;
	}
	@Override
	public boolean updateActionBarTitle() {
		return false;
	}

	@Override
	public void doAction(Context c){
		SessionManager.getInstance(c).setIsLoggedIn(false);
		//notfiy user of logged out?
		((MainActivity) c).reloadNavigationDrawer();
	}
}