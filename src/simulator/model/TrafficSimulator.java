package simulator.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import simulator.misc.SortedArrayList;

public class TrafficSimulator implements Observable<TrafficSimObserver>{
	
	private RoadMap mapa;
	private List<Event> eventos;
	private int tiempo;
	private List<TrafficSimObserver> observadores;


	public TrafficSimulator() {
		mapa = new RoadMap();
		eventos = new SortedArrayList<Event>();
		tiempo = 0;
		this.observadores = new ArrayList<>();

	}
	
	public void addEvent(Event e) {
		eventos.add(e);
		onEventAdded(mapa, eventos, e, tiempo);
	}
	
	public void advance(){
		tiempo++;
		onAdvanceStart(mapa,eventos,tiempo);
		int i = 0;
		while(i != eventos.size()) {
			if(eventos.get(i)._time == tiempo) {
				eventos.get(i).execute(mapa);
				eventos.remove(i);
			}else i++;
		}
		for(int j = 0; j < mapa.getJunctions().size(); j++) {
			mapa.getJunctions().get(j).advance(tiempo);
		}
		for (int j = 0; j < mapa.getRoads().size(); j++) {
			mapa.getRoads().get(j).advance(tiempo);	
		}
		onAdvanceEnd(mapa, eventos, tiempo);
	}
	
	public void reset() {
		mapa.reset();
		eventos.clear();
		tiempo = 0;
		onReset(mapa, eventos, tiempo);
	}
	
	public JSONObject report() {
		JSONObject js = new JSONObject();
		js.put("time", tiempo);
		js.put("state", mapa.report()); 
		return js;
	}

	void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		
	}
	void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		
	}
	void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		
	}
	void onReset(RoadMap map, List<Event> events, int time) {
		
	}
	void onRegister(RoadMap map, List<Event> events, int time) {
		
	}
	void onError(String err) {
		for(TrafficSimObserver o: observadores){
			o.onAdvanceStart(mapa, eventos, tiempo);
		}
	}
	
	@Override
	public void addObserver(TrafficSimObserver o) {
		if(!observadores.contains(o))
			observadores.add(o);		
	}

	@Override
	public void removeObserver(TrafficSimObserver o) {
		observadores.remove(o);
	}
}
