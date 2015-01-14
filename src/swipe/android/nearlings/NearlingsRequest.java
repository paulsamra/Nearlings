package swipe.android.nearlings;

import android.content.Context;

import com.edbert.library.network.sync.JsonResponseInterface;
import com.edbert.library.sendRequest.SendRequestInterface;

public abstract class NearlingsRequest<T extends JsonResponseInterface> implements SendRequestInterface<T> {

	protected Context c;

	public NearlingsRequest(Context c) {
		this.c = c;
	}
	
	@Override
	public void executePostRetrieval(Context c, T o) {
		// TODO Auto-generated method stub
		
	}
}