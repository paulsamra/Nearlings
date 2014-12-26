package swipe.android.nearlings;

import java.util.ArrayList;

import com.crashlytics.android.Crashlytics;
import com.edbert.library.navigationdrawer.AbstractNavDrawerItemManager;
import com.edbert.library.navigationdrawer.NavDrawerActivity;
import com.edbert.library.navigationdrawer.NavDrawerItemInterface;
import com.edbert.library.navigationdrawer.viewadapter.NavDrawerListAdapter;

import swipe.android.nearlings.navigationdrawer.NavDrawerItemManager;
import swipe.android.nearlings.viewAdapters.NearlingsNavDrawerListAdapter;
import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends NavDrawerActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Crashlytics.start(this);
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
public void setUpNavigationAdapter(){
	adapter = new NearlingsNavDrawerListAdapter(getApplicationContext(),
			navDrawerItems);
}
}
