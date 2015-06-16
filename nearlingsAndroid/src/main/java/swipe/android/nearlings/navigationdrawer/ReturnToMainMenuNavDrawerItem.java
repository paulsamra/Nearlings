package swipe.android.nearlings.navigationdrawer;

import swipe.android.nearlings.HomeActivity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.edbert.library.navigationdrawer.NavDrawerItem;

class ReturnToMainMenuNavDrawerItem extends NavDrawerItem {
	private static final String TITLE = "Return";


	public ReturnToMainMenuNavDrawerItem() {
		super(TITLE);
	}

	@Override
	public Fragment getFragment() {
		return null;
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
		
		Intent i = new Intent(c, HomeActivity.class);
		// Closing all the Activities
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		// Add new Flag to start new Activity
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		// Staring Login Activity
		c.startActivity(i);
	}
}