package swipe.android.nearlings.json.cancelOffer;

import java.util.ArrayList;

import swipe.android.nearlings.json.NearlingsResponse;

import com.edbert.library.network.sync.JsonResponseInterface;

public class CancelOfferResponse extends NearlingsResponse{
	String result;


	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}