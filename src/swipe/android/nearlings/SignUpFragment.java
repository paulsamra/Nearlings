package swipe.android.nearlings;

import swipe.android.nearlings.jsonResponses.register.JsonRegisterResponse;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.edbert.library.network.AsyncTaskCompleteListener;

public class SignUpFragment extends Fragment implements
		AsyncTaskCompleteListener<JsonRegisterResponse> {

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
		return R.layout.register_form;
	}

	public void onTaskComplete(JsonRegisterResponse result) {
		// if (result != null && result.isValid()) {

		SessionManager.getInstance(this.getActivity()).setIsLoggedIn(true);
		// SessionManager.getInstance(this.getActivity()).setUserName();
		// SessionManager.getInstance(this.getActivity()).setAuthToken();

		// now return
		Intent intent = new Intent(this.getActivity(), MainActivity.class);
		this.getActivity().startActivity(intent);
		this.getActivity().finish();
		// userDataViewAdapter.validateAddressViews();
		// }
	}


	protected void initializeButtons() {
		Button loginButton = (Button) rootView.findViewById(R.id.loginButton);

		loginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				login();
			}
		});
	}

	public void login() {
		/*
		 * new PostDataWebTask<JsonCreateAlertResponse>(this.getActivity(),
		 * this, JsonCreateAlertResponse.class).execute(SessionManager.BASE_URL
		 * + "/api/v1/alerts", MapUtils.mapToString(params), jsonString);
		 */
		new DummyWebTask<JsonRegisterResponse>(this.getActivity(), this,
				JsonRegisterResponse.class).execute("text", "lol", "text");
	}
}