package swipe.android.nearlings;

import android.content.Intent;

public interface ActivityCallbackFromAdapter{
	public void startActivityForResultBridge(Intent i, int request_code);
}