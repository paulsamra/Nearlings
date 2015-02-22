package swipe.android.nearlings.jsonResponses.login;

import com.edbert.library.network.sync.JsonResponseInterface;

public class JsonLoginResponse implements JsonResponseInterface{

	@Override
	public boolean isValid() {
		return error == null;
	}
	 String error, token;
	private int userID;
	
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
}