package swipe.android.nearlings;

import java.util.Map;

import swipe.android.nearlings.jsonResponses.login.JsonLoginResponse;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.edbert.library.network.AsyncTaskCompleteListener;
import com.edbert.library.network.PostDataWebTask;
import com.edbert.library.utils.MapUtils;

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
		if (result != null && result.isValid()) {
			SessionManager.getInstance(this.getActivity()).setIsLoggedIn(true);
			SessionManager.getInstance(this.getActivity()).setUserName(
					String.valueOf(result.getUserID()));
			SessionManager.getInstance(this.getActivity()).setUserID(
					String.valueOf(result.getUserID()));
			SessionManager.getInstance(this.getActivity()).setAuthToken(
					result.getToken());

			Intent intent = new Intent(this.getActivity(), MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			this.getActivity().startActivity(intent);
			this.getActivity().finish();
		}else{
			//display error
		}
	}

	public void login() {
		Map<String, String> headers = SessionManager.getInstance(this.getActivity()).defaultSessionHeaders();

		headers.put("username", "ramsin");
		headers.put("password", "ramsin");
		new PostDataWebTask<JsonLoginResponse>(this.getActivity(), this,
				JsonLoginResponse.class).execute(
				SessionManager.getInstance(this.getActivity()).loginURL(),
				MapUtils.mapToString(headers));
	}

	protected void initializeButtons() {
		Button loginButton = (Button) rootView.findViewById(R.id.parse_login_button);

		loginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				login();
			}
		});
	}

}