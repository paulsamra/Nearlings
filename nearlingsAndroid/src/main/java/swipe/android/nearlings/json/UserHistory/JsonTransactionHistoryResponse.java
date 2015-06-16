package swipe.android.nearlings.json.UserHistory;


import swipe.android.nearlings.json.NearlingsResponse;

public class JsonTransactionHistoryResponse extends NearlingsResponse{
	PaymentHistoryDetails details;

	public PaymentHistoryDetails getDetails() {
		return details;
	}

	public void setDetails(PaymentHistoryDetails details) {
		this.details = details;
	}

}