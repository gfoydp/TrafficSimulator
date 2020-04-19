package simulator.factories;

import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewInterCityRoad;
import simulator.model.Weather;

public class NewInterCityRoadEventBuilder extends Builder<Event>{

	public NewInterCityRoadEventBuilder(String type) {
		super(type);
		}

	@Override
	protected Event createTheInstance(JSONObject data) {
		int tiempo = data.getInt("time");
		String id = data.getString("id");
		String src = data.getString("src");
		String dest = data.getString("dest");
		int lenght = data.getInt("length");
		int co2 = data.getInt("co2limit");
		int maxspeed = data.getInt("maxspeed");
		String s = data.getString("weather");
		Weather weather = Weather.valueOf(s.toUpperCase());
		
		return new NewInterCityRoad(tiempo,id,src,dest,lenght,co2,maxspeed,weather);
	}

}
