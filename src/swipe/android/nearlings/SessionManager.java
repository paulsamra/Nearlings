package swipe.android.nearlings;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.edbert.library.database.DatabaseCommandManager;

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
	// USER
	private static final String IS_LOGGED_IN = "IS_LOGGED_IN";
	private static final String USER_NAME = "USER_NAME";
	private static final String TOKEN = "TOKEN";
	private static final String LOCATION = "LOCATION";
	private static final String FIRST_NAME = "FIRST_NAME";
	private static final String LAST_NAME = "LAST_NAME";
	private static final String MOBILE = "MOBILE";
	private static final String EMAIL = "EMAIL";
	private static final String GRAVITAR = "GRAVITAR";
	private static final String ALERT_COUNT = "ALERT_COUNT";
	private static final String MEMBERSHIPS = "MEMBERSHIPS";
	private static final String EVENT_START_TIME = "EVENT_START_TIME";
	private static final String PASSWORD = "PASSWORD";

	// same across all searches
	private static final String SEARCH_STRING = "SEARCH_STRING";
	private static final String SEARCH_RADIUS = "SEARCH_RADIUS";
	private static final String SEARCH_VISIBILITY = "SEARCH_VISIBILITY";

	// Explore
	private static final String EXPLORE_SEARCH_STATUS = "EXPLORE_SEARCH_STATUS";
	private static final String EXPLORE_SEARCH_REWARD = "EXPLORE_SEARCH_REWARD";
	private static final String EXPLORE_SEARCH_CATEGORY = "EXPLORE_SEARCH_CATEGORY";
	private static final String EXPLORE_TIME_AGO = "EXPLORE_TIME_AGO";
	private static final String EXPLORE_TIME_ENDED = "EXPLORE_TIME_ENDED";

	// Group
	private static final String GROUP_SEARCH_CATEGORY = "GROUP_SEARCH_CATEGORY";

	// Events
	private static final String EVENTS_SEARCH_CATEGORY = "EVENTS_SEARCH_CATEGORY";
	private static final String EVENTS_TIME_START = "EVENTS_TIME_START";
	// Default values
	public static final float DEFAULT_SEARCH_RADIUS = 40.0f;
	public static final String DEFAULT_STRING = "";
	public static final String DEFAULT_STRING_DISPLAY = "All";
	public static final int DEFAULT_VALUE = -1;
	private static final String USER_ID = "USER_ID";

	public static final float SEARCH_DEFAULT_NUMERIC = -1;
	private static final String URL_BASE = "https://nearlings.com/api/2014-10-13";

	public static final int SEARCH_LIMIT = 30;

	public String loginURL() {
		return URL_BASE + "/login";
	}

	public void setFirstName(String name) {
		editor.putString(FIRST_NAME, name);
		editor.commit();
	}


	public void setLastName(String name) {
		editor.putString(LAST_NAME, name);
		editor.commit();
	}

	public void setMobile(String mobile) {
		editor.putString(MOBILE, mobile);
		editor.commit();
	}public void setPassword(String password) {
		editor.putString(PASSWORD, password);
		editor.commit();
	}

	public void setEmail(String email) {
		editor.putString(EMAIL, email);
		editor.commit();
	}

	public void setGravitar(String gravitar) {
		editor.putString(GRAVITAR, gravitar);
		editor.commit();
	}

	public void setAlertCount(int count) {
		editor.putInt(ALERT_COUNT, count);
		editor.commit();
	}

	public void setMemberships(int[] list) {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < list.length; i++) {
			str.append(list[i]).append(",");
		}
		editor.putString(MEMBERSHIPS, str.toString());
		editor.commit();
	}

	public int[] getMemberships() {
		String savedString = pref.getString(MEMBERSHIPS, "");
		StringTokenizer st = new StringTokenizer(savedString, ",");
		int count = st.countTokens();
		int[] memberships = new int[count];
		for (int i = 0; i < count; i++)
			memberships[i] = Integer.valueOf(st.nextToken());

		return memberships;
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
		return URL_BASE + "/needs";
	}

	public String exploreEventsURL() {
		return URL_BASE + "/events";
	}

	public String createEventURL() {
		return URL_BASE + "/event";
	}
	public String createGroupURL() {
		return URL_BASE + "/group";
	}

	public String commentsURL(String id) {
		return URL_BASE + "/need/" + id + "/comments";
	}

	public String needsDetailsURL(String id) {
		return URL_BASE + "/need/" + id;
	}

	public String needsOffersURL(String id) {
		return URL_BASE + "/need/" + id + "/offers";
	}

	public String makeOfferURL(String id) {
		return URL_BASE + "/need/" + id + "/makeoffer";
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
	public String getPassword() {
		return pref.getString(PASSWORD, "");
	}
	public Map<String, String> defaultSessionHeaders() {
		Map<String, String> headers = new LinkedHashMap<String, String>();

		headers.put("Accept", "application/json");
		headers.put("Content-Type", "application/json");
		headers.put("Cache-Control", "none");
		if (getAuthToken() != null && getAuthToken() != "") {
			
			headers.put("token", getAuthToken());
			
		}
		return headers;
	}

	public String getSearchLocation() {
		return pref.getString(LOCATION, "");
	}

	public void setSearchLocation(String location) {
		editor.putString(LOCATION, location);
		editor.commit();
	}

	public String getSearchString() {
		return pref.getString(SEARCH_STRING, DEFAULT_STRING);
	}

	public void setSearchString(String searchString) {
		editor.putString(SEARCH_STRING, searchString);
		editor.commit();
	}

	public float getSearchRadius() {
		return pref.getFloat(SEARCH_RADIUS, DEFAULT_SEARCH_RADIUS);
	}

	public void setSearchRadius(float searchRadius) {
		editor.putFloat(SEARCH_RADIUS, searchRadius);
		editor.commit();
	}

	public String getExploreCategory() {
		return pref.getString(EVENTS_SEARCH_CATEGORY, DEFAULT_STRING);
	}

	public void setExploreCategory(String category) {
		editor.putString(EVENTS_SEARCH_CATEGORY, category);
	}

	public String getSearchStatus() {
		return pref.getString(EXPLORE_SEARCH_STATUS, DEFAULT_STRING);
	}

	public void setSearchStatus(String searchStatus) {
		editor.putString(EXPLORE_SEARCH_STATUS, searchStatus);

	}

	public String getEventCategory() {
		return pref.getString(EVENTS_SEARCH_CATEGORY, DEFAULT_STRING);
	}

	public void setEventCategory(String event_category) {
		editor.putString(EVENTS_SEARCH_CATEGORY, event_category);
		editor.commit();
	}

	public String getGroupCategory() {
		return pref.getString(GROUP_SEARCH_CATEGORY, DEFAULT_STRING);
	}
	
	public String getFirstName(){
		return pref.getString(FIRST_NAME, "");
	}
	public String getLastName(){
		return pref.getString(LAST_NAME, "");
	}
	public String getGravitar(){
		return pref.getString(GRAVITAR, "");
	}
	public String getEmail(){
		return pref.getString(EMAIL, "");
	}

	public void setGroupCategory(String group_category) {
		editor.putString(GROUP_SEARCH_CATEGORY, group_category);
		editor.commit();
	}

	public float getSearchRewardMinimum() {
		return pref.getFloat(EXPLORE_SEARCH_REWARD, DEFAULT_VALUE);
	}

	public void setSearchRewardMinimum(float searchStatus) {
		editor.putFloat(EXPLORE_SEARCH_REWARD, searchStatus);

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

	private static final String PROFILE_URL = "PROFILE_URL";
	public void setUserProfileImageURL(String url) {
		editor.putString(PROFILE_URL, url);
		editor.commit();

	}
	public String getUserProfileURL() {
		return pref.getString(PROFILE_URL, "");
	}

	public void setSearchVisibility(String visibility) {
		editor.putString(SEARCH_VISIBILITY, visibility);
		editor.commit();
	}

	public String getSearchVisibility() {
		return pref.getString(SEARCH_VISIBILITY, DEFAULT_STRING);
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

	public String cancelOfferURL(String id) {
		return URL_BASE + "/need/" + id + "/canceloffer";
	}

	public String makeCommentURL(String id) {
		return URL_BASE + "/need/" + id + "/comments";

	}

	public String submitReviewURL(String id) {
		return URL_BASE + "/user/" + id + "/reviews";

	}
	
	public String createNeedURL(){
		return URL_BASE + "/need/";

	}
	
	public String changeStateURL(String id){
		return URL_BASE + "/need/" + id ;
	}

	public String userDetailsURL(String id) {

		return URL_BASE + "/user/" + id ;
	}

	public String userReviewsURL(String id) {
		return URL_BASE + "/user/" + id +"/reviews";
	}

	public void setEventStartTime(long epoch) {
		editor.putLong(EVENT_START_TIME, epoch);
		editor.commit();
		
	}
	public long getEventTimeStart(){
		return pref.getLong(EVENT_START_TIME, -1);
	}
public static final String BALANCE = "BALANCE";
	public void setBalance(float balance) {
		editor.putFloat(BALANCE, balance);
		editor.commit();
	}
	public float getBalance(){
		return pref.getFloat(BALANCE, 0.0f);
	}

	public String userHistoryURL(String id) {
		// TODO Auto-generated method stub
		return URL_BASE + "/user/" + id + "/payments";
	}

	public String withdrawMoneyURL(String id) {
		// TODO Auto-generated method stub
		return URL_BASE + "/user/"+id +"/withdrawal";
	}
}
