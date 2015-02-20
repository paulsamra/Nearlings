package swipe.android.nearlings.json.changeStateResponse;

import java.util.ArrayList;

import com.edbert.library.network.sync.JsonResponseInterface;

public class MarkAsAssignedResponse implements JsonResponseInterface {
	String result, error;

	@Override
	public boolean isValid() {
		return (error == null);
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}


}