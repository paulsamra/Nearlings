package swipe.android.nearlings;
/*
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LoginFragment extends Fragment{
@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		userDataViewAdapter = new UserDataViewAdapter(rootView);

		initializeButtons();
	}

	@Override
	protected int getLayoutResourceID() {
		return R.layout.login_form;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == REQUEST_CODE_SELECT_ADDRESS_IN_MAP
				&& resultCode == RESULT_OK && data != null) {
			//if we are ok, we will populate the shared prefrences and
			//return back to the main activity.
			SessionManager.getInstance().setIsLogggedIn(true);
			SessionManager.getInstance().setUserName();
			SessionManager.getInstance().setAuthToken();
			
			//now return
			Intent intent = new Intent(this.getActivity(), MainActivity.class);
			this.getActivity().startActivity(intent);
			this.getActivity().finish();
			//userDataViewAdapter.validateAddressViews();
		}
	}

	// -----------------------------------------------------------------
	// private / protected methods
	// -----------------------------------------------------------------

	protected void initializeButtons() {
		Button loginButton = (Button) findViewById(R.id.loginButton);

		loginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//saveForm();
				login();
			}
		});
	}

}*/