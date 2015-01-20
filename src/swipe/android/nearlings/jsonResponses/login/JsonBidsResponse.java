package swipe.android.nearlings.jsonResponses.login;

import java.util.ArrayList;

import swipe.android.nearlings.jsonResponses.register.Bids;

import com.edbert.library.network.sync.JsonResponseInterface;

public class JsonBidsResponse implements JsonResponseInterface {
	String error;

	private ArrayList<Bids> bids;

	@Override
	public boolean isValid() {
		return error == null;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public ArrayList<Bids> getBids() {
		return bids;
	}

	public void setBids(ArrayList<Bids> bids) {
		this.bids = bids;
	}

	
}