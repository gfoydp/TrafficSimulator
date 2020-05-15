package simulator.model;

public abstract class NewRoadEvent extends Event {
	
	public NewRoadEvent(int time) {
		super(time);
	}

	void execute(RoadMap map) {
		Road r = execute2(map);
		map.addRoad(r);
	}
	
	abstract Road execute2(RoadMap map);
}
