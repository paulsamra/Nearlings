package swipe.android.nearlings.json.needs.needsdetailsoffersresponse;

import java.util.ArrayList;

import com.edbert.library.network.sync.JsonResponseInterface;

public class JsonNeedsOffersResponse implements JsonResponseInterface {

	public ArrayList<NeedsOffers> getOffers() {
		return offers;
	}

	public void setOffers(ArrayList<NeedsOffers> offers) {
		this.offers = offers;
	}

	String error;
	ArrayList<NeedsOffers> offers;

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