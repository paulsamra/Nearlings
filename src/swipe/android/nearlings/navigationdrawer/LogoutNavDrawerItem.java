package swipe.android.nearlings.navigationdrawer;

import java.util.Map;

import swipe.android.nearlings.DetailFragment;
import swipe.android.nearlings.LoginActivity;
import swipe.android.nearlings.MainActivity;
import swipe.android.nearlings.NearlingsApplication;
import swipe.android.nearlings.NearlingsContentProvider;
import swipe.android.nearlings.R;
import swipe.android.nearlings.SessionManager;
import swipe.android.nearlings.MessagesSync.NearlingsSyncHelper;
import swipe.android.nearlings.jsonResponses.login.JsonLoginResponse;

import com.edbert.library.navigationdrawer.NavDrawerItem;
import com.edbert.library.network.PostDataWebTask;
import com.edbert.library.utils.MapUtils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class LogoutNavDrawerItem extends NavDrawerItem {
	private static final String TITLE = "Logout";

	public LogoutNavDrawerItem(int icon, boolean isCounterVisible, int count) {
		super(TITLE, icon, isCounterVisible, count);

	}

	public LogoutNavDrawerItem(String title, int icon) {
		super(title, icon);
	}

	public LogoutNavDrawerItem(String title) {
		super(title);
	}

	public LogoutNavDrawerItem() {
		super(TITLE);
	}

	@Override
	public Fragment getFragment() {
		return new DetailFragment();
	}

	@Override
	public Type getNavDrawerType() {
		return Type.LOGOUT_TYPE;
	}

	@Override
	public boolean updateActionBarTitle() {
		return false;
	}

	@Override
	public void doAction(Context c) {
		/*
		 * Map<String, String> headers =
		 * SessionManager.getInstance(c).defaultSessionHeaders();
		 * 
		 * new PostDataWebTask<JsonLoginResponse>(c, null,
		 * JsonLoginResponse.class).execute(
		 * SessionManager.getInstance(c).loginURL(),
		 * MapUtils.mapToString(headers));
		 */
		// stop all requests
		NearlingsApplication nap = (NearlingsApplication ) c.getApplicationContext();
		NearlingsSyncHelper nsh = nap.getSyncHelper();
	
		ContentResolver.cancelSync(
				nsh.getAccount(),
				nsh.getAuthority());

		// clear all data tables
		NearlingsContentProvider a = new NearlingsContentProvider();
		a.clearAllTables();

		// reset to neutral
		SessionManager.getInstance(c).resetTables();

		// need to clear all userpref
		SessionManager.getInstance(c).clearUserPref();

		// notfiy user of logged out?
		((MainActivity) c).reloadNavigationDrawer();
		Intent i = new Intent(c, LoginActivity.class);
		// Closing all the Activities
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		// Add new Flag to start new Activity
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		// Staring Login Activity
		c.startActivity(i);
	}
}