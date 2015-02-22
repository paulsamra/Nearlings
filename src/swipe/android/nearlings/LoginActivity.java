package swipe.android.nearlings;

import java.util.Map;
import java.util.StringTokenizer;

import swipe.android.nearlings.json.jsonUserDetailsResponse.JsonUserDetailsResponse;
import swipe.android.nearlings.json.needs.comments.JsonCommentsResponse;
import swipe.android.nearlings.jsonResponses.login.JsonLoginResponse;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.edbert.library.network.AsyncTaskCompleteListener;
import com.edbert.library.network.GetDataWebTask;
import com.edbert.library.network.PostDataWebTask;
import com.edbert.library.utils.MapUtils;

public class LoginActivity extends Activity implements
		AsyncTaskCompleteListener {

	Button signup, login, forgotPassword;
	EditText usernameET, passwordET;
	boolean isLoginFormVisible = true;
	LinearLayout loginForm, signupForm;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.login_form);
		if (SessionManager.getInstance(this).isLoggedIn()) {
			goToNextActivity();
		}
		getActionBar().hide();
		usernameET = (EditText) findViewById(R.id.login_username_input);
		passwordET = (EditText) findViewById(R.id.login_password_input);
		loginForm = (LinearLayout) findViewById(R.id.login_insert);
		signupForm = (LinearLayout) findViewById(R.id.signup_insert);
		loginForm.setVisibility(View.VISIBLE);
		signupForm.setVisibility(View.GONE);
		forgotPassword = (Button) findViewById(R.id.parse_login_help);
		forgotPassword.setVisibility(View.VISIBLE);

		login = (Button) findViewById(R.id.parse_login_button);
		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isLoginFormVisible) {
					doLogin();
				} else {
					switchToLoginForm();
				}
			}

		});
		signup = (Button) findViewById(R.id.parse_signup_button);
		signup.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isLoginFormVisible) {
					switchToSignupForm();
				} else {
					doSignUp();
				}
			}
		});
		signup.setText("Sign Up");
	}

	// true is go to login form, false is go to signup
	public void switchToLoginForm() {
		signupForm.setVisibility(View.GONE);
		loginForm.setVisibility(View.VISIBLE);
		isLoginFormVisible = true;
		forgotPassword.setVisibility(View.VISIBLE);
		signup.setText("Sign Up");

	}

	public void switchToSignupForm() {
		loginForm.setVisibility(View.GONE);
		signupForm.setVisibility(View.VISIBLE);
		isLoginFormVisible = false;
		forgotPassword.setVisibility(View.GONE);
		signup.setText("Create Account");
	}

	public void doSignUp() {

	}

	public void onTaskComplete(Object result) {
		if (result == null) {
			NearlingsApplication.displayNetworkNotAvailableDialog(this);
		} else if (result instanceof JsonLoginResponse) {
			JsonLoginResponse loginResult = (JsonLoginResponse) result;
			if (!loginResult.isValid()) {
				String loginTitle = this.getString(R.string.login_error_title);

				String try_again = this.getString(R.string.try_again);
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(loginResult.getError())
						.setTitle(loginTitle)
						.setCancelable(false)
						.setPositiveButton(try_again,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.dismiss();
									}
								});

				AlertDialog alert = builder.create();
				alert.show();
			} else {
				SessionManager.getInstance(this).setIsLoggedIn(true);
				SessionManager.getInstance(this).setUserID(
						String.valueOf(loginResult.getUserID()));
				SessionManager.getInstance(this).setAuthToken(
						loginResult.getToken());

				String url = SessionManager.getInstance(this).userDetailsURL(
						String.valueOf(loginResult.getUserID()));
				Map<String, String> headers = SessionManager.getInstance(this)
						.defaultSessionHeaders();

				new GetDataWebTask<JsonUserDetailsResponse>(this,
						JsonUserDetailsResponse.class, false).execute(url,
						MapUtils.mapToString(headers));
			}
		} else if (result instanceof JsonUserDetailsResponse) {
			JsonUserDetailsResponse detailsResponse = (JsonUserDetailsResponse) result;
			if (!detailsResponse.isValid()) {
				NearlingsApplication.displayNetworkNotAvailableDialog(this);
			} else {

				SessionManager.getInstance(this).setFirstName(
						detailsResponse.getDetails().getFirstname());
				SessionManager.getInstance(this).setLastName(
						detailsResponse.getDetails().getLastname());
				SessionManager.getInstance(this).setMobile(
						detailsResponse.getDetails().getMobile());
				SessionManager.getInstance(this).setEmail(
						detailsResponse.getDetails().getEmail());
				SessionManager.getInstance(this).setGravitar(
						detailsResponse.getDetails().getGravitar());
				SessionManager.getInstance(this).setAlertCount(
						detailsResponse.getDetails().getAlertcount());
				SessionManager.getInstance(this).setMemberships(
						detailsResponse.getDetails().getMemberships());
				goToNextActivity();
			
			}
		}

		/*
		 * 
		 * SessionManager.getInstance(this).setFirstName(result.getFirstname());
		 * SessionManager.getInstance(this).setLastName(result.getLastname());
		 * SessionManager.getInstance(this).setMobile(result.getMobile());
		 * SessionManager.getInstance(this).setEmail(result.getEmail());
		 * SessionManager.getInstance(this).setGravitar(result.getGravitar());
		 * SessionManager
		 * .getInstance(this).setAlertCount(result.getAlertcount());
		 */
		// SessionManager.getInstance(this).setMemberships(result.getMemberships());

	}

	public void goToNextActivity() {
		Intent intent = new Intent(this, HomeActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
	}

	public void doLogin() {
		if (!((NearlingsApplication) this.getApplication())
				.isNetworkAvailable()) {
			((NearlingsApplication) this.getApplication())
					.displayNetworkNotAvailableDialog(this);
			return;
		}
		Map<String, String> headers = SessionManager.getInstance(this)
				.defaultSessionHeaders();
		String username = "";
		String password = "";
		if (NearlingsApplication.DEVELOPER_MODE) {
			username = "ramsin";
			password = "ramsin";
		} else {
			username = usernameET.getText().toString();
			password = passwordET.getText().toString();
		}
		headers.put("username", username);
		headers.put("password", password);
		new PostDataWebTask<JsonLoginResponse>(this, JsonLoginResponse.class)
				.execute(SessionManager.getInstance(this).loginURL(),
						MapUtils.mapToString(headers));
	}
}
