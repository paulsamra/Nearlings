package swipe.android.nearlings.jsonResponses.explore;

import java.util.ArrayList;

import swipe.android.nearlings.json.NearlingsResponse;

import com.edbert.library.network.sync.JsonResponseInterface;

public class JsonExploreResponse extends NearlingsResponse{
	public ArrayList<Needs> needs;



	public ArrayList<Needs> getTasks() {
		return needs;
	}

	public void setTasks(ArrayList<Needs> needs) {
		this.needs = needs;
	}

}