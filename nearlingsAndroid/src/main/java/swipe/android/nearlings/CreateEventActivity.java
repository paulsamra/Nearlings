package swipe.android.nearlings;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.edbert.library.dialog.DialogManager;
import com.edbert.library.network.AsyncTaskCompleteListener;
import com.edbert.library.utils.MapUtils;

public class CreateEventActivity extends ActionBarActivity implements
		AsyncTaskCompleteListener {

	EventFormViewAdapter eventFormViewAdapter;

	public void onCreate(Bundle savedInstanceState) {

		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		super.onCreate(savedInstanceState);

		setContentView(R.layout.edit_event);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(true);
		getSupportActionBar().setTitle("Create Event");
		eventFormViewAdapter = new EventFormViewAdapter(this, getWindow()
				.getDecorView().findViewById(android.R.id.content),
				savedInstanceState);
		// eventFormViewAdapter.addTextWatcherToAll(textWatcher);
		// //-------------------
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.create_event_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.save_event:
			// submitEvent();

			submitEvent();

			break;
		default:
			onBackPressed();

		}
		return true;
	}

	public void submitEvent() {
		// should check first
		Map<String, String> headers = SessionManager.getInstance(this)
				.defaultSessionHeaders();

		try {
			JSONObject jsonObject = this.eventFormViewAdapter.getJSONObject();
			jsonObject.put("group_id", SessionManager.getInstance(this).getMemberships()[0]);

			new CreateItemNearlings(this).execute(
					SessionManager.getInstance(this).createEventURL(),
					MapUtils.mapToString(headers), jsonObject.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// construct the body

	@Override
	public void onTaskComplete(Object result) {
		if (result != null) {
			try {

				String s = (String) result;

				JSONObject result1 = new JSONObject(s);

				if (result1.get("error") == null
						|| result1.get("error").equals("null")
						|| result1.get("error").equals(null)) {

					// we're good
					Intent intent = new Intent(this, HomeActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					DialogManager.createSingleButtonDialogWithIntent(this,
							"Ok", "Event Created",
							"Event was successfully created!", intent, true);

				} else {
					DialogManager.showOkDialog(this, "OK", "Error",
							(result1.get("error").toString()));
					return;

				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			DialogManager.showOkDialog(this, "OK", "Network Error",
					getString(R.string.network_error));

		}
	}

}