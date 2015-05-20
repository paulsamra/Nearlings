package swipe.android.nearlings;

import swipe.android.nearlings.events.EventsContainerFragment;
import swipe.android.nearlings.json.NearlingsResponse;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;

import com.edbert.library.network.AsyncTaskCompleteListener;

public class NearlingsActivity extends FragmentActivity implements
		AsyncTaskCompleteListener<NearlingsResponse> {

	@Override
	public void onTaskComplete(NearlingsResponse result) {
		if (result != null
				&& result.getError().equals("Token expired or invalid.")) {
			((NearlingsApplication) NearlingsActivity.this
					.getApplication()).logoutDialog(NearlingsActivity.this);

		}
	}
}