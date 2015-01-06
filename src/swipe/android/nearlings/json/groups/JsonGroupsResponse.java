package swipe.android.nearlings.json.groups;

import java.util.ArrayList;

import swipe.android.nearlings.groups.Groups;

import com.edbert.library.network.sync.JsonResponseInterface;


public class JsonGroupsResponse implements JsonResponseInterface {
	public ArrayList<Groups> groups;

	public ArrayList<Groups> getGroups() {
		return groups;
	}


	public void setGroups(ArrayList<Groups> groups) {
		this.groups = groups;
	}


	@Override
	public boolean isValid() {
		return error == null;
	}

	private String error;

	public String getError() {
		return error;
	}


	public void setError(String error) {
		this.error = error;
	}
}
   