package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class MoveFirstStrategy implements DequeuingStrategy {

	
	public List<Vehicle> dequeue(List<Vehicle> q) { 
		List<Vehicle> aux = new ArrayList<>();
		if(!q.isEmpty()) 
			aux.add(q.get(0));
		
		return aux;
	}
	

}
