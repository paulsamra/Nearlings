package swipe.android.nearlings;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar.Tab;

import com.edbert.library.containers.TabsActivityContainer;

public class LoginActivity extends TabsActivityContainer {

	@Override
	protected void addDefaultFragments() {
		mapFragList.put("Login", new LoginFragment());
		mapFragList.put("Sign Up", new SignUpFragment());
	}


}