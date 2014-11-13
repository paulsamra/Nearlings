package swipe.android.nearlings;

import java.util.LinkedHashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;

public class SessionManager{
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

	//keys
	private static final String IS_LOGGED_IN = "IS_LOGGED_IN";
	private static final String USER_NAME = "USER_NAME";
	private static final String TOKEN = "TOKEN";
	
	private static final String URL_BASE="https://nearlings.com/api/2014-10-13";
	public String loginURL(){
		return URL_BASE + "/login";
	}
	public String needsDetailsFollowersURL(String id){
		//TODO
		return URL_BASE + "/login";
	}
	public String needsDetailsOffersURL(String id){
		//TODO
		return URL_BASE + "/login";
	}
	public String needDetailsURL(String id){
		//TODO
		return URL_BASE + "/login";
	}
	public String needDetailsQueryURL(String location, String searchFilter){
		//TODO
		return URL_BASE + "/login";
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
    public void setUserName(String username){
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(USER_NAME, username);
        editor.commit();
    }
    public void setAuthToken(String token){
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(TOKEN, token);
        editor.commit();
    }
    public Map<String, String> defaultSessionHeaders(){
    	Map<String, String> headers = new LinkedHashMap<String, String>();

		headers.put("Accept", "application/json");
		headers.put("Content-Type", "application/json");
		headers.put("Cache-Control", "none");
		return headers;
    }


}