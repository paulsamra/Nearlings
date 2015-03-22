package swipe.android.nearlings.json.events;


import java.util.ArrayList;

import swipe.android.nearlings.json.NearlingsResponse;

import com.edbert.library.network.sync.JsonResponseInterface;

public class JsonEventsResponse extends NearlingsResponse {
	ArrayList<Events> events;

	public ArrayList<Events> getEvents() {
		return events;
	}

	public void setEvents(ArrayList<Events> events) {
		this.events = events;
	}

}