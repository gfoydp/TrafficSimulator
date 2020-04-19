package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class RoadMap {
	
	private List<Junction> listaCruces;
	private List<Road> listaCarreteras;
	private List<Vehicle> listaVehiculos;
	private Map<String,Junction> mapaCruces;
	private Map<String,Road> mapaCarreteras;
	private Map<String,Vehicle> mapaVehiculos;
	
	 RoadMap() {
		
		listaCruces = new ArrayList<>();
		listaCarreteras = new ArrayList<>();
		listaVehiculos = new ArrayList<>();
		mapaCruces = new HashMap<>();
		mapaCarreteras = new HashMap<>();
		mapaVehiculos = new HashMap<>();
	}
	
	void addJunction(Junction j) {
	if(!mapaCruces.containsKey(j._id)) {
			listaCruces.add(j);
			mapaCruces.put(j._id,j);
		}
	else throw new IllegalArgumentException("Ya existe un cruce con ese id");
	}
	
	void addRoad(Road r) {
		if(mapaCarreteras.containsKey(r._id)) throw new IllegalArgumentException("Ya existe una carretera con ese id");
		if(!mapaCruces.containsKey(r.getCruceD()._id) || !mapaCruces.containsKey(r.getCruceO()._id)) {
			throw new NullPointerException("No existen cruces que conecten a la carretera.");
		}
		r.getCruceD().addIncomingRoad(r);
		r.getCruceO().addOutGoingRoad(r);
		
		listaCarreteras.add(r);
		mapaCarreteras.put(r._id, r);
		
	}
	
	void addVehicle(Vehicle v) {
		
		for(int i = 0 ; i < v.getItinerary().size();i++) {
			if(!listaCruces.contains(v.getItinerary().get(i))) 
				throw new  NullPointerException("No existe la carretera con cruce "+ v.getItinerary().get(i) + " en la lista de carreteras." );		
		}
		if(mapaVehiculos.containsKey(v._id)) 
			throw new IllegalArgumentException("El vehiculo ya se encuentra en la lista de vehiculos");
		
		else {
			listaVehiculos.add(v);
			mapaVehiculos.put(v._id,v);
		}
	}
	
	public Junction getJunction (String id) {
		return mapaCruces.get(id);
	}
	
	public Road getRoad (String id) {
		return mapaCarreteras.get(id);
	}
	
	public Vehicle getVehicle(String id) {		
		return mapaVehiculos.get(id);
	}
	
	public List<Junction> getJunctions(){
		return Collections.unmodifiableList(listaCruces);
	}
	public List<Road> getRoads(){
		return Collections.unmodifiableList(listaCarreteras);
	}
	
	public List<Vehicle> getVehicles(){
		return Collections.unmodifiableList(listaVehiculos);
	}
	void reset() {
		listaCruces.clear();;
		listaCarreteras.clear();;
		listaVehiculos.clear();;
		mapaCruces.clear();;
		mapaCarreteras.clear();;
		mapaVehiculos.clear();;
	}
	public JSONObject report() {
		JSONObject js = new JSONObject();
		JSONArray cruces = new JSONArray();
		JSONArray carreteras = new JSONArray();
		JSONArray vehiculos = new JSONArray();

		for(int i = 0; i < listaCruces.size(); i++) {
			cruces.put(listaCruces.get(i).report());
		}
		js.put("junctions", cruces);
		for(int i = 0; i < listaCarreteras.size();i++ ) {
			carreteras.put(listaCarreteras.get(i).report());
		}
		js.put("roads", carreteras);
		for(int i = 0; i < listaVehiculos.size();i++ ) {
			vehiculos.put(listaVehiculos.get(i).report());
		}	
		js.put("vehicles", vehiculos);
		return js;
	}
}
