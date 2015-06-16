package swipe.android.nearlings.json.alerts;

import java.util.ArrayList;

import swipe.android.nearlings.json.NearlingsResponse;

public class JsonMessagesResponse  extends NearlingsResponse{

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