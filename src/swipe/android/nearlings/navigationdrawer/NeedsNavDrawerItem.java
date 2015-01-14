package swipe.android.nearlings.navigationdrawer;

import swipe.android.nearlings.DetailFragment;
import swipe.android.nearlings.needs.DiscoverContainerFragment;

import com.edbert.library.navigationdrawer.NavDrawerItem;

import android.support.v4.app.Fragment;

public class NeedsNavDrawerItem extends NavDrawerItem {
private static final String TITLE = "Needs";
	public NeedsNavDrawerItem(int icon, boolean isCounterVisible, int count) {
		super(TITLE, icon, isCounterVisible, count);
		
	}

	public NeedsNavDrawerItem(String title, int icon) {
		super(title, icon);
	}
	public NeedsNavDrawerItem(String title) {
		super(title);
	}
	public NeedsNavDrawerItem() {
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