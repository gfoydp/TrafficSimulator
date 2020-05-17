package simulator.control;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.annotation.processing.FilerException;

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
	private boolean loaded;
	
	
	public Controller(TrafficSimulator sim, Factory<Event> eventsFactory){
		if(sim == null) throw new IllegalArgumentException("El simulador no puede ser null");
		else this.simulador = sim;
		
		if(eventsFactory == null) throw new IllegalArgumentException("El simulador no puede ser null");
		else this.factoria_eventos = eventsFactory;
		
		loaded = false;
	}
	
	
	public void loadEvents(InputStream in) throws FileNotFoundException {
		JSONObject jo = new JSONObject(new JSONTokener(in));
		if(!jo.has("events")) throw new FileNotFoundException("No events in the JSON");
		JSONArray js = jo.getJSONArray("events");
		
		for (int i = 0; i < js.length(); i++) {
			simulador.addEvent(factoria_eventos.createInstance(js.getJSONObject(i)));
		}
		loaded = true;
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
	
	public void run(int n) throws FileNotFoundException {
		if(loaded) {
		for(int i = 0; i < n; i++) {
			simulador.advance();
		}
		}
		else throw new FileNotFoundException("There aren't events to run the simulator");
	}
	
	
	public void reset() {
		simulador.reset();
	}

	public void addObserver(TrafficSimObserver o){
		simulador.addObserver(o);
	}
	public void removeObserver(TrafficSimObserver o){
		simulador.removeObserver(o);

	}
	public void addEvent(Event e){
		simulador.addEvent(e);
	}
}
