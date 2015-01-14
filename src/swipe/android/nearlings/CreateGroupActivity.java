package swipe.android.nearlings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import swipe.android.nearlings.googleplaces.GoogleParser;
import swipe.android.nearlings.googleplaces.GoogleParser.PlacesTask;
import swipe.android.nearlings.jsonResponses.events.create.JsonEventSubmitResponse;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.edbert.library.network.AsyncTaskCompleteListener;
import com.edbert.library.network.PostDataWebTask;
import com.edbert.library.utils.MapUtils;
import com.example.deletableedittext.DeleteableEditText;

public class CreateGroupActivity extends FragmentActivity implements
		AsyncTaskCompleteListener {

	public static final String DATEPICKER_START_TAG = "datepicker_start";
	public static final String DATEPICKER_END_TAG = "datepicker_end";
	public static final String TIMEPICKER_START_TAG = "timepicker_start";
	public static final String TIMEPICKER_END_TAG = "timepicker_end";
	AutoCompleteTextView edt_input_place;
	ImageButton btn_delete_place;
	Button start_date, start_time, category;
	DeleteableEditText search_item;
	EditText edt_input_search_item;
	ImageButton btn_delete_item;
	PlacesTask placesTask;
	SimpleAdapter adapterWithItems;
	SimpleAdapter adapterWithoutItems;

	// ListView listOfPlaces;
	String[] from = new String[] { "description" };
	int[] to = new int[] { android.R.id.text1 };

	ArrayAdapter<String> adapter;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(true);
		getActionBar().setDisplayShowTitleEnabled(true);
		getActionBar().setTitle("Create Group");
		setContentView(R.layout.create_group);

		edt_input_place = (AutoCompleteTextView) findViewById(R.id.location);

		edt_input_place.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				GoogleParser outer = GoogleParser.getInstance(s.toString(),
						CreateGroupActivity.this);
				placesTask = outer.new PlacesTask(s.toString());
				// Log.i("Checking google place", "Checking google place");
				placesTask.execute(s.toString());
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// btn_delete.setVisibility(edt_input.isFocused() && s.length()
				// > 0 ? View.VISIBLE : View.GONE);
			}

			public void afterTextChanged(Editable s) {
				/*
				 * btn_delete_place.setVisibility(edt_input_place.isFocused() &&
				 * s.length() > 0 ? View.VISIBLE : View.GONE);
				 */
			}
		});

		edt_input_place.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				HashMap<String, String> description = (HashMap<String, String>) adapterWithItems
						.getItem(position);
				String s = description.get("description");
				edt_input_place.setText(s);
			}
		});
		category = (Button) findViewById(R.id.category_button);
		setUpCategory();
	}

	@Override
	public void onTaskComplete(Object result) {
		// TODO Auto-generated method stub
		if (result instanceof List<?>) {
			List<HashMap<String, String>> resultOfGooglePlace = (List<HashMap<String, String>>) result;

			// Creating a SimpleAdapter for the AutoCompleteTextView
			adapterWithItems = new SimpleAdapter(getBaseContext(),
					resultOfGooglePlace, android.R.layout.simple_list_item_1,
					from, to);

			// Setting the adapter
			adapterWithItems.notifyDataSetChanged();
			edt_input_place.setAdapter(adapterWithItems);
			adapterWithItems.notifyDataSetChanged();

		}
	
	}


	private void setUpCategory() {
		category.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				final String[] items = getResources().getStringArray(
						R.array.group_types);
				AlertDialog.Builder builder = new AlertDialog.Builder(
						CreateGroupActivity.this);

				builder.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {

						category.setText(items[item]);
						dialog.cancel();
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
			}

		});
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
			// save the thing
			//checkForValidInputs();
			break;
		default:
			onBackPressed();
			
		}
		return true;
	}

	public void populateInitialValues() {
		// initialize start and end date to be today

		// start time is current. end time is 1 hour from now
		// start_date, start_time, end_date, end_time;
	}

	public boolean checkForValidInputs() {
		return true;
		// start_date, start_time, end_date, end_time;
	}

	public void submitGroup() {
		Map<String, String> headers = SessionManager.getInstance(this)
				.defaultSessionHeaders();

		headers.put("username", "ramsin");
		headers.put("password", "ramsin");
		new PostDataWebTask<JsonEventSubmitResponse>(this,
				JsonEventSubmitResponse.class).execute(SessionManager
				.getInstance(this).createEventURL(), MapUtils
				.mapToString(headers));
	}



	boolean startTimeLastCalled = true;


}