package swipe.android.nearlings.json.groups;

import java.util.ArrayList;

import swipe.android.nearlings.groups.Groups;
import swipe.android.nearlings.json.NearlingsResponse;

import com.edbert.library.network.sync.JsonResponseInterface;


public class JsonGroupsResponse extends NearlingsResponse{
	public ArrayList<Groups> groups;

	public ArrayList<Groups> getGroups() {
		return groups;
	}


	public void setGroups(ArrayList<Groups> groups) {
		this.groups = groups;
	}


}
   