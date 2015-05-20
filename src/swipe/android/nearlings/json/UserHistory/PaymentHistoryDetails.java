package swipe.android.nearlings.json.UserHistory;

import java.util.ArrayList;

public class PaymentHistoryDetails{
	float balance, available;
	ArrayList<PaymentHistory> pending;
	ArrayList<PaymentHistory> posted;
	public float getBalance() {
		return balance;
	}
	public void setBalance(float balance) {
		this.balance = balance;
	}
	public float getAvailable() {
		return available;
	}
	public void setAvailable(float available) {
		this.available = available;
	}
	public ArrayList<PaymentHistory> getPending() {
		return pending;
	}
	public void setPending(ArrayList<PaymentHistory> pending) {
		this.pending = pending;
	}
	public ArrayList<PaymentHistory> getPosted() {
		return posted;
	}
	public void setPosted(ArrayList<PaymentHistory> posted) {
		this.posted = posted;
	}
}