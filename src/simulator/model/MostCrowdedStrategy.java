package simulator.model;

import java.util.List;

public class MostCrowdedStrategy implements LightSwitchingStrategy {
	
	private int timeSlot;
	
	public MostCrowdedStrategy(int timeSlot){
		this.timeSlot = timeSlot;
	}

	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime,int currTime) {
		int pos = 0;
		int max = 0;
		
		if(roads.isEmpty())
			return -1;
		
		if(currGreen == -1) {
			
			for(int i = qs.size() ; i  > 0; i--) {
				if( qs.get(i).size() > max ) {
					max = qs.get(i).size();
					pos = i;
				}
			}
			return pos;
		}
			
			
			if(currTime-lastSwitchingTime < timeSlot)
				return currGreen;
			
			for(int i = qs.size(); i > (currGreen + 1) % qs.size(); i--) {
				if( qs.get(i).size() > max ) {
					max = qs.get(i).size();
					i = pos;
				}			
		}
		return pos;
	 }

}
