package swipe.android.nearlings.json.jsonUserReviewsResponse;

import java.util.ArrayList;

import com.edbert.library.network.sync.JsonResponseInterface;

public class JsonUserReviewsResponse implements JsonResponseInterface {
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}


	
	public ArrayList<Review> getReviews() {
		return reviews;
	}

	public void setReviews(ArrayList<Review> reviews) {
		this.reviews = reviews;
	}



	String error;
	ArrayList<Review> reviews;

	@Override
	public boolean isValid() {
		return (error == null);
	}

}