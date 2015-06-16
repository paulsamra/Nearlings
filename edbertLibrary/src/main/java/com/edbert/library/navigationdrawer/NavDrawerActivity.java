package com.edbert.library.navigationdrawer;

import java.util.ArrayList;

import android.app.ActionBar;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.edbert.library.R;
import com.edbert.library.navigationdrawer.NavDrawerItemInterface.Type;
import com.edbert.library.navigationdrawer.viewadapter.NavDrawerListAdapter;

public abstract class NavDrawerActivity extends ActionBarActivity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	public AbstractNavDrawerItemManager navDrawerManager;
	// used to store app title
	private CharSequence mTitle;

	protected ArrayList<NavDrawerItemInterface> navDrawerItems;
	protected NavDrawerListAdapter adapter;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTitle = getTitle();

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		setNavDrawerManager();
		navDrawerItems = navDrawerManager.getNavDrawerItems();

		// Set up
		setUpNavDrawerTiles();
		setUpNavDrawerHeader();
		setUpNavDrawerFooter();
		
		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		setUpNavigationAdapter();
		if(adapter == null){
			adapter = new NavDrawerListAdapter(getApplicationContext(),
					navDrawerItems);
		}

		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
	/*	getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
*/
		setActionBar();
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,

				// menu
				// toggle
				// icon
				R.string.app_name, // nav drawer open - description for
									// accessibility
				R.string.app_name // nav drawer close - description for
									// accessibility
		) {
			public void onDrawerClosed(View view) {
				getSupportActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
			 invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
			invalidateOptionsMenu();
			}
		};

		mDrawerToggle.syncState();
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		mDrawerToggle.syncState();
		if (savedInstanceState == null) {
			// on first time display view for first nav item
			ArrayList<NavDrawerItemInterface> navDrawer=  navDrawerManager.getNavDrawerItems();
			int i = 0;
			while(navDrawer.get(i).getNavDrawerType().equals(Type.PROFILE_TYPE) && i < navDrawer.size()){
				i++;
			}
			displayView(i);
		}

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click

		return super.onOptionsItemSelected(item);

	}

	/***
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void setTitle(CharSequence title) {
		if(title != null){
			mTitle = title;

			getSupportActionBar().setTitle(mTitle);
		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	protected abstract void setUpNavDrawerHeader();

	protected abstract void setUpNavDrawerFooter();

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	protected void displayView(int position) {
		NavDrawerItem navDrawerSelected = (NavDrawerItem) navDrawerManager
				.getNavDrawerItems().get(position);
		Fragment fragment = null;

		// if we want to log out, we will terminate it right here
		if (navDrawerSelected.getNavDrawerType().equals(Type.LOGOUT_TYPE)) {

			navDrawerSelected.doAction(this);
			return;
		}

		navDrawerSelected.doAction(this);

		fragment = navDrawerSelected.getFragment();
		// replace the fragment once found
		if (fragment != null) {
			
			// save instances and pop stacks to save previous instances. 
					FragmentManager fragmentManager = getSupportFragmentManager();
		String backStateName = fragment.getClass().getName();
			boolean fragmentPopped = fragmentManager.popBackStackImmediate(
					backStateName, 0);
if(fragmentPopped || fragmentManager.findFragmentByTag(backStateName) != null){
    while (fragmentManager.getBackStackEntryCount() != 0) {
    	fragmentManager.popBackStackImmediate();
    }
}

FragmentTransaction ft = fragmentManager.beginTransaction();

ft.replace(R.id.frame_container, fragment, backStateName);
ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);

ft.addToBackStack(backStateName);
ft.commit();
		/*	if (!fragmentPopped
					&& fragmentManager.findFragmentByTag(backStateName) == null) {

				FragmentTransaction ft = fragmentManager.beginTransaction();

				ft.replace(R.id.frame_container, fragment, backStateName);
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

				ft.addToBackStack(backStateName);
				ft.commit();

			}*//*else {
				//fuck it we clear all of them
				
				
			}*/
			
		/*	FragmentManager fragmentManager = getSupportFragmentManager();
			String backStateName = fragment.getClass().getName();
			boolean fragmentPopped = fragmentManager.popBackStackImmediate(backStateName, 0);
			
			FragmentTransaction ft = fragmentManager.beginTransaction();
			if (!fragmentPopped) {
			            ft.replace(R.id.frame_container, fragment);
			}
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			ft.addToBackStack(backStateName);
			ft.commit();*/
			
			
			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			if (navDrawerSelected.updateActionBarTitle()){
				setTitle(navDrawerSelected.getSupportActionBarTitle());
				Log.d("title","Updating actionbar title");
			
			}else{
			Log.d("title","Could not update action bar title");
			}
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (navDrawerItems.get(position).getNavDrawerType()
					.equals(Type.PROFILE_TYPE)) {
				return;
			}
			displayView(position);
		}
	}

	protected abstract void setNavDrawerManager();

	protected abstract void setUpNavDrawerTiles();

	public void reloadNavigationDrawer() {
		mDrawerLayout.closeDrawers();
		navDrawerItems = navDrawerManager.getNavDrawerItems();
		setUpNavigationAdapter();
		if(adapter == null){
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		}
		mDrawerList.setAdapter(adapter);

		synchronized(mDrawerList){
			adapter.notifyDataSetInvalidated();
		
			mDrawerList.notifyAll();
		
		}
		synchronized(navDrawerManager){

			navDrawerManager.notifyAll();
		}
		mDrawerLayout.invalidate();

	}
	
	public void openDrawer(){
		mDrawerLayout.openDrawer(GravityCompat.START);
	}
	public void closeDrawer(){
		mDrawerLayout.closeDrawer(GravityCompat.START);
	}
	
	public void toggleDrawer(){
		if(mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
			closeDrawer();
		}else{
			openDrawer();
		}
	}
	protected void setUpNavigationAdapter(){
		adapter = null;
	}
	
	private void setActionBar() {
		getSupportActionBar().setDisplayShowTitleEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
	}
	protected int getHomeIcon(){
		return R.drawable.ic_drawer;
	}
}
