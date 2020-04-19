package simulator.model;

import java.util.List;

import simulator.misc.Pair;

public class NewSetContClassEvent extends Event {
	
	List<Pair<String, Integer>> cs;
	
	public NewSetContClassEvent(int time, List<Pair<String,Integer>> cs){
		super(time);

		if(cs ==null)
			throw new IllegalArgumentException("La lista no puede ser nula");
		else this.cs = cs;
	}

	@Override
	void execute(RoadMap map) {
		for(int i = 0; i < cs.size(); i++)  {
			Vehicle v = map.getVehicle(cs.get(i).getFirst());
			if(v!=null)
				v.setContaminationClass(cs.get(i).getSecond());
			else throw new IllegalArgumentException("El vehiculo no existe");
		}
	}
}