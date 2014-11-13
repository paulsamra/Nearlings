package swipe.android.nearlings.jsonResponses.login;

import java.util.ArrayList;

import swipe.android.nearlings.jsonResponses.register.Users;

import com.edbert.library.network.sync.JsonResponseInterface;

public class JsonFollowersResponse implements JsonResponseInterface {
	String error;

	private ArrayList<Users> users;

	@Override
	public boolean isValid() {
		return error == null;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public ArrayList<Users> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<Users> users) {
		this.users = users;
	}
}