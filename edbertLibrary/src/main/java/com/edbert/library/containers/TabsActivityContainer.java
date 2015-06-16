package com.edbert.library.containers;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;

import com.edbert.library.R;

public abstract class TabsActivityContainer extends ActionBarActivity implements
		TabListener {
	String id = "0";
	protected PositionalLinkedMap<String, Fragment> mapFragList = new PositionalLinkedMap<String, Fragment>();

	protected abstract void addDefaultFragments();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		addDefaultFragments();
		super.onCreate(savedInstanceState);
		ActionBar bar = this.getSupportActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

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
	public void onTabReselected(Tab tab, FragmentTransaction ft) {

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction fragmentTransaction) {
		FragmentManager manager = getSupportFragmentManager();
		fragmentTransaction = manager.beginTransaction();

		// need current fragment?
		Fragment fragment = mapFragList.getValue(tab.getPosition());

		// fragment = navDrawerSelected.getFragment();
		// replace the fragment once found
		if (fragment != null) {
			// save instances and pop stacks to save previous instances
			FragmentManager fragmentManager = getSupportFragmentManager();

			//
			String backStateName = createTag(fragment);
			boolean fragmentPopped = fragmentManager.popBackStackImmediate(
					backStateName, 0);

			if (!fragmentPopped
					&& fragmentManager.findFragmentByTag(backStateName) == null) {

				FragmentTransaction ft = fragmentManager.beginTransaction();

				//ft.replace(android.R.id.content, fragment, backStateName);

				ft.replace(R.id.frame_container, fragment, backStateName);
				
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

				ft.addToBackStack(backStateName);
				ft.commit();

			}
		}

	//	fragmentTransaction.replace(android.R.id.content, fragment);
		fragmentTransaction.replace(R.id.frame_container, fragment);
		fragmentTransaction.commit();
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {

	}

	protected String createTag(Fragment fragment) {
		return fragment.getClass().getName();
	}
	
	@Override
	public void onBackPressed(){
		this.finish();
	}

}