package swipe.android.nearlings.json.needs.needsdetailsresponse;

import java.util.ArrayList;

import swipe.android.nearlings.json.NearlingsResponse;

import com.edbert.library.network.sync.JsonResponseInterface;

public class JsonNeedsDetailResponse  extends NearlingsResponse{
	public NeedsDetails getDetails() {
		return details;
	}

	public void setDetails(NeedsDetails details) {
		this.details = details;
	}


	NeedsDetails details;

}