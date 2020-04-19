package simulator.model;

public class NewInterCityRoad extends Event {

	private String id;
	private String srcJun;
	private	String destJunc; 
	private int length;
	private	int co2Limit; 
	private	int maxSpeed;
	private	Weather weather;
	
	public NewInterCityRoad(int time, String id, String srcJun, String destJunc, int length, int co2Limit, int maxSpeed, Weather weather){
	super(time);
	
	this.id = id;
	this.srcJun = srcJun;
	this.destJunc = destJunc;
	this.length = length;
	this.co2Limit = co2Limit;
	this.maxSpeed = maxSpeed;
	this.weather = weather;	
	}

	@Override
	void execute(RoadMap map) {
		InterCityRoad NewRoad = new InterCityRoad(id, map.getJunction(srcJun), map.getJunction(destJunc), maxSpeed, co2Limit, length, weather);
		map.addRoad(NewRoad);
	}

	@Override
	public String toString() {
	return "New InterCityRoad '"+id+"'";
	}	
}