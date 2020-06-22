package simulator.model;


public class CityRoad extends Road {

	CityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);}

	@Override
	void reduceTotalContamination() {
		int x = 2;
		if(weather == Weather.WINDY || weather == Weather.STORM) {
			x = 10;
		}
		if(totalCont >= x)
			totalCont -= x;
		else totalCont = 0;
	}

	@Override
	void updateSpeedLimit() {
	}

	@Override
	int calculateVehicleSpeed(Vehicle v) {
		int speed = (int)(((11.0 - v.getContaminationClass()) / 11.0) * speedLimit);
		return speed;
	}

}
