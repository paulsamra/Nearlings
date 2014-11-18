package swipe.android.nearlings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

import com.example.deletableedittext.DeleteableEditText;

public class SearchActivity extends Activity {
	DeleteableEditText place;
	EditText edt_input_place;
	ImageButton btn_delete_place;
	DeleteableEditText search_item;
	EditText edt_input_search_item;
	ImageButton btn_delete_item;

	PlacesTask placesTask;
	ParserTask parserTask;
	ListView listOfPlaces;
	String[] from = new String[] { "description" };
	int[] to = new int[] { android.R.id.text1 };

	SimpleAdapter adapterWithItems;
	SimpleAdapter adapterWithoutItems;

	Button cancel, search;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_activity);

		place = (DeleteableEditText) findViewById(R.id.search_places);
		search_item = (DeleteableEditText) findViewById(R.id.search_item);
		
		cancel = (Button) findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
			
		});
		search = (Button) findViewById(R.id.search);
		search.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				String s = edt_input_place.getText().toString();
				if(s == ""){
					s = "All";
				}
				SessionManager.getInstance(SearchActivity.this).setSearchLocation(edt_input_place.getText().toString());
				SessionManager.getInstance(SearchActivity.this).setSearchString(edt_input_search_item.getText().toString());
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
				placesTask = new PlacesTask();
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

		listOfPlaces.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				HashMap<String, String> description = (HashMap<String, String>) adapterWithItems.getItem(position);
				String s = description.get("description");
				place.setText(s);
			}
		});
	}

	/**
	 * Google Places Parser. Private class since we don't do this anywhere else
	 */
	/** A method to download json data from url */
	private String downloadUrl(String strUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(strUrl);

			// Creating an http connection to communicate with url
			urlConnection = (HttpURLConnection) url.openConnection();

			// Connecting to url
			urlConnection.connect();

			// Reading data from url
			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					iStream));

			StringBuffer sb = new StringBuffer();

			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			data = sb.toString();

			br.close();

		} catch (Exception e) {
			Log.d("Exception while downloading url", e.toString());
		} finally {
			iStream.close();
			urlConnection.disconnect();
		}
		return data;
	}

	// Fetches all places from GooglePlaces AutoComplete Web Service
	private class PlacesTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... place) {
			// For storing data from web service
			String data = "";

			// Obtain browser key from https://code.google.com/apis/console
			String key = "key=AIzaSyBKE7at-QYxRoryyERHIhgLHBLvIauMPHY";

			String input = SearchActivity.this.place.getText();

			try {
				input = "input=" + URLEncoder.encode(place[0], "utf-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}

			// place type to be searched
			String types = "types=geocode";

			// Sensor enabled
			String sensor = "sensor=false";

			// Building the parameters to the web service
			String parameters = input + "&" + types + "&" + sensor + "&" + key;

			// Output format
			String output = "json";

			// Building the url to the web service
			String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"
					+ output + "?" + parameters;

			try {
				// Fetching the data from we service
				data = downloadUrl(url);
			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}
			return data;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			// Creating ParserTask
			parserTask = new ParserTask();

			// Starting Parsing the JSON string returned by Web Service
			parserTask.execute(result);
		}
	}

	/** A class to parse the Google Places in JSON format */

	private class ParserTask extends
			AsyncTask<String, Integer, List<HashMap<String, String>>> {

		JSONObject jObject;

		@Override
		protected List<HashMap<String, String>> doInBackground(
				String... jsonData) {

			List<HashMap<String, String>> places = null;

			PlaceJSONParser placeJsonParser = new PlaceJSONParser();

			try {
				jObject = new JSONObject(jsonData[0]);

				// Getting the parsed data as a List construct
				places = placeJsonParser.parse(jObject);

			} catch (Exception e) {
				Log.d("Exception", e.toString());
			}
			return places;
		}

		@Override
		protected void onPostExecute(List<HashMap<String, String>> result) {

			// Creating a SimpleAdapter for the AutoCompleteTextView
			adapterWithItems = new SimpleAdapter(getBaseContext(), result,
					android.R.layout.simple_list_item_1, from, to);

			// Setting the adapter
			listOfPlaces.setAdapter(adapterWithItems);
		}
	}

}