package swipe.android.nearlings.jsonResponses.explore;

import java.util.ArrayList;

import com.edbert.library.network.sync.JsonResponseInterface;

public class JsonExploreResponse implements JsonResponseInterface {
	public ArrayList<Tasks> tasks;

	@Override
	public boolean isValid() {
		return error == null;
	}

	private String error;

	public String getError() {
		return error;
	}

	public ArrayList<Tasks> getTasks() {
		return tasks;
	}

	public void setTasks(ArrayList<Tasks> tasks) {
		this.tasks = tasks;
	}

	public void setError(String error) {
		this.error = error;
	}
}