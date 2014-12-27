package swipe.android.nearlings.json.needs.comments;

import java.util.ArrayList;

import com.edbert.library.network.sync.JsonResponseInterface;

public class JsonCommentsResponse  implements JsonResponseInterface{
ArrayList<Comments> comments;
String error;
public ArrayList<Comments> getComments() {
	return comments;
}
public void setComments(ArrayList<Comments> comments) {
	this.comments = comments;
}
public String getError() {
	return error;
}
public void setError(String error) {
	this.error = error;
}
@Override
public boolean isValid() {
	return error == null;
}
}