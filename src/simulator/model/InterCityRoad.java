package simulator.model;

public class InterCityRoad extends Road {

	InterCityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length,Weather weather){
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
		}
	
	 void reduceTotalContamination() {
		int x = 0;
		if(weather == Weather.SUNNY) 
			x = 2;
		if(weather == Weather.CLOUDY) 
			x = 3;
		if(weather == Weather.RAINY) 
			x = 10;
		if(weather == Weather.WINDY) 
			x = 15;
		if(weather == Weather.STORM) 
			x = 20;
		
		int y = (int) (((100.0 - x) / 100.0) * totalCont);
		totalCont = y; 
		}
	
	void updateSpeedLimit() {
		
		if(totalCont > contLimit) 
		speedLimit = (int) (maxSpeed * 0.5);
		else speedLimit = maxSpeed;
	}
	
	int calculateVehicleSpeed (Vehicle v) {
		
		if(weather == Weather.STORM)
			speedLimit = (int)(speedLimit * 0.8);
		return speedLimit;
	}

}
