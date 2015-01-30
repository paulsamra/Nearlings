package swipe.android.nearlings.jsonResponses.explore;

import java.util.ArrayList;

import com.edbert.library.network.sync.JsonResponseInterface;

public class JsonExploreResponse implements JsonResponseInterface {
	public ArrayList<Needs> tasks;

	@Override
	public boolean isValid() {
		return error == null;
	}

	private String error;

	public String getError() {
		return error;
	}

	public ArrayList<Needs> getTasks() {
		return tasks;
	}

	public void setTasks(ArrayList<Needs> tasks) {
		this.tasks = tasks;
	}

	public void setError(String error) {
		this.error = error;
	}
}