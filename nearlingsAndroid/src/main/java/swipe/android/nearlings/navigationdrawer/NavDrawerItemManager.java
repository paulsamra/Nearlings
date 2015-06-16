package swipe.android.nearlings.navigationdrawer;

import com.edbert.library.navigationdrawer.AbstractNavDrawerItemManager;

public class NavDrawerItemManager extends AbstractNavDrawerItemManager {
	private static NavDrawerItemManager navDrawerManagerInstance;

	public static NavDrawerItemManager getInstance() {
		if (navDrawerManagerInstance == null) {
			navDrawerManagerInstance = new NavDrawerItemManager();
		}
		return navDrawerManagerInstance;
	}

	@Override
	public void setDefaultLayout() {

		navDrawerButtonArrayList.clear();
		register(new NeedsNavDrawerItem());

		register(new LoginNavDrawerItem());
		
		
		register(new DashboardNavDrawerItem());
		register(new NeedsNavDrawerItem());

		register(new EventsNavDrawerItem());
		register(new GroupsNavDrawerItem());
		register(new MessagesNavDrawerItem());
	}
	
	
	public void setCurrentlySignedInLayout() {
		navDrawerButtonArrayList.clear();
		/*
		register(new ProfileNavDrawerItem());
		register(new DiscoverNavDrawerItem());
		register(new DashboardNavDrawerItem());*/
		register(new NeedsNavDrawerItem());

		register(new EventsNavDrawerItem());
		register(new GroupsNavDrawerItem());
		register(new ReturnToMainMenuNavDrawerItem());
		/*
		register(new MessagesNavDrawerItem());
		register(new LogoutNavDrawerItem());*/
	}
}