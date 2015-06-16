package swipe.android.nearlings;

import android.support.v4.app.Fragment;

public class MessagesActivity extends FragmentFrameHolderActivity{

	@Override
	public Fragment getFragment() {
		// TODO Auto-generated method stub
		return new MessagesFragment();
	}
	
}