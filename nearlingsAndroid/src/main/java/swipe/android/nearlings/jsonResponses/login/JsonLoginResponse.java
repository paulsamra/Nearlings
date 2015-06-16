package swipe.android.nearlings.jsonResponses.login;

import swipe.android.nearlings.json.NearlingsResponse;

public class JsonLoginResponse extends NearlingsResponse{

	 String token;
	private int userID;
	
	
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