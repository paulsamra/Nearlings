package swipe.android.nearlings;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

public class SessionManager {
	// Shared Preferences
	static SharedPreferences pref;

	// Editor for Shared preferences
	static Editor editor;

	// Context
	Context _context;

	// Shared pref mode
	static int PRIVATE_MODE = 0;

	// Sharedpref file name
	public static final String PREF_NAME = "NearlingsPreference";

	private static SessionManager instance;

	// keys
	private static final String IS_LOGGED_IN = "IS_LOGGED_IN";
	private static final String USER_NAME = "USER_NAME";
	private static final String TOKEN = "TOKEN";
	private static final String LOCATION = "LOCATION";
	private static final String SEARCH_STRING = "SEARCH_STRING";

	private static final String URL_BASE = "https://nearlings.com/api/2014-10-13";

	public String loginURL() {
		return URL_BASE + "/login";
	}

	public String needsDetailsFollowersURL(String id) {
		// TODO
		return URL_BASE + "/login";
	}

	public String needsDetailsOffersURL(String id) {
		// TODO
		return URL_BASE + "/login";
	}

	public String needDetailsURL(String id) {
		// TODO
		return URL_BASE + "/login";
	}

	public String needsDetailsBidsURL(String id) {
		// TODO
		return URL_BASE + "/login";
	}

	public String needDetailsQueryURL(String location, String searchFilter) {
		// TODO
		return URL_BASE + "/login";
	}
	public String exploreNeedsURL() {
		// TODO
		return URL_BASE + "/explore";
	}
	public String createEventURL() {
		// TODO
		return URL_BASE + "/events";
	}
	

	private SessionManager(Context c) {
		this._context = c;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}

	public static synchronized SessionManager getInstance(Context c) {
		if (instance == null)
			instance = new SessionManager(c);
		return instance;
	}

	public void setIsLoggedIn(boolean showAbout) {
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(IS_LOGGED_IN, showAbout);
		editor.commit();
	}

	public boolean isLoggedIn() {
		return pref.getBoolean(IS_LOGGED_IN, false);
	}

	public void setUserName(String username) {
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(USER_NAME, username);
		editor.commit();
	}

	public void setAuthToken(String token) {
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(TOKEN, token);
		editor.commit();
	}

	public String getAuthToken() {
		return pref.getString(TOKEN, "");
	}

	public Map<String, String> defaultSessionHeaders() {
		Map<String, String> headers = new LinkedHashMap<String, String>();

		headers.put("Accept", "application/json");
		headers.put("Content-Type", "application/json");
		headers.put("Cache-Control", "none");

		if (getAuthToken() != null || getAuthToken() != ""){
			Log.e("Token", getAuthToken());
			headers.put("token", getAuthToken());
		}
		return headers;
	}

	public String getSearchLocation() {
		return pref.getString(LOCATION, "");
	}

	public void setSearchLocation(String location) {
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(LOCATION, location);
		editor.commit();
	}

	public String getSearchString() {
	/*	Location s = ((NearlingsApplication) this._context
				.getApplicationContext()).getCurrentLocation();

		Geocoder gcd = new Geocoder(_context, Locale.getDefault());
		List<Address> addresses;
		String loc = "";
		try {
			addresses = gcd.getFromLocation(s.getLatitude(), s.getLongitude(),
					1);

			if (addresses.size() > 0)
				loc = addresses.get(0).getLocality();
				//System.out.println(addresses.get(0).getLocality());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		return pref.getString(SEARCH_STRING, "All");
	}

	public void setSearchString(String searchString) {
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(SEARCH_STRING, searchString);
		editor.commit();
	}
}