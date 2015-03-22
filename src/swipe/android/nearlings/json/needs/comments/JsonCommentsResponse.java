package swipe.android.nearlings.json.needs.comments;

import java.util.ArrayList;

import swipe.android.nearlings.json.NearlingsResponse;

import com.edbert.library.network.sync.JsonResponseInterface;

public class JsonCommentsResponse extends NearlingsResponse{
	ArrayList<Comments> comments;

	public ArrayList<Comments> getComments() {
		return comments;
	}

	public void setComments(ArrayList<Comments> comments) {
		this.comments = comments;
	}

}