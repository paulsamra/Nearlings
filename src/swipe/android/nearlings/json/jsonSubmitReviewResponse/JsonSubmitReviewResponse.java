package swipe.android.nearlings.json.jsonSubmitReviewResponse;

import com.edbert.library.network.sync.JsonResponseInterface;

public class JsonSubmitReviewResponse implements JsonResponseInterface {
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	String error;
	int result;

	@Override
	public boolean isValid() {
		return (error == null);
	}

}