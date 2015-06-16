package swipe.android.nearlings.jsonResponses.register;

//TODO: make this shared between JSON and stuff
public class Bids{
	float bid;
	String date;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	Users bidder;
	public float getBid() {
		return bid;
	}
	public Bids(float bid, String date, Users bidder) {
		super();
		this.bid = bid;
		this.date = date;
		this.bidder = bidder;
	}
	public void setBid(float bid) {
		this.bid = bid;
	}
	public Users getBidder() {
		return bidder;
	}
	public void setBidder(Users bidder) {
		this.bidder = bidder;
	}

	
}