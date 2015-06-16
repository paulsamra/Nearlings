package swipe.android.nearlings;

import android.support.v4.app.Fragment;

public class GroupsDetailsActivity extends FragmentFrameHolderActivity{

	@Override
	public Fragment getFragment() {
		return new GroupsDetailsFragment();
	}
	
}