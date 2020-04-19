package simulator.control;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Event;
import simulator.model.TrafficSimObserver;
import simulator.model.TrafficSimulator;

public class Controller {
	TrafficSimulator simulador;
	Factory<Event> factoria_eventos;
	
	
	public Controller(TrafficSimulator sim, Factory<Event> eventsFactory){
		if(sim == null) throw new IllegalArgumentException("El simulador no puede ser null");
		else this.simulador = sim;
		
		if(eventsFactory == null) throw new IllegalArgumentException("El simulador no puede ser null");
		else this.factoria_eventos = eventsFactory;
	}
	
	
	public void loadEvents(InputStream in) {
		JSONObject jo = new JSONObject(new JSONTokener(in));
		if(!jo.has("events")) throw new IllegalArgumentException("No existe events en el JSON");
		JSONArray js = jo.getJSONArray("events");
		
		for (int i = 0; i < js.length(); i++) {
			simulador.addEvent(factoria_eventos.createInstance(js.getJSONObject(i)));
		}
	}
	 
	public void run(int n, OutputStream out) {
		
		PrintStream printer = new PrintStream(out);
		JSONObject js = new JSONObject();
		JSONArray arr = new JSONArray();
		for (int i = 0; i < n ; i++) {
			simulador.advance();
			arr.put(simulador.report());
		}
		js.put("states", arr);
		
		printer.println(js.toString(1));
	}
	
	
	public void reset() {
		simulador.reset();
	}


	public void addObserver(TrafficSimObserver o){
		simulador.addObserver(o);
	}
	void removeObserver(TrafficSimObserver o){
		simulador.removeObserver(o);

	}
	void addEvent(Event e){
		simulador.addEvent(e);
	}
}
