package swipe.android.nearlings;

import swipe.android.nearlings.json.NearlingsResponse;
import android.content.Context;
import android.os.Bundle;

import com.edbert.library.sendRequest.SendRequestInterface;

public abstract class NearlingsRequest<T extends NearlingsResponse> implements
		SendRequestInterface<T> {

	protected Context c;

	public NearlingsRequest(Context c) {
		this.c = c;
	}

	@Override
	public boolean executePostRetrieval(Bundle extras, Context c, T o) throws Exception{
		if (o != null && o.getError() != null && o.getError().equals("Token expired or invalid.")) {

			throw new ExpiredSessionException();
		}
		return true;
	}
}