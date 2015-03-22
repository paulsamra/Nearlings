package swipe.android.nearlings.json;

import java.util.ArrayList;

import swipe.android.nearlings.groups.Groups;

import com.edbert.library.network.sync.JsonResponseInterface;


public class NearlingsResponse implements JsonResponseInterface {
	
	@Override
	public boolean isValid() {
		return error == null;
	}

	private String error;

	public String getError() {
		return error;
	}


	public void setError(String error) {
		this.error = error;
	}
}
   