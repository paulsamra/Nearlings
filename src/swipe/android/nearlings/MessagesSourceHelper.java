package swipe.android.nearlings;

import android.content.Context;
import android.os.Bundle;

import com.edbert.library.network.sync.JsonResponseInterface;
import com.edbert.library.sendRequest.AbstractSourceHelper;

public class MessagesSourceHelper<JsonMessagesResponse> extends AbstractSourceHelper{

	@Override
	public JsonResponseInterface makeRequest(Bundle b) {
		/*T response = (T) SocketOperator.getInstance(classtype).getResponse(
				getContext(), getBaseURL(extras), defaultMap);*/
	
		return null;
	}

	@Override
	public Class<JsonMessagesResponse> getJSONclass() {
		return null;
	}

	@Override
	public boolean writeToDatabase(Context c, Object response) {
		return super.writeToDatabase(c, response);
	}

}