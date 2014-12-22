package swipe.android.nearlings.jsonResponses.explore;

import com.edbert.library.network.sync.JsonResponseInterface;

public class JsonExploreResponse implements JsonResponseInterface{

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