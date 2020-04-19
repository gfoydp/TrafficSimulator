package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

public class Vehicle extends SimulatedObject implements Comparable<Vehicle> {
	
	private List<Junction> itinerary;
	private int maxSpeed;
	private int velact;
	private VehicleStatus vehicleStatus;
	private Road road;
	private int localitation;
	private int contamination; 
	private int totalCont;
	private int totalDistance;
	private int num_cruces;
	 
	
	Vehicle(String id, int maxSpeed, int contClass, List<Junction> itinerary){
		
		super(id);
		
		if(maxSpeed < 0) 
			throw new IllegalArgumentException("Velocidad maxima no valida para vehiculo, debe ser mayor que 0");
		else this.maxSpeed = maxSpeed;
		
		if(contClass < 0 || contClass > 10)
			throw new IllegalArgumentException("Contaminacion no valida para vehiculo, debe encontrarse en el rango [0,10]");
		else this.contamination = contClass;
		
		if(itinerary.size() < 2) 
			throw new IllegalArgumentException("Lista no valida para vehiculo, su dimension debe ser al menos de 2 cruces"); 
		else this.itinerary = Collections.unmodifiableList(new ArrayList<>(itinerary));
		
		this.vehicleStatus = VehicleStatus.PENDING;
		this.localitation = 0;
	}
	

	@Override 
	void advance(int time) {
		int sLocalVelocidad = 0;
		int c = 0;
		int localizacion = localitation;
		if(this.vehicleStatus == VehicleStatus.TRAVELING) {
			
			sLocalVelocidad = this.velact + localizacion;
			
			if(sLocalVelocidad > road.getLongitud()) {
				
				this.localitation = road.getLongitud();
				
			}else {
				this.localitation = sLocalVelocidad;
			}
			c = (this.localitation - localizacion) * this.contamination; 
			this.totalCont += c;
			road.addContamination(c);
						 
			if(this.localitation >= road.getLongitud()) {
				road.getCruceD().enter(this);
			
				this.vehicleStatus = VehicleStatus.WAITING;
				velact = 0;
			}
			totalDistance += (localitation - localizacion); 
		}
	}

	@Override
	public JSONObject report() {
		JSONObject js = new JSONObject();
		
		js.put("id", _id);
		js.put("speed", velact);
		js.put("distance", totalDistance);
		js.put("co2", totalCont);
		js.put("class", contamination);
		js.put("status", vehicleStatus);
		if(vehicleStatus == VehicleStatus.TRAVELING || vehicleStatus == VehicleStatus.WAITING) {
		js.put("road", road);
		js.put("location", localitation);
		}

		return js;
	}
	
	void setSpeed(int s) {
		
	if(s < 0) {
		throw new IllegalArgumentException("La velocidad introducida debe ser mayor a 0");
	}else {
		if(s < this.maxSpeed) {
			this.velact = s;
		}else {
			this.velact = this.maxSpeed;
		}
	}
	
	}
	
	void setContaminationClass(int c) {
		
		if(c > 10 || c < 0) {
			throw new IllegalArgumentException("Nivel de contaminacion no valido, debe encontrarse entre [0,10]");
		}else {
			this.contamination = c;
		}
	}
	
	void moveToNextRoad() { 
		if(vehicleStatus == VehicleStatus.PENDING || vehicleStatus == VehicleStatus.WAITING) {
			Junction origen;
			Junction destino;
			if(vehicleStatus == VehicleStatus.PENDING) {
				origen = itinerary.get(num_cruces);
				num_cruces++;
			}
			else {
				localitation = 0;
				num_cruces++;
				road.exit(this);
				origen = road.getCruceD();
			}
			if(num_cruces == itinerary.size() && vehicleStatus == VehicleStatus.WAITING)
				vehicleStatus= VehicleStatus.ARRIVED;
			else{
				destino = itinerary.get(num_cruces);
				road = origen.roadTo(destino);
				road.enter(this);
				vehicleStatus= VehicleStatus.TRAVELING;}
			}
	
		else throw new IllegalArgumentException("El estado del vehiculo debe ser PENDING o WAITING");
	}
	
	public int compareTo(Vehicle v) { 
		if(localitation < v.localitation) return 1;
		else if(localitation > v.localitation) return -1;
		else return 0;
	}


	public int getContaminationClass() {
		return contamination;
	}
	
	
	public Road getRoad() {
		return this.road;
	}


	public int getLocalitation() {
		return this.localitation;
	}


	public int getVelact() {
		return velact;
	}
	
	public int getMaxSpeed() {
		return maxSpeed;
	}
	
	public int getTotalCont() {
		return totalCont;
	}
	
	public int getTotalDistance() {
		return totalDistance;
	}


	public VehicleStatus getVehicleStatus() {
		return vehicleStatus;
	}


	public List<Junction> getItinerary() {
		return Collections.unmodifiableList(new ArrayList<>(itinerary));
	}
}
	
