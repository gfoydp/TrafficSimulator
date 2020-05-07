package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class Road extends SimulatedObject {
	
	protected Junction cruceO;
	protected Junction cruceD;
	protected int longitud;
	protected int maxSpeed;
	protected int speedLimit;
	protected int contLimit;
	protected Weather weather;
	protected int totalCont;
	protected List<Vehicle> vehicles;
	
	
	Road(String id, Junction cruceO, Junction cruceD, int maxSpeed, int contLimit, int length, Weather weather)throws IllegalArgumentException {
		super(id);
		if(maxSpeed < 0) throw new IllegalArgumentException("Velocidad no valida, debe ser mayor que 0");
		else {
			this.maxSpeed = maxSpeed;
			this.speedLimit = maxSpeed;
		}
		
		if(contLimit <= -1)	throw new IllegalArgumentException("Limite de contaminacion no valido, este no debe ser negativo");
		else this.contLimit = contLimit;
		
		if(length < 0) throw new IllegalArgumentException("Longitud no valida, debe ser mayor que 0");
		else this.longitud = length;
		
		if(cruceO == null) throw new IllegalArgumentException("Cruce de origen no valido");
		else this.cruceO = cruceO;
		
		if(cruceD == null) throw new IllegalArgumentException("Cruce de destino no valido");
		else this.cruceD = cruceD;
		
		if(weather == null) throw new IllegalArgumentException("Tiempo no valido, debe ser [CLOUDY, RAINY, STORM, SUNNY, WINDY]");
		else this.weather = weather;
		
		this.vehicles = new ArrayList <> ();
		
	}

	@Override
	void advance(int time) {
		reduceTotalContamination();
		updateSpeedLimit();
		for(int i = 0; i < vehicles.size() ;i++) {
			if(vehicles.get(i).getVehicleStatus() == VehicleStatus.TRAVELING)
				vehicles.get(i).setSpeed(calculateVehicleSpeed(vehicles.get(i)));
			vehicles.get(i).advance(time);
		}
		Collections.sort(vehicles);
	}

	@Override
	public JSONObject report() {
		JSONObject js = new JSONObject();
		js.put("id", _id);
		js.put("speedlimit", speedLimit);
		js.put("weather", weather);
		js.put("co2", totalCont);
		JSONArray v = new JSONArray();
		for(int i = 0 ; i < vehicles.size(); i++) {
			v.put(vehicles.get(i));
		}
		js.put("vehicles", v);
		return js;
	}

	 public Junction getCruceO() {
		return cruceO;
	}
	 
	 public Junction getCruceD() {
		return cruceD;
	}
	
	 void enter(Vehicle v) {
		if(v.getLocalitation() == 0 && v.getVelact() == 0)
		vehicles.add(v);
		else throw new IllegalArgumentException("Localizacion o velocidad incorrectas.");
		
	}

	void exit(Vehicle v) {
		vehicles.remove(v);
		}

	void setWeather(Weather w) {
		if(w != null) this.weather = w;
		else throw new IllegalArgumentException("Tiempo no valido, debe ser [CLOUDY, RAINY, STORM, SUNNY, WINDY");
	}


	void addContamination(int c) {
		if(c >= 0) this.totalCont += c;		
		else throw new IllegalArgumentException("Las unidades de CO2 no deben ser negativas");
	}
	
	abstract void reduceTotalContamination();
	
	abstract void updateSpeedLimit();
	
	abstract int calculateVehicleSpeed(Vehicle v);
	


	public int getTotalCO2() {
		return totalCont;
	}

	public int getCO2Limit() {
		return contLimit;
	}

	public int getLength() {
		return longitud;
	}

	public int getMaxSpeed() {
		return maxSpeed;
	}

	public int getSpeedLimit() {
		return speedLimit;
	}

	public Weather getWeather() {
		return weather;
	}
	
		
}		
		