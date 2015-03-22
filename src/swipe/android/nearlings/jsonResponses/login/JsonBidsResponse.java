package swipe.android.nearlings.jsonResponses.login;

import java.util.ArrayList;

import swipe.android.nearlings.json.NearlingsResponse;
import swipe.android.nearlings.jsonResponses.register.Bids;

import com.edbert.library.network.sync.JsonResponseInterface;

public class JsonBidsResponse extends NearlingsResponse{

	private ArrayList<Bids> bids;


	public ArrayList<Bids> getBids() {
		return bids;
	}

	public void setBids(ArrayList<Bids> bids) {
		this.bids = bids;
	}

	
}