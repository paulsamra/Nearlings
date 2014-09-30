package com.edbert.library.navigationdrawer;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

public interface NavDrawerItemInterface {
	public enum Type {
		LOGOUT_TYPE, SECTION_TYPE, ITEM_TYPE, PROFILE_TYPE
		}
	
	public void launchActivity(Context c);

	public Fragment getFragment();

	public boolean updateActionBarTitle();
	
	public String actionBarTitle();
	

}