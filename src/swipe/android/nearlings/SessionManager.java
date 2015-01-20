package swipe.android.nearlings;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.edbert.library.database.DatabaseCommandManager;

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
	private static final String SEARCH_RADIUS = "SEARCH_RADIUS";
	private static final String SEARCH_STATUS = "SEARCH_STATUS";
	private static final String SEARCH_REWARD = "SEARCH_REWARD";
	private static final String USER_ID = "USER_ID";

	public static final String SEARCH_DEFAULT_FILTER = "All";

	public static final float SEARCH_DEFAULT_NUMERIC = -1;
	private static final String URL_BASE = "https://nearlings.com/api/2014-10-13";

	public String loginURL() {
		return URL_BASE + "/login";
	}

	public String needsDetailsFollowersURL(String id) {
		return URL_BASE + "/login";
	}

	public String needsDetailsOffersURL(String id) {
		return URL_BASE + "/login";
	}

	public String needDetailsURL(String id) {
		return URL_BASE + "/login";
	}

	public String exploreGroupsURL() {
		return URL_BASE + "/groups";
	}

	public String needsDetailsBidsURL(String id) {
		return URL_BASE + "/login";
	}

	public String needDetailsQueryURL(String location, String searchFilter) {
		return URL_BASE + "/login";
	}

	public String exploreNeedsURL() {
		return URL_BASE + "/explore";
	}

	public String exploreEventsURL() {
		return URL_BASE + "/events";
	}

	public String createEventURL() {
		return URL_BASE + "/event";
	}

	public String commentsURL(String id) {
		return URL_BASE + "/need/" + id + "/comments";
	}

	public String alertsURL() {
		return URL_BASE + "/user/" + this.getUserID() + "/alerts";
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
		editor.putString(USER_NAME, username);
		editor.commit();
	}

	public void setAuthToken(String token) {

		editor.putString(TOKEN, token);
		editor.commit();
	}

	public String getAuthToken() {
		return pref.getString(TOKEN, "");
	}

	public String getUserName() {
		return pref.getString(USER_NAME, "");
	}

	public Map<String, String> defaultSessionHeaders() {
		Map<String, String> headers = new LinkedHashMap<String, String>();

		headers.put("Accept", "application/json");
		headers.put("Content-Type", "application/json");
		headers.put("Cache-Control", "none");

		if (getAuthToken() != null && getAuthToken() != "") {
			Log.e("Token", getAuthToken());
			headers.put("token", getAuthToken());
		}
		return headers;
	}

	public String getSearchLocation() {
		return pref.getString(LOCATION, "");
	}

	public void setSearchLocation(String location) {
		editor.putString(LOCATION, location);
	}

	public String getSearchString() {
		return pref.getString(SEARCH_STRING, "All");
	}

	public void setSearchString(String searchString) {
		editor.putString(SEARCH_STRING, searchString);

	}

	public float getSearchRadius() {
		return pref.getFloat(SEARCH_RADIUS, -1);
	}

	public void setSearchRadius(float searchRadius) {
		editor.putFloat(SEARCH_RADIUS, searchRadius);

	}

	public String getSearchStatus() {
		return pref.getString(SEARCH_STATUS, "All");
	}

	public void setSearchStatus(String searchStatus) {
		editor.putString(SEARCH_STATUS, searchStatus);

	}

	public float getSearchRewardMinimum() {
		return pref.getFloat(SEARCH_REWARD, -1);
	}

	public void setSearchRewardMinimum(float searchStatus) {
		editor.putFloat(SEARCH_REWARD, searchStatus);

	}

	public void setUserID(String id) {
		editor.putString(USER_ID, id);
		editor.commit();

	}

	public void commitPendingChanges() {
		editor.commit();
	}

	public String getUserID() {
		return pref.getString(USER_ID, "");
	}

	public void resetTables() {
		DatabaseCommandManager.deleteAllTables(NearlingsContentProvider
				.getDBHelperInstance(_context).getWritableDatabase());

		DatabaseCommandManager.createAllTables(NearlingsContentProvider
				.getDBHelperInstance(_context).getWritableDatabase());
	}

	public void clearUserPref() {
		// destroy all shared preferences
		SharedPreferences settings = _context.getSharedPreferences(
				SessionManager.PREF_NAME, Context.MODE_PRIVATE);
		settings.edit().clear().commit();
	}
}