package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MoveAllStrategy implements DequeuingStrategy {
	
	public MoveAllStrategy() {
		
	}

	
	public List<Vehicle> dequeue(List<Vehicle> q) {
		if(q.isEmpty())
		return new ArrayList<Vehicle>();
		else return  Collections.unmodifiableList(new ArrayList<Vehicle>(q));
	}

}
