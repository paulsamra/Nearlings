package swipe.android.nearlings.json.jsonUserReviewsResponse;

import java.util.ArrayList;

import swipe.android.nearlings.json.NearlingsResponse;

public class JsonUserReviewsResponse extends NearlingsResponse{

	
	public ArrayList<Review> getReviews() {
		return reviews;
	}

	public void setReviews(ArrayList<Review> reviews) {
		this.reviews = reviews;
	}


	ArrayList<Review> reviews;


}