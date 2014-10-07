package swipe.android.nearlings.navigationdrawer;

import swipe.android.nearlings.DetailFragment;

import com.edbert.library.navigationdrawer.NavDrawerItem;

import android.support.v4.app.Fragment;

public class ProfileNavDrawerItem extends NavDrawerItem {
private static final String TITLE = "Profile";
	public ProfileNavDrawerItem(int icon, boolean isCounterVisible, int count) {
		super(TITLE, icon, isCounterVisible, count);
	}

	public ProfileNavDrawerItem(String title, int icon) {
		super(title, icon);
	}
	public ProfileNavDrawerItem(String title) {
		super(title);
	}
	public ProfileNavDrawerItem() {
		super(TITLE);
	}
	@Override
	public Fragment getFragment() {
		return null;
	}

	@Override
	public Type getNavDrawerType() {
		return Type.PROFILE_TYPE;
	}


}