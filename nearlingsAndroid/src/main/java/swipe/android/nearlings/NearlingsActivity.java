package swipe.android.nearlings;

import swipe.android.nearlings.json.NearlingsResponse;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.edbert.library.network.AsyncTaskCompleteListener;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;

public class NearlingsActivity extends ActionBarActivity implements
		AsyncTaskCompleteListener<NearlingsResponse> {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		requestWindowFeature(Window.FEATURE_ACTION_BAR);

		super.onCreate(savedInstanceState);

	}
	@Override
	public void onTaskComplete(NearlingsResponse result) {
		if (result != null
				&& result.getError().equals("Token expired or invalid.")) {
			((NearlingsApplication) NearlingsActivity.this
					.getApplication()).logoutDialog(NearlingsActivity.this);

		}
	}
}