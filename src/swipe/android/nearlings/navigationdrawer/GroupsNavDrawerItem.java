package swipe.android.nearlings.navigationdrawer;

import swipe.android.nearlings.DetailFragment;

import com.edbert.library.navigationdrawer.NavDrawerItem;

import android.support.v4.app.Fragment;

public class GroupsNavDrawerItem extends NavDrawerItem {
private static final String TITLE = "Groups";
	public GroupsNavDrawerItem(int icon, boolean isCounterVisible, int count) {
		super(TITLE, icon, isCounterVisible, count);
		
	}

	public GroupsNavDrawerItem(String title, int icon) {
		super(title, icon);
	}
	public GroupsNavDrawerItem(String title) {
		super(title);
	}
	public GroupsNavDrawerItem() {
		super(TITLE);
	}
	@Override
	public Fragment getFragment() {
		return new DetailFragment();
	}


	@Override
	public Type getNavDrawerType() {
		return Type.ITEM_TYPE;
	}


}