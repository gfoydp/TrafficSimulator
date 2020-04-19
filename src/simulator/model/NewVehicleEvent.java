package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class NewVehicleEvent extends Event {
	private String id;
	private int maxSpeed;
	private int contClass;
	private List<String> itinerary;
	
	public NewVehicleEvent(int time, String id, int maxSpeed, int contClass, List<String> itinerary) {
			super(time);
			this.id = id;
			this.maxSpeed = maxSpeed;
			this.contClass = contClass;
			this.itinerary = itinerary;
			}

	@Override
	void execute(RoadMap map) {
		List<Junction> iti = new ArrayList<>();
		for(int i = 0; i < this.itinerary.size() ;i++) {
			iti.add(map.getJunction(itinerary.get(i)));
		}
		
		Vehicle vehicle = new Vehicle (id, maxSpeed, contClass, iti);
		map.addVehicle(vehicle);
		vehicle.moveToNextRoad();
	}
	
	@Override
	public String toString() {
	return "New Vehicle '"+id+"'";
	}

}
