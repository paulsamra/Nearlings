package swipe.android.nearlings.json.jsonSubmitReviewResponse;

import swipe.android.nearlings.json.NearlingsResponse;

import com.edbert.library.network.sync.JsonResponseInterface;

public class JsonSubmitReviewResponse extends NearlingsResponse {
	

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	
	int result;


}