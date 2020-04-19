package simulator.factories;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetWeatherEvent;
import simulator.model.Weather;

public class SetWeatherEventBuilder extends Builder<Event> {

	public SetWeatherEventBuilder(String type) {
		super(type);
	}

	@Override
	protected Event createTheInstance(JSONObject data)  {
		int tiempo = data.getInt("time");
		List<Pair<String,Weather>> ws = new LinkedList<>();
		JSONArray pair = data.getJSONArray("info");
		String road;
		String w;
		Weather weather;
		for(int i = 0 ;i < pair.length();i++) {
			road = pair.getJSONObject(i).getString("road");
			w = pair.getJSONObject(i).getString("weather");
			weather = Weather.valueOf(w.toUpperCase());
			Pair<String,Weather> aux = new Pair<String,Weather>(road,weather);
			ws.add(aux);
		}
		
		return new SetWeatherEvent(tiempo,ws);
	}

	
}
