package swipe.android.nearlings;

import java.util.HashMap;
import java.util.Map;

import com.edbert.library.sendRequest.SendRequestInterface;

public abstract class NearlingsRequest implements SendRequestInterface {


	protected Map<String, String> defaultParams() {
		Map<String, String> defaultMap = new HashMap<String, String>();

		/*
		 * String token = "\\UsernameToken Token=\"" +
		 * SessionManager.getInstance(this.getContext()).getToken() + "\"";
		 * defaultMap.put("Token", token);
		 */
		return defaultMap;
	}
}