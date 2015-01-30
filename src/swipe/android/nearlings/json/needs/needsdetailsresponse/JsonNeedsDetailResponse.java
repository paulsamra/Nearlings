package swipe.android.nearlings.json.needs.needsdetailsresponse;

import java.util.ArrayList;

import com.edbert.library.network.sync.JsonResponseInterface;

public class JsonNeedsDetailResponse implements JsonResponseInterface {
	public NeedsDetails getDetails() {
		return details;
	}

	public void setDetails(NeedsDetails details) {
		this.details = details;
	}

	String error;
	NeedsDetails details;

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	@Override
	public boolean isValid() {
		return error == null;
	}
}