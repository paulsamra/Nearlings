package swipe.android.nearlings.json.jsonUserDetailsResponse;

import swipe.android.nearlings.json.NearlingsResponse;

public class JsonUserDetailsResponse  extends NearlingsResponse{
	public Details getDetails() {
		return details;
	}

	public void setDetails(Details details) {
		this.details = details;
	}

	Details details;
	
}