package swipe.android.nearlings;

import java.util.Map;

import org.droidparts.widget.ClearableEditText;
import org.json.JSONObject;

import swipe.android.nearlings.json.NearlingsResponse;
import swipe.android.nearlings.json.JsonPatchUserInfoResponse.JsonPatchUserInfoResponse;
import swipe.android.nearlings.json.jsonUserDetailsResponse.JsonUserDetailsResponse;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.edbert.library.network.AsyncTaskCompleteListener;
import com.edbert.library.network.GetDataWebTask;
import com.edbert.library.network.PutDataWebTask;
import com.edbert.library.utils.MapUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfilePageActivity extends NearlingsActivity implements
		AsyncTaskCompleteListener<NearlingsResponse> {
	ImageView imageView;
	ClearableEditText firstName, lastName, username, email, password;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_layout);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(true);
		getActionBar().setDisplayShowTitleEnabled(true);
		getActionBar().setTitle("Personal Settings");

		imageView = (ImageView) findViewById(R.id.profile_image);
		firstName = (ClearableEditText) findViewById(R.id.first_name_field);
		lastName = (ClearableEditText) findViewById(R.id.last_name_field);
		username = (ClearableEditText) findViewById(R.id.username_field);
		email = (ClearableEditText) findViewById(R.id.email_field);
		password = (ClearableEditText) findViewById(R.id.password_field);
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String url = "https://en.gravatar.com/gravatars/new";
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
			}

		});
		reloadInfo();
	}

	private void reloadInfo() {

		firstName.setText(SessionManager.getInstance(this).getFirstName());
		lastName.setText(SessionManager.getInstance(this).getLastName());
		username.setText(SessionManager.getInstance(this).getUserName());
		email.setText(SessionManager.getInstance(this).getEmail());
		ImageLoader.getInstance().displayImage(
				"http://www.gravatar.com/avatar/"
						+ SessionManager.getInstance(this).getGravitar(),
				imageView, NearlingsApplication.getDefaultOptions());
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		onBackPressed();
		this.finish();
		return true;
	}

	private JSONObject getJsonRepOfUser() throws Exception {
		JSONObject user = new JSONObject();
		if (!firstName.getText().toString()
				.equals(SessionManager.getInstance(this).getFirstName()))
			user.put("firstname", firstName.getText().toString());

		if (!lastName.getText().toString()
				.equals(SessionManager.getInstance(this).getFirstName()))
			user.put("lastname", lastName.getText().toString());

		if (!username.getText().toString()
				.equals(SessionManager.getInstance(this).getFirstName()))
			user.put("username", username.getText().toString());

		if (!email.getText().toString()
				.equals(SessionManager.getInstance(this).getFirstName()))
			user.put("email", email.getText().toString());

		if (!password.getText().toString()
				.equals(SessionManager.getInstance(this).getPassword())) {
			passwordChanged = true;
			user.put("password", password.getText().toString());
		}

		return user;
	}

	boolean passwordChanged = false;

	@Override
	public void onBackPressed() {
		String url = SessionManager.getInstance(this).userDetailsURL(
				String.valueOf(SessionManager.getInstance(this).getUserID()));
		Map<String, String> headers = SessionManager.getInstance(this)
				.defaultSessionHeaders();

		try {
			new PutDataWebTask<JsonPatchUserInfoResponse>(this,
					JsonPatchUserInfoResponse.class, false).execute(url,
					MapUtils.mapToString(headers),getJsonRepOfUser().toString());
		} catch (Exception e) {
			e.printStackTrace();
			displaySomeError();
		}
	}

	@Override
	public void onTaskComplete(NearlingsResponse result) {

		if (result == null) {
			NearlingsApplication.displayNetworkNotAvailableDialog(this);
			return;
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		if (result.isValid()) {
			if (result instanceof JsonUserDetailsResponse) {
				// insert into our thing
				JsonUserDetailsResponse detailsResponse = (JsonUserDetailsResponse) result;

				SessionManager.getInstance(this).setFirstName(
						detailsResponse.getDetails().getFirstname());
				SessionManager.getInstance(this).setLastName(
						detailsResponse.getDetails().getLastname());
				SessionManager.getInstance(this).setMobile(
						detailsResponse.getDetails().getMobile());
				SessionManager.getInstance(this).setEmail(
						detailsResponse.getDetails().getEmail());
				SessionManager.getInstance(this).setUserName(
						detailsResponse.getDetails().getUsername());
				SessionManager.getInstance(this).setGravitar(
						detailsResponse.getDetails().getGravitar());
				SessionManager.getInstance(this).setAlertCount(
						detailsResponse.getDetails().getAlertcount());
				SessionManager.getInstance(this).setMemberships(
						detailsResponse.getDetails().getMemberships());
				//
				reloadInfo();
				return;

			} else {
				// go here if we're patching the data
				JsonPatchUserInfoResponse patch = (JsonPatchUserInfoResponse) result;
				if (passwordChanged) {
					displayPasswordChangedKick();
					return;
				}
				builder.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int item) {

								ProfilePageActivity.super.onBackPressed();
								dialog.cancel();
							}
						});
				builder.setTitle("Profile Changed");
				builder.setMessage("Your profile has been successfully changed!");
				//this.finish();
				return;
			}
		} else {

			builder.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int item) {
							dialog.cancel();
						}
					});
			builder.setTitle("Error");
			builder.setMessage(result.getError());
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		String url = SessionManager.getInstance(this).userDetailsURL(
				String.valueOf(SessionManager.getInstance(this).getUserID()));
		Map<String, String> headers = SessionManager.getInstance(this)
				.defaultSessionHeaders();

		new GetDataWebTask<JsonUserDetailsResponse>(this,
				JsonUserDetailsResponse.class, false).execute(url,
				MapUtils.mapToString(headers));
	}

	private void displayPasswordChangedKick() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						dialog.cancel();
						NearlingsApplication nap = (NearlingsApplication) ProfilePageActivity.this
								.getApplicationContext();
						nap.logout();
					}
				});
		builder.setTitle("Password updated");
		builder.setMessage("Your password was changed. Please login again.");
	}

	private void displaySomeError() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						dialog.cancel();
ProfilePageActivity.this.finish();
					}
				});
		builder.setTitle("Oops!");
		builder.setMessage("An unexpected error occured. Returning you to the settings page without changes.");
	}
}