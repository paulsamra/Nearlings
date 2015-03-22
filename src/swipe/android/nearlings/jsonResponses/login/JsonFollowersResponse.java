package swipe.android.nearlings.jsonResponses.login;

import java.util.ArrayList;

import swipe.android.nearlings.json.NearlingsResponse;
import swipe.android.nearlings.jsonResponses.register.Users;

import com.edbert.library.network.sync.JsonResponseInterface;

public class JsonFollowersResponse extends NearlingsResponse{


	private ArrayList<Users> users;

	public ArrayList<Users> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<Users> users) {
		this.users = users;
	}
}