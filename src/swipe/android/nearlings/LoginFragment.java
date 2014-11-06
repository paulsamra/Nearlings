package swipe.android.nearlings;

import com.edbert.library.network.AsyncTaskCompleteListener;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;

public class LoginFragment extends Fragment implements
		AsyncTaskCompleteListener<JsonLoginResponse> {

	protected View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		rootView = inflater.inflate(getLayoutResourceID(), container, false);

		initializeButtons();
		return rootView;
	}

	protected int getLayoutResourceID() {
		return R.layout.login_form;
	}

	public void onTaskComplete(JsonLoginResponse result) {
	//	if (result != null && result.isValid()) {

			SessionManager.getInstance(this.getActivity()).setIsLoggedIn(true);
		 //SessionManager.getInstance(this.getActivity()).setUserName();
			//  SessionManager.getInstance(this.getActivity()).setAuthToken();
			
			// now return
			Intent intent = new Intent(this.getActivity(), MainActivity.class);
			this.getActivity().startActivity(intent);
			this.getActivity().finish();
			// userDataViewAdapter.validateAddressViews();
//		}
	}

	public void login() {
		/*
		 * new PostDataWebTask<JsonCreateAlertResponse>(this.getActivity(),
		 * this, JsonCreateAlertResponse.class).execute(SessionManager.BASE_URL
		 * + "/api/v1/alerts", MapUtils.mapToString(params), jsonString);
		 */
		new DummyWebTask<JsonLoginResponse>(this.getActivity(), this, JsonLoginResponse.class).execute("text","lol","text");
	}

	protected void initializeButtons() {
		Button loginButton = (Button) rootView.findViewById(R.id.loginButton);

		loginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.e("Click", "click");
				login();
			}
		});
	}

}