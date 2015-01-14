package swipe.android.nearlings;

import swipe.android.nearlings.navigationdrawer.NavDrawerItemManager;
import swipe.android.nearlings.viewAdapters.NearlingsNavDrawerListAdapter;
import android.os.Bundle;

import com.edbert.library.navigationdrawer.NavDrawerActivity;

public class MainActivity extends NavDrawerActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Crashlytics.start(this);
	}

	@Override
	public void setUpNavDrawerTiles() {
		if (SessionManager.getInstance(this).isLoggedIn()) {
			NavDrawerItemManager.getInstance().setCurrentlySignedInLayout();

		} else {
			NavDrawerItemManager.getInstance().setDefaultLayout();
		}
		if (adapter != null) {
			adapter.notifyDataSetInvalidated();
		}
	}

	@Override
	protected void setUpNavDrawerHeader() {
		return;
	}

	@Override
	protected void setUpNavDrawerFooter() {
		return;
	}

	@Override
	protected void setNavDrawerManager() {
		// this should just be a static refrence
		navDrawerManager = NavDrawerItemManager.getInstance();
	}

	@Override
	public void reloadNavigationDrawer() {
		setUpNavDrawerTiles();
		setUpNavDrawerHeader();
		setUpNavDrawerFooter();
		super.reloadNavigationDrawer();

		// adapter.notifyDataSetInvalidated();
	}

	@Override
	public void onBackPressed() {
		this.finish();
	}

	@Override
	public void setUpNavigationAdapter() {
		adapter = new NearlingsNavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
	}

	protected void displayView(int position) {
		super.displayView(position);
	
		if (navDrawerItems.size() > position)
			super.setTitle(navDrawerItems.get(position).getTitle());
	}
}
