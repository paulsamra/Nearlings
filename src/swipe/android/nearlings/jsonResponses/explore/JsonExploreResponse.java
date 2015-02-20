package swipe.android.nearlings.jsonResponses.explore;

import java.util.ArrayList;

import com.edbert.library.network.sync.JsonResponseInterface;

public class JsonExploreResponse implements JsonResponseInterface {
	public ArrayList<Needs> needs;

	@Override
	public boolean isValid() {
		return error == null;
	}

	private String error;

	public String getError() {
		return error;
	}

	public ArrayList<Needs> getTasks() {
		return needs;
	}

	public void setTasks(ArrayList<Needs> needs) {
		this.needs = needs;
	}

	public void setError(String error) {
		this.error = error;
	}
}