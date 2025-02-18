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
		for(TrafficSimObserver o : observadores) 
			o.onEventAdded(mapa, eventos, e, tiempo);
		
	}
	
	public void advance(){
		tiempo++;
		for(TrafficSimObserver o : observadores) 
			o.onAdvanceStart(mapa,eventos,tiempo);
		int i = 0;
		while(i != eventos.size()) {
			if(eventos.get(i)._time == tiempo) {
				eventos.get(i).execute(mapa);
				eventos.remove(i);
			}else i++;
		}
		try {
		for(int j = 0; j < mapa.getJunctions().size(); j++) {
			mapa.getJunctions().get(j).advance(tiempo);
		}
		for (int j = 0; j < mapa.getRoads().size(); j++) {
			mapa.getRoads().get(j).advance(tiempo);	
		}
		}catch(Exception e) {
			for(TrafficSimObserver o : observadores) 
				o.onError(e.getMessage());
		}
		for(TrafficSimObserver o : observadores) 
			o.onAdvanceEnd(mapa, eventos, tiempo);
		
	}
	
	public void reset() {
		mapa.reset();
		eventos.clear();
		tiempo = 0;
		for(TrafficSimObserver o : observadores) 
			o.onReset(mapa, eventos, tiempo);
	}
	
	public JSONObject report() {
		JSONObject js = new JSONObject();
		js.put("time", tiempo);
		js.put("state", mapa.report()); 
		return js;
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
		o.onRegister(mapa, eventos, tiempo);
	}

	@Override
	public void removeObserver(TrafficSimObserver o) {
		observadores.remove(o);
	}
}
