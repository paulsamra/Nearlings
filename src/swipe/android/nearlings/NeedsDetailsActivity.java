package swipe.android.nearlings;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.widget.FrameLayout;

public class NeedsDetailsActivity extends ActionBarActivity implements TabListener {
	String id = "0";
	Map<String, Fragment> mapFragList = new LinkedHashMap<String, Fragment>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);

		mapFragList.put("Details", new NeedsDetailsFragment());
		ActionBar bar = this.getSupportActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		Bundle b = getIntent().getExtras();
		id = b.getString("id");
		/*
		 * mapFragList.put("Followers", new NeedsFollowersFragment());
		 * mapFragList.put("Offers", new NeedsOffersFragment());
		 */
		Iterator it = mapFragList.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();

			Tab tab = bar.newTab();
			tab.setText((String) pairs.getKey());

			tab.setTabListener(this);
			bar.addTab(tab);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		return true;
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		Fragment tf =   new NeedsDetailsFragment();
//when is this called? this is a null
		//if (mapFragList.size() > tab.getPosition())
		Bundle data = new Bundle();
		data.putString("id", id);
		tf.setArguments(data);

		ft.replace(android.R.id.content, tf);

	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {

	}

}