package simulator.factories;


import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewVehicleEvent;

public class NewVehicleEventBuilder extends Builder<Event> {

	public NewVehicleEventBuilder(String type) {
		super(type);
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		int tiempo = data.getInt("time");
		String id = data.getString("id");
		int maxspeed = data.getInt("maxspeed");
		int cclass = data.getInt("class");
		JSONArray it = data.getJSONArray("itinerary");
		List<String> itinerary = new ArrayList<>();
		
		for(int i=0;i<it.length();i++) {
			itinerary.add(it.getString(i));
		}
		return new NewVehicleEvent(tiempo,id,maxspeed,cclass,itinerary);
	}

}

