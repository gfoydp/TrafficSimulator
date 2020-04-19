package simulator.factories;

import org.json.JSONObject;

import simulator.model.LightSwitchingStrategy;
import simulator.model.RoundRobinStrategy;

public class RoundRobinStrategyBuilder extends Builder<LightSwitchingStrategy> {

	public RoundRobinStrategyBuilder(String type) {
		super(type);
	}

	@Override
	protected LightSwitchingStrategy createTheInstance(JSONObject data) {
		int timeSlot;
		if(data.isEmpty())
			timeSlot = 1;
		else timeSlot = data.getInt("timeslot");
		
		return new RoundRobinStrategy(timeSlot);
	}

}
