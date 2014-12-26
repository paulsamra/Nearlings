package swipe.android.nearlings;

import java.util.Map;

import swipe.android.nearlings.jsonResponses.login.JsonLoginResponse;

import com.edbert.library.network.AsyncTaskCompleteListener;
import com.edbert.library.network.PostDataWebTask;
import com.edbert.library.utils.MapUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class LoginActivity extends Activity implements
AsyncTaskCompleteListener<JsonLoginResponse>  {

	Button signup, login, forgotPassword;
	boolean isLoginFormVisible = true;
	LinearLayout loginForm, signupForm;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.login_form);
		if(SessionManager.getInstance(this).isLoggedIn()){
			Intent intent = new Intent(this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
		}
		  getActionBar().hide();

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
	public void onTaskComplete(JsonLoginResponse result) {
		if (result != null && result.isValid()) {
			SessionManager.getInstance(this).setIsLoggedIn(true);
			SessionManager.getInstance(this).setUserName(
					String.valueOf(result.getUserID()));
			SessionManager.getInstance(this).setAuthToken(
					result.getToken());

			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		}else{
			//display error
		}
	}

	public void doLogin() {
		Map<String, String> headers = SessionManager.getInstance(this).defaultSessionHeaders();

		headers.put("username", "ramsin");
		headers.put("password", "ramsin");
		new PostDataWebTask<JsonLoginResponse>(this,
				JsonLoginResponse.class).execute(
				SessionManager.getInstance(this).loginURL(),
				MapUtils.mapToString(headers));
	}
}
