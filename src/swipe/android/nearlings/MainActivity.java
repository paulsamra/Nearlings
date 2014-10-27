package swipe.android.nearlings;

import com.crashlytics.android.Crashlytics;
import com.edbert.library.navigationdrawer.NavDrawerActivity;

import swipe.android.nearlings.navigationdrawer.NavDrawerItemManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends NavDrawerActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Crashlytics.start(this);
	}
	@Override
	public void setUpNavDrawerTiles() {
		NavDrawerItemManager.getInstance().setDefaultLayout();
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
		//this should just be a static refrence
		navDrawerManager = NavDrawerItemManager.getInstance();
	}


}
