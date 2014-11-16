package swipe.android.nearlings;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;

import com.edbert.library.network.sync.JsonResponseInterface;
import com.edbert.library.sendRequest.SendRequestInterface;

public abstract class NearlingsRequest implements SendRequestInterface {

	protected Context c;

	public NearlingsRequest(Context c) {
		this.c = c;
	}
	
	@Override
	public void executePostRetrieval(Context c, Object o) {
		// TODO Auto-generated method stub
		
	}
}