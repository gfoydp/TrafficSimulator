package simulator.factories;

import org.json.JSONObject;

import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.NewJunctionEvent;

public class NewJunctionEventBuilder extends Builder<Event> {
	
	Factory<LightSwitchingStrategy> lssFactory;
	Factory<DequeuingStrategy> dqsFactory;

	public NewJunctionEventBuilder(String type, Factory<LightSwitchingStrategy>lssFactory, Factory<DequeuingStrategy> dqsFactory) {
		super(type);
		this.lssFactory = lssFactory;
		this.dqsFactory = dqsFactory;
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		int tiempo =  data.getInt("time");
		String id = data.getString("id");
		int x = data.getJSONArray("coor").getInt(0);
		int y = data.getJSONArray("coor").getInt(1);
		LightSwitchingStrategy lsStrategy = lssFactory.createInstance(data.getJSONObject("ls_strategy"));
		DequeuingStrategy dqStrategy = dqsFactory.createInstance(data.getJSONObject("dq_strategy"));
		
		return new NewJunctionEvent(tiempo, id, lsStrategy, dqStrategy, x, y);
	}

}
