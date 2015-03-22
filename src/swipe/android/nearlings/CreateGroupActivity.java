package swipe.android.nearlings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import swipe.android.nearlings.discover.options.SearchOptionsFilter;
import swipe.android.nearlings.googleplaces.GoogleParser;
import swipe.android.nearlings.googleplaces.GoogleParser.PlacesTask;
import swipe.android.nearlings.jsonResponses.events.create.JsonEventSubmitResponse;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;

import com.edbert.library.dialog.DialogManager;
import com.edbert.library.network.AsyncTaskCompleteListener;
import com.edbert.library.network.PostDataWebTask;
import com.edbert.library.utils.MapUtils;
import com.example.deletableedittext.DeleteableEditText;

public class CreateGroupActivity extends FragmentActivity implements
		AsyncTaskCompleteListener {
	GroupFormViewAdapter groupFormViewAdapter;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(true);
		getActionBar().setDisplayShowTitleEnabled(true);
		getActionBar().setTitle("Create Group");

		setContentView(R.layout.create_group);
		groupFormViewAdapter = new GroupFormViewAdapter(this, getWindow()
				.getDecorView().findViewById(android.R.id.content),
				savedInstanceState);

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
			JSONObject jsonObject = this.groupFormViewAdapter.getJSONObject();
			new CreateItemNearlings(this).execute(
					SessionManager.getInstance(this).createGroupURL(),
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
							"Ok", "Group Created",
							"Group was successfully created!", intent, true);

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
