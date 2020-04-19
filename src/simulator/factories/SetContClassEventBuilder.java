package simulator.factories;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.NewSetContClassEvent;

public class SetContClassEventBuilder extends Builder<Event> {

	public SetContClassEventBuilder(String type) {
		super(type);
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		int tiempo = data.getInt("time");
		List<Pair<String,Integer>> cs = new LinkedList<>();
		JSONArray pair = data.getJSONArray("info");
		String vehicle;
		int cclass;
		for(int i = 0 ;i < pair.length();i++) {
			vehicle = pair.getJSONObject(i).getString("vehicle");
			cclass = pair.getJSONObject(i).getInt("class");
			Pair<String,Integer> aux = new Pair<String,Integer>(vehicle,cclass);
			cs.add(aux);
		}
		return new NewSetContClassEvent(tiempo, cs);
	}

}
