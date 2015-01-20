package swipe.android.nearlings;

import java.text.DateFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import swipe.android.nearlings.discover.options.SearchOptionsFilter;
import swipe.android.nearlings.googleplaces.GoogleParser;
import swipe.android.nearlings.googleplaces.GoogleParser.PlacesTask;
import swipe.android.nearlings.jsonResponses.events.create.JsonEventSubmitResponse;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import android.widget.Switch;
import android.widget.Toast;

import com.edbert.library.dialog.DialogManager;
import com.edbert.library.network.AsyncTaskCompleteListener;
import com.edbert.library.network.PostDataWebTask;
import com.edbert.library.network.SocketOperator;
import com.edbert.library.utils.MapUtils;
import com.example.deletableedittext.DeleteableEditText;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;

import org.json.JSONException;
import org.json.JSONObject;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

public class CreateNeedActivity extends FragmentActivity implements
		AsyncTaskCompleteListener {

	NeedFormViewAdapter needFormViewAdapter;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(true);
		getActionBar().setDisplayShowTitleEnabled(true);
		getActionBar().setTitle("Create Need");

		setContentView(R.layout.create_need);
		needFormViewAdapter = new NeedFormViewAdapter(this, getWindow()
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
			if (needFormViewAdapter.areAllViewsValid()) {
				submitEvent();
			} else {
				DialogManager.showOkDialog(this, "OK", "Network Error",
						getString(R.string.network_error));
			}
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
			JSONObject jsonObject = this.needFormViewAdapter.getJSONObject();

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
		try {
			if (result != null) {

				String s = (String) result;

				JSONObject result1 = new JSONObject(s);

				if (result1.get("error") != null) {
					DialogManager.showOkDialog(this, "OK", "Error",
							(String) (result1.get("error")));
					return;
				} else {
					// we're good
				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		DialogManager.showOkDialog(this, "OK", "Network Error",
				getString(R.string.network_error));

	}
}