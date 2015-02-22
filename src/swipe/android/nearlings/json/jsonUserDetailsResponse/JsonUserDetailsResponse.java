package swipe.android.nearlings.json.jsonUserDetailsResponse;

import com.edbert.library.network.sync.JsonResponseInterface;

public class JsonUserDetailsResponse implements JsonResponseInterface {
	public Details getDetails() {
		return details;
	}

	public void setDetails(Details details) {
		this.details = details;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	Details details;
	String error;


	@Override
	public boolean isValid() {
		return (error == null);
	}

}