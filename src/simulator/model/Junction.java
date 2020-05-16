package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;

public class Junction extends SimulatedObject {
	
	private List<Road> roadEntranceList;
	private Map<Junction, Road> outerRoadMap;
	private List<List<Vehicle>> colasList;
	private Map<Road,List<Vehicle>> colaCarretera;
	private int indiceDeSemaforoEnVerde;
	private int ultimoPasoDecambioDeSemaforo;
	private LightSwitchingStrategy estrategiaCambioSemaforo;
	private DequeuingStrategy estrategiaExtraerElementosCola;
	private int x;
	private int y;
	
	

	Junction(String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor) {
		super(id);
		if(lsStrategy == null) throw new IllegalArgumentException("La estrategia no puede ser null");
		else this.estrategiaCambioSemaforo = lsStrategy;
		
		if(dqStrategy == null) throw new IllegalArgumentException("La estrategia no puede ser null");
		else this.estrategiaExtraerElementosCola = dqStrategy;

		if(xCoor < 0) throw new IllegalArgumentException("La coordenada x debe ser mayor que 0");
		else this.x = xCoor;
		
		if(yCoor < 0) throw new IllegalArgumentException("La coordenada y debe ser mayor que 0");
		else this.y = yCoor;
		
		
		this.roadEntranceList = new ArrayList<>();		
		this.outerRoadMap  = new HashMap<>();
		this.colasList = new ArrayList<>();
		this.colaCarretera = new HashMap<>();
		this.indiceDeSemaforoEnVerde = -1;
		}

	@Override
	void advance(int time) {
		
		if(indiceDeSemaforoEnVerde != -1 && !colasList.isEmpty()) {
			List<Vehicle> v = estrategiaExtraerElementosCola.dequeue(colasList.get(indiceDeSemaforoEnVerde));
			for(int i = 0; i < v.size(); i++) {
				v.get(i).moveToNextRoad();
				colasList.get(indiceDeSemaforoEnVerde).remove(i);
			}
		}
		
		int i = estrategiaCambioSemaforo.chooseNextGreen(roadEntranceList, colasList, indiceDeSemaforoEnVerde, ultimoPasoDecambioDeSemaforo, time);
		
		if(i != indiceDeSemaforoEnVerde) {
			indiceDeSemaforoEnVerde = i;
			ultimoPasoDecambioDeSemaforo = time;
		}
	}

	@Override
	public JSONObject report() {
		JSONObject js = new JSONObject();
		js.put("id",_id); 
		if(indiceDeSemaforoEnVerde == -1)
		js.put("green","none");
		else js.put("green",roadEntranceList.get(indiceDeSemaforoEnVerde)); 
		JSONArray array = new JSONArray();
	
		
		for (Map.Entry<Road, List<Vehicle>> entry : colaCarretera.entrySet()) {
			
			JSONObject aux = new JSONObject();
			JSONArray arr = new JSONArray();
			aux.put("road", entry.getKey());
			for (int j = 0; j < entry.getValue().size() ;j++) {
				arr.put(entry.getValue().get(j));
			}
			aux.put("vehicles",arr);
			array.put(aux);
		}
		
		js.put("queues", array);
	
		return js;
	}

	
	void addIncomingRoad(Road r) {
		if(this == r.getCruceD()) {
			roadEntranceList.add(r);
			List<Vehicle> list = new LinkedList<>();
			colasList.add(list);
			colaCarretera.put(r, list);
		}else throw new IllegalArgumentException("La carretera no es una carretera entrante a este cruce.");
	}
	
	void addOutGoingRoad(Road r) {
		for(Entry<Junction, Road> entry : outerRoadMap.entrySet()) {
			if(entry.getValue().getCruceO() == this && entry.getKey() == r.getCruceD())
				throw new IllegalArgumentException("La carretera ya existe");
		}		
		if(this == r.getCruceO()) 
			this.outerRoadMap.put(r.getCruceD(), r);
		else throw new NullPointerException("La cruce de origen no corresponde con este cruce.");
	}
	
	
	void enter(Vehicle v) {
		Road road = v.getRoad();
		if(colaCarretera.containsKey(road))
			colaCarretera.get(road).add(v);
	}
	
	Road roadTo(Junction j) {
		Road road = null;
		for(Entry<Junction, Road> entry : outerRoadMap.entrySet()) {
			if(entry.getValue().getCruceO() == this && entry.getKey() == j) {
				road = entry.getValue();
			}
		}
		return road;
	}

	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

	public int getGreenLightIndex() {
		return indiceDeSemaforoEnVerde;
	}

	public List<Road> getInRoads() {
		return Collections.unmodifiableList(new ArrayList<>(roadEntranceList));
	}

	public Map<Road, List<Vehicle>> getColaCarretera() {
		return Collections.unmodifiableMap(new HashMap<>(colaCarretera));
	}
	public String getGreens() {
		if(indiceDeSemaforoEnVerde == -1) return "NONE";
		return roadEntranceList.get(indiceDeSemaforoEnVerde).toString();
	}
	public List<Vehicle> getQueue(Road r) {
		colaCarretera.get(r);
		return Collections.unmodifiableList(new ArrayList<>(colaCarretera.get(r)));
	}

}

