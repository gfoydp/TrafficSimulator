package simulator.factories;

import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewCityRoad;
import simulator.model.Weather;

public class NewCityRoadEventBuilder extends Builder<Event>{

	public NewCityRoadEventBuilder(String type) {
		super(type);
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		int tiempo = data.getInt("time");
		String id = data.getString("id");
		String origen = data.getString("src");
		String destino = data.getString("dest");
		int length = data.getInt("length");
		int co2 = data.getInt("co2limit");
		int maxspeed = data.getInt("maxspeed");
		String w = data.getString("weather");
		Weather weather = Weather.valueOf(w.toUpperCase());
		
		return new NewCityRoad(tiempo,id,origen,destino,length,co2,maxspeed,weather);	
	}

}
