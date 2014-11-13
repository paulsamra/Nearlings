package swipe.android.nearlings;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.edbert.library.sendRequest.SendRequestInterface;

public abstract class NearlingsRequest implements SendRequestInterface {

	protected Context c;

	public NearlingsRequest(Context c) {
		this.c = c;
	}
}