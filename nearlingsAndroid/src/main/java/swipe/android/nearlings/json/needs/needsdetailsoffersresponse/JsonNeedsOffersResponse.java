package swipe.android.nearlings.json.needs.needsdetailsoffersresponse;

import java.util.ArrayList;

import swipe.android.nearlings.json.NearlingsResponse;

public class JsonNeedsOffersResponse extends NearlingsResponse{

	public ArrayList<NeedsOffers> getOffers() {
		return offers;
	}

	public void setOffers(ArrayList<NeedsOffers> offers) {
		this.offers = offers;
	}

	ArrayList<NeedsOffers> offers;

}