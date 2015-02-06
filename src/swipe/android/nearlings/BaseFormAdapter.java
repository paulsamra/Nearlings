package swipe.android.nearlings;

import java.text.DateFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import swipe.android.nearlings.googleplaces.GoogleParser;
import swipe.android.nearlings.googleplaces.GoogleParser.PlacesTask;
import swipe.android.nearlings.jsonResponses.events.create.JsonEventSubmitResponse;

import com.edbert.library.network.AsyncTaskCompleteListener;
import com.example.deletableedittext.DeleteableEditText;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.google.gson.JsonObject;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import de.metagear.android.util.WidgetUtils;
import de.metagear.android.view.ValidatingEditText;
import de.metagear.android.view.ValidatingView;
import de.metagear.android.view.validation.ViewValidator;
import de.metagear.android.view.validation.textview.EmailValidator;
import de.metagear.android.view.validation.textview.MinLengthValidator;
import de.metagear.android.view.validation.textview.NumberValidator;
import de.metagear.android.view.validation.textview.PriceValidator;
import de.metagear.android.view.validation.textview.ZipCodeValidator;

// reviewed
public abstract class BaseFormAdapter extends ValidatingViewAdapter implements
AsyncTaskCompleteListener, OnDateSetListener,
TimePickerDialog.OnTimeSetListener  {
	protected List<ValidatingView> validatingViews;

	private static final int MIN_LENGTH_OF_TEXT_VALUES = 0;
	


	boolean startTimeLastCalled = true;
	protected FragmentActivity ctx;
	

	public static final String DATEPICKER_START_TAG = "datepicker_start";
	public static final String DATEPICKER_END_TAG = "datepicker_end";
	public static final String TIMEPICKER_START_TAG = "timepicker_start";
	public static final String TIMEPICKER_END_TAG = "timepicker_end";

	// ListView listOfPlaces;
	String[] from = new String[] { "description" };
	int[] to = new int[] { android.R.id.text1 };
Calendar calendar;
TimePickerDialog timePickerDialog;
PlacesTask placesTask;

AutoCompleteTextView edt_input_place;
Button start_date, start_time;
SimpleAdapter adapterWithItems;
SimpleAdapter adapterWithoutItems;

final DatePickerDialog datePickerDialog;
	public BaseFormAdapter(FragmentActivity ctx, View rootView, Bundle savedInstanceState) {
		super(rootView);
		edt_input_place = (AutoCompleteTextView) rootView.findViewById(R.id.location);
		
		start_date = (Button) rootView.findViewById(R.id.start_date);
		start_time = (Button) rootView.findViewById(R.id.start_time);
		this.ctx = ctx;
		 calendar = Calendar.getInstance();

			 datePickerDialog = DatePickerDialog.newInstance(
					this, calendar.get(Calendar.YEAR),
					calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH), false);
			timePickerDialog = TimePickerDialog.newInstance(
					this, calendar.get(Calendar.HOUR_OF_DAY),
					calendar.get(Calendar.MINUTE), false, false);
			if (savedInstanceState != null) {
				DatePickerDialog dpd = (DatePickerDialog) ctx.getSupportFragmentManager()
						.findFragmentByTag(DATEPICKER_START_TAG);
				if (dpd != null) {
					dpd.setOnDateSetListener(this);
				}
				TimePickerDialog tpd = (TimePickerDialog) ctx.getSupportFragmentManager()
						.findFragmentByTag(TIMEPICKER_START_TAG);
				if (tpd != null) {
					tpd.setOnTimeSetListener(this);
				}
				DatePickerDialog dpd2 = (DatePickerDialog) ctx.getSupportFragmentManager()
						.findFragmentByTag(DATEPICKER_END_TAG);
				if (dpd2 != null) {
					dpd2.setOnDateSetListener(this);
				}
				TimePickerDialog tpd2 = (TimePickerDialog) ctx.getSupportFragmentManager()
						.findFragmentByTag(TIMEPICKER_END_TAG);
				if (tpd2 != null) {
					tpd2.setOnTimeSetListener(this);
				}
			}
			this.setUpPlaces(edt_input_place);
			this.setUpTime(start_time);
			this.setUpDate(start_date);
			setUpTimeInitialize(start_date, start_time);
		
	}

	private void setUpTimeInitialize( TextView start_date, TextView start_time){
	
		if(start_date == null || start_time == null){
			return;
		}
		
		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH); // Note: zero based!
		int day = now.get(Calendar.DAY_OF_MONTH);
		int hour = now.get(Calendar.HOUR_OF_DAY);
		int minute = now.get(Calendar.MINUTE);
		String monthName = new DateFormatSymbols().getMonths()[month];

		start_date.setText(monthName + " " + day + ", " + year);
		if (now.get(Calendar.AM_PM) == Calendar.PM) {
			start_time.setText(hour + ":" + String.format("%02d", minute));
		} else {
			start_time.setText(hour + ":" + String.format("%02d", minute));
		}
		
	}
	
	protected void setUpDate(Button start_date){
		if(start_date == null){
			return;
		}
		start_date.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				datePickerDialog.setVibrate(false);
				datePickerDialog.setYearRange(1985, 2028);
				datePickerDialog.show(ctx.getSupportFragmentManager(),
						DATEPICKER_START_TAG);
			}

		});

	}
	protected void setUpTime(Button start_time){
		if(start_time == null){
			return;
		}
		start_time.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				timePickerDialog.setVibrate(false);
				startTimeLastCalled = true;
				timePickerDialog.show(ctx.getSupportFragmentManager(),
						TIMEPICKER_START_TAG);

			}

		});
	}
protected void setUpPlaces(final AutoCompleteTextView edt_input_place){
	if(edt_input_place == null){
		return;
	}
	edt_input_place.addTextChangedListener(new TextWatcher() {
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			GoogleParser outer = GoogleParser.getInstance(s.toString(),
					BaseFormAdapter.this);
			placesTask = outer.new PlacesTask(s.toString());
			placesTask.execute(s.toString());
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void afterTextChanged(Editable s) {
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

}
	protected void setUpCategory(final Button category, int array_of_string) {
		
		final String[] items = ctx.getResources().getStringArray(
				array_of_string);
		category.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				
				AlertDialog.Builder builder = new AlertDialog.Builder(
						ctx);

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
		category.setText(items[0]);
	}

	protected void setUpAgeRequirements(final Button age_inequality, final EditText age_value) {
		
		final String[] items = ctx.getResources().getStringArray(
				R.array.event_requirements_age_inequalities);
		age_inequality.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			
				AlertDialog.Builder builder = new AlertDialog.Builder(
						ctx);

				builder.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {

						age_inequality.setText(items[item]);
						dialog.cancel();
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
			}

		});
		age_inequality.setText(items[0]);
		age_value.setText("0");
	}
	

	@Override
	public void onTaskComplete(Object result) {
		if (result instanceof List<?>) {
			List<HashMap<String, String>> resultOfGooglePlace = (List<HashMap<String, String>>) result;

			// Creating a SimpleAdapter for the AutoCompleteTextView
			adapterWithItems = new SimpleAdapter(ctx,
					resultOfGooglePlace, android.R.layout.simple_list_item_1,
					from, to);

			// Setting the adapter
			adapterWithItems.notifyDataSetChanged();
			edt_input_place.setAdapter(adapterWithItems);
			adapterWithItems.notifyDataSetChanged();
			// adapterWithItems.notifyDataSetChanged();
			// edt_input_place.invalidate();
		} 
	
	}

	@Override
	public void onDateSet(DatePickerDialog datePickerDialog, int year,
			int month, int day) {

		String monthName = new DateFormatSymbols().getMonths()[month];
		String total = monthName + " " + day + ", " + year;
		if (datePickerDialog.getTag().equals(DATEPICKER_START_TAG)) {
			start_date.setText(total);
		} else if (datePickerDialog.getTag().equals(DATEPICKER_END_TAG)) {
			// end_date.setText(total);
		}
	}

	@Override
	public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
		String total = hourOfDay + ":" + String.format("%02d", minute);

		if (startTimeLastCalled) {
			start_time.setText(total);
		} else {
			// end_time.setText(total);
		}
	}


protected OnClickListener dateOnClickListener = new OnClickListener(){

	@Override
	public void onClick(View v) {
		datePickerDialog.setVibrate(false);
		datePickerDialog.setYearRange(1985, 2028);
		datePickerDialog.show(ctx.getSupportFragmentManager(),
				DATEPICKER_START_TAG);
	}
};

protected OnClickListener timeOnClickListener = new OnClickListener(){

	@Override
	public void onClick(View v) {
		timePickerDialog.setVibrate(false);
		startTimeLastCalled = true;
		timePickerDialog.show(ctx.getSupportFragmentManager(),
				TIMEPICKER_START_TAG);
	}
};

protected void setUpPriceListener(final EditText price) {
	
	price.addTextChangedListener(new TextWatcher() {
		private String current = "";

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if (!s.toString().equals(current)) {
				price.removeTextChangedListener(this);

				String cleanString = s.toString().replaceAll("[$,.]", "");
				cleanString = cleanString.replaceAll("[^0-9.]", "");
				if(cleanString.equals("")){
					cleanString = "0.00";
				}
				double parsed = Double.parseDouble(cleanString);
				
				String formatted = NumberFormat.getCurrencyInstance()
						.format((parsed / 100));

				current = formatted;
				price.setText(formatted);
				price.setSelection(formatted.length());

				price.addTextChangedListener(this);
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub

		}
	});
	price.setText("$0.00");
}

}