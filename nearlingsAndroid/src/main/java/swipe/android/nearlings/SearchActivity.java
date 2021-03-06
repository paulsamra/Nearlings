package swipe.android.nearlings;

import java.util.HashMap;
import java.util.List;

import swipe.android.nearlings.googleplaces.GoogleParser;
import swipe.android.nearlings.googleplaces.GoogleParser.PlacesTask;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.edbert.library.network.AsyncTaskCompleteListener;
import com.example.deletableedittext.DeleteableEditText;

public class SearchActivity extends ActionBarActivity implements
		AsyncTaskCompleteListener<List<HashMap<String, String>>> {
	DeleteableEditText place;
	EditText edt_input_place;
	ImageButton btn_delete_place;
	DeleteableEditText search_item;
	EditText edt_input_search_item;
	ImageButton btn_delete_item;

	ListView listOfPlaces;
	String[] from = new String[] { "description" };
	int[] to = new int[] { android.R.id.text1 };
public static final int RESULT_CANCELED = 99;
	SimpleAdapter adapterWithItems;
	SimpleAdapter adapterWithoutItems;

	PlacesTask placesTask;
	Button cancel, search;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_activity);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(true);
		getSupportActionBar().setTitle("Search");

		place = (DeleteableEditText) findViewById(R.id.search_places);
		search_item = (DeleteableEditText) findViewById(R.id.search_item);
		cancel = (Button) findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}

		});
		search = (Button) findViewById(R.id.search);
		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String s = edt_input_search_item.getText().toString();
				if (s.equals("") || s.equals(SessionManager.DEFAULT_STRING_DISPLAY)) {
					s = SessionManager.DEFAULT_STRING;
				}

				String location = edt_input_place.getText().toString();
				if (location.equals("") || location.equals(SessionManager.DEFAULT_STRING_DISPLAY)) {
					location = SessionManager.DEFAULT_STRING;
				}
				SessionManager
						.getInstance(SearchActivity.this)
						.setSearchLocation(location);
				SessionManager.getInstance(SearchActivity.this)
						.setSearchString(
								s);
				SessionManager.getInstance(SearchActivity.this).commitPendingChanges();
				SessionManager.getInstance(SearchActivity.this).setActiveSearchChanges(true);
				finish();
			}

		});
		// EditText searchTo = (EditText)findViewById(R.id.medittext);
		edt_input_place = (EditText) place.findViewById(R.id.edt_input);
		edt_input_search_item = (EditText) search_item
				.findViewById(R.id.edt_input);
		btn_delete_item = (ImageButton) search_item
				.findViewById(R.id.btn_delete);
		btn_delete_place = (ImageButton) place.findViewById(R.id.btn_delete);
		listOfPlaces = (ListView) findViewById(R.id.search_places_list);

		edt_input_place.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				GoogleParser outer = GoogleParser.getInstance(s.toString(),
						SearchActivity.this);
				placesTask = outer.new PlacesTask(s.toString());
				placesTask.execute(s.toString());
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// btn_delete.setVisibility(edt_input.isFocused() && s.length()
				// > 0 ? View.VISIBLE : View.GONE);
			}

			public void afterTextChanged(Editable s) {
				btn_delete_place.setVisibility(edt_input_place.isFocused()
						&& s.length() > 0 ? View.VISIBLE : View.GONE);
			}
		});

		edt_input_search_item
				.setOnFocusChangeListener(new OnFocusChangeListener() {
					public void onFocusChange(View v, boolean hasFocus) {
						btn_delete_item.setVisibility(edt_input_search_item
								.getText().length() <= 0 || !hasFocus ? 8 : 0);

						if (hasFocus) {

							listOfPlaces.setAdapter(adapterWithoutItems);
						} else {
							if (adapterWithItems != null)
								listOfPlaces.setAdapter(adapterWithItems);
						}
					}
				});

		listOfPlaces.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				HashMap<String, String> description = (HashMap<String, String>) adapterWithItems
						.getItem(position);
				String s = description.get("description");
				place.setText(s);
			}
		});

		
		edt_input_place.setText(SessionManager.getInstance(SearchActivity.this)
				.getSearchLocation());
		String currentItem = SessionManager.getInstance(
				SearchActivity.this).getSearchString();
		if(currentItem.equals(null) || currentItem.equals("")){
			currentItem = SessionManager.DEFAULT_STRING_DISPLAY;
		}
		edt_input_search_item.setText(currentItem);
	}

	@Override
	public void onTaskComplete(List<HashMap<String, String>> resultOfGooglePlace) {
		// TODO Auto-generated method stub

		// Creating a SimpleAdapter for the AutoCompleteTextView
		adapterWithItems = new SimpleAdapter(getBaseContext(),
				resultOfGooglePlace, android.R.layout.simple_list_item_1, from,
				to);

		// Setting the adapter
		listOfPlaces.setAdapter(adapterWithItems);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
				onBackPressed();

		return true;
	}
}