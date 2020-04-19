package simulator.model;

import java.util.List;

import simulator.misc.Pair;

public class SetWeatherEvent extends Event {
	
	List<Pair<String, Weather>> ws;

	
	public SetWeatherEvent(int time, List<Pair<String,Weather>> ws) {
		super(time);
		
		if(ws == null)
		throw new IllegalArgumentException("La lista no puede ser nula");
		else this.ws = ws;
	}


	@Override
	void execute(RoadMap map) {
		for(int i = 0; i < ws.size() ;i++) {
			Road r = map.getRoad(ws.get(i).getFirst());
			if(r != null)
				r.setWeather(ws.get(i).getSecond());
				else throw new NullPointerException("La carretera no existe");
		}
	}
	
	@Override
	public String toString() {
		return "Change weather";
	}
}