package swipe.android.nearlings.json.needs.needsdetailsresponse;

import swipe.android.nearlings.json.NearlingsResponse;

public class JsonNeedsDetailResponse  extends NearlingsResponse{
	public NeedsDetails getDetails() {
		return details;
	}

	public void setDetails(NeedsDetails details) {
		this.details = details;
	}


	NeedsDetails details;

}