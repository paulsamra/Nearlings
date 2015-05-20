package swipe.android.nearlings.json.UserHistory;


import java.util.ArrayList;

import swipe.android.nearlings.json.NearlingsResponse;

import com.edbert.library.network.sync.JsonResponseInterface;

public class JsonTransactionHistoryResponse extends NearlingsResponse{
	PaymentHistoryDetails details;

	public PaymentHistoryDetails getDetails() {
		return details;
	}

	public void setDetails(PaymentHistoryDetails details) {
		this.details = details;
	}

}