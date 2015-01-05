package swipe.android.nearlings.navigationdrawer;

import swipe.android.nearlings.DetailFragment;
import swipe.android.nearlings.EventsContainerFragment;
import swipe.android.nearlings.EventsListFragment;

import com.edbert.library.navigationdrawer.NavDrawerItem;

import android.support.v4.app.Fragment;

public class EventsNavDrawerItem extends NavDrawerItem {
private static final String TITLE = "Events";
	public EventsNavDrawerItem(int icon, boolean isCounterVisible, int count) {
		super(TITLE, icon, isCounterVisible, count);
		
	}

	public EventsNavDrawerItem(String title, int icon) {
		super(title, icon);
	}
	public EventsNavDrawerItem(String title) {
		super(title);
	}
	public EventsNavDrawerItem() {
		super(TITLE);
	}
	@Override
	public Fragment getFragment() {
	return new EventsContainerFragment();//	return new EventsListFragment();
	}


	@Override
	public Type getNavDrawerType() {
		return Type.ITEM_TYPE;
	}


}