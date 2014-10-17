package swipe.android.nearlings.navigationdrawer;

import swipe.android.nearlings.DetailFragment;
import swipe.android.nearlings.DiscoverContainerFragment;

import com.edbert.library.navigationdrawer.NavDrawerItem;

import android.support.v4.app.Fragment;

public class DiscoverNavDrawerItem extends NavDrawerItem {
private static final String TITLE = "Discover";
	public DiscoverNavDrawerItem(int icon, boolean isCounterVisible, int count) {
		super(TITLE, icon, isCounterVisible, count);
		
	}

	public DiscoverNavDrawerItem(String title, int icon) {
		super(title, icon);
	}
	public DiscoverNavDrawerItem(String title) {
		super(title);
	}
	public DiscoverNavDrawerItem() {
		super(TITLE);
	}
	@Override
	public Fragment getFragment() {
		return new DiscoverContainerFragment();
	}


	@Override
	public Type getNavDrawerType() {
		return Type.ITEM_TYPE;
	}


}