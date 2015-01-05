package swipe.android.nearlings.json.events;


import java.util.ArrayList;

import com.edbert.library.network.sync.JsonResponseInterface;

public class JsonEventsResponse implements JsonResponseInterface {
	ArrayList<Events> events;
	String error;


	public ArrayList<Events> getEvents() {
		return events;
	}

	public void setEvents(ArrayList<Events> events) {
		this.events = events;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	@Override
	public boolean isValid() {
		return error == null;
	}
}