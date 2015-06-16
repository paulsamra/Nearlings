package com.edbert.library.containers;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;

public class TagTabListener implements ActionBar.TabListener{
    private Fragment fragment;
private ActionBarActivity c;
private String tag;
    public TagTabListener(Fragment fragment, ActionBarActivity c, String tag){
        this.fragment = fragment;
        this.tag = tag;
        this.c = c;
    }

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction fragmentTransaction) {
		FragmentManager manager = c.getSupportFragmentManager();
		fragmentTransaction = manager.beginTransaction();

		

		//fragment = navDrawerSelected.getFragment();
		// replace the fragment once found
		if (fragment != null) {
			// save instances and pop stacks to save previous instances
			FragmentManager fragmentManager = c.getSupportFragmentManager();

			String backStateName = fragment.getClass().getName();
			boolean fragmentPopped = fragmentManager.popBackStackImmediate(
					backStateName, 0);

			if (!fragmentPopped
					&& fragmentManager.findFragmentByTag(backStateName) == null) {

				FragmentTransaction ft = fragmentManager.beginTransaction();

				ft.replace(android.R.id.content, fragment, backStateName);
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

				ft.addToBackStack(backStateName);
				ft.commit();

			}
		}

		fragmentTransaction.replace(android.R.id.content, fragment);
		fragmentTransaction.commit();
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {

	}

}