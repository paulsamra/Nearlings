package swipe.android.nearlings.json.alerts;

import java.util.ArrayList;

import com.edbert.library.network.sync.JsonResponseInterface;

public class JsonMessagesResponse implements JsonResponseInterface {
	ArrayList<Alerts> alerts;

	@Override
	public boolean isValid() {
		return true;
	}

	public ArrayList<Alerts> getAlerts() {
		return alerts;
	}

	public void setAlerts(ArrayList<Alerts> alerts) {
		this.alerts = alerts;
	}

}