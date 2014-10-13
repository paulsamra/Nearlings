package swipe.android.nearlings.navigationdrawer;

import swipe.android.nearlings.DetailFragment;

import com.edbert.library.navigationdrawer.NavDrawerItem;

import android.support.v4.app.Fragment;

public class LoginNavDrawerItem extends NavDrawerItem {
private static final String TITLE = "Logout";
	public LoginNavDrawerItem(int icon, boolean isCounterVisible, int count) {
		super(TITLE, icon, isCounterVisible, count);
		
	}

	public LoginNavDrawerItem(String title, int icon) {
		super(title, icon);
	}
	public LoginNavDrawerItem(String title) {
		super(title);
	}
	public LoginNavDrawerItem() {
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

}