package swipe.android.nearlings;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import swipe.android.nearlings.GoogleParser.PlacesTask;
import swipe.android.nearlings.jsonResponses.events.create.JsonEventSubmitResponse;
import android.app.Activity;
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
import android.widget.Toast;

import com.edbert.library.network.AsyncTaskCompleteListener;
import com.edbert.library.network.PostDataWebTask;
import com.edbert.library.utils.MapUtils;
import com.example.deletableedittext.DeleteableEditText;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

public class CreateEventActivity extends FragmentActivity implements
		AsyncTaskCompleteListener, OnDateSetListener,
		TimePickerDialog.OnTimeSetListener {

	public static final String DATEPICKER_START_TAG = "datepicker_start";
	public static final String DATEPICKER_END_TAG = "datepicker_end";
	public static final String TIMEPICKER_START_TAG = "timepicker_start";
	public static final String TIMEPICKER_END_TAG = "timepicker_end";
	AutoCompleteTextView edt_input_place;
	ImageButton btn_delete_place;
	Button start_date, start_time, end_date, end_time;
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

		setContentView(R.layout.edit_event);

		final Calendar calendar = Calendar.getInstance();

		final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
				this, calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH), false);
		final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
				this, calendar.get(Calendar.HOUR_OF_DAY),
				calendar.get(Calendar.MINUTE), false, false);

		edt_input_place = (AutoCompleteTextView) findViewById(R.id.location);
	
		edt_input_place.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				GoogleParser outer = GoogleParser.getInstance(s.toString(),
						CreateEventActivity.this);
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
		Calendar c = Calendar.getInstance();
		int seconds = c.get(Calendar.SECOND);
		start_date = (Button) findViewById(R.id.start_date);
		start_date.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				datePickerDialog.setVibrate(false);
				datePickerDialog.setYearRange(1985, 2028);
				datePickerDialog.show(getSupportFragmentManager(),
						DATEPICKER_START_TAG);
			}

		});

		start_time = (Button) findViewById(R.id.start_time);
		start_time.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				timePickerDialog.setVibrate(false);
				startTimeLastCalled = true;
				timePickerDialog.show(getSupportFragmentManager(),
						TIMEPICKER_START_TAG);

			}

		});
		end_date = (Button) findViewById(R.id.end_date);
		end_date.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				datePickerDialog.setVibrate(false);
				datePickerDialog.setYearRange(1985, 2028);
				datePickerDialog.show(getSupportFragmentManager(),
						DATEPICKER_END_TAG);
			}

		});
		end_time = (Button) findViewById(R.id.end_time);
		end_time.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				timePickerDialog.setVibrate(false);
				startTimeLastCalled = false;
				timePickerDialog.show(getSupportFragmentManager(),
						TIMEPICKER_END_TAG);

			}

		});
		if (savedInstanceState != null) {
			DatePickerDialog dpd = (DatePickerDialog) getSupportFragmentManager()
					.findFragmentByTag(DATEPICKER_START_TAG);
			if (dpd != null) {
				dpd.setOnDateSetListener(this);
			}
			TimePickerDialog tpd = (TimePickerDialog) getSupportFragmentManager()
					.findFragmentByTag(TIMEPICKER_START_TAG);
			if (tpd != null) {
				tpd.setOnTimeSetListener(this);
			}
			DatePickerDialog dpd2 = (DatePickerDialog) getSupportFragmentManager()
					.findFragmentByTag(DATEPICKER_END_TAG);
			if (dpd2 != null) {
				dpd2.setOnDateSetListener(this);
			}
			TimePickerDialog tpd2 = (TimePickerDialog) getSupportFragmentManager()
					.findFragmentByTag(TIMEPICKER_END_TAG);
			if (tpd2 != null) {
				tpd2.setOnTimeSetListener(this);
			}
		}
		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH); // Note: zero based!
		int day = now.get(Calendar.DAY_OF_MONTH);
		int hour = now.get(Calendar.HOUR_OF_DAY);
		int minute = now.get(Calendar.MINUTE);
		String monthName = new DateFormatSymbols().getMonths()[month];

		start_date.setText(monthName + " " + day + ", " + year);
		end_date.setText(monthName + " " + day + ", " + year);
		if (now.get(Calendar.AM_PM) == Calendar.PM) {
			start_time.setText(hour + ":" + String.format("%02d", minute));
			end_time.setText(hour + ":" + String.format("%02d", minute + 1));
		} else {
			start_time.setText(hour + ":" + String.format("%02d", minute));
			end_time.setText(hour + ":" + String.format("%02d", minute + 1));
		}
	}

	@Override
	public void onTaskComplete(Object result) {
		// TODO Auto-generated method stub
		Log.e("Task complete", "task complete");
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
			// adapterWithItems.notifyDataSetChanged();
			// edt_input_place.invalidate();
		} else if (result instanceof JsonEventSubmitResponse
				&& ((JsonEventSubmitResponse) result).isValid()) {
			this.finish();
		} else {
			// something went wrong!
		}

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
			checkForValidInputs();
		default:
			return super.onOptionsItemSelected(item);
		}
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

	public void submitEvent() {
		Map<String, String> headers = SessionManager.getInstance(this)
				.defaultSessionHeaders();

		headers.put("username", "ramsin");
		headers.put("password", "ramsin");
		new PostDataWebTask<JsonEventSubmitResponse>(this,
				JsonEventSubmitResponse.class).execute(SessionManager
				.getInstance(this).createEventURL(), MapUtils
				.mapToString(headers));
	}

	@Override
	public void onDateSet(DatePickerDialog datePickerDialog, int year,
			int month, int day) {

		String monthName = new DateFormatSymbols().getMonths()[month];
		String total = monthName + " " + day + ", " + year;
		if (datePickerDialog.getTag().equals(DATEPICKER_START_TAG)) {
			start_date.setText(total);
		} else if (datePickerDialog.getTag().equals(DATEPICKER_END_TAG)) {
			end_date.setText(total);
		}
	}

	boolean startTimeLastCalled = true;

	@Override
	public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
		String total = hourOfDay + ":" + String.format("%02d", minute);

		if (startTimeLastCalled) {
			start_time.setText(total);
		} else {
			end_time.setText(total);
		}
	}
}