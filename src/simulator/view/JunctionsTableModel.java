package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class JunctionsTableModel extends AbstractTableModel implements TrafficSimObserver{
	
	
	private static final long serialVersionUID = 1L; //NO SE

	private final String columnNames[] = {"Id", "Green", "Queues"};
	private List<Junction> _junction;

	
	
	public JunctionsTableModel(Controller _ctrl) {
		_junction = new ArrayList<Junction>();
		_ctrl.addObserver(this);
	}

	
	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}	
	
	
	private void updateTable(List<Junction> j) {
		_junction = j;
		this.fireTableStructureChanged();
	}
	
	@Override
	public int getRowCount() {
		return _junction.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object obj = null;
		if(columnIndex == 0) {
			obj = _junction.get(rowIndex).getId();
		}
		if(columnIndex == 1) {
			obj = _junction.get(rowIndex).getGreenLightIndex();
			if(_junction.get(rowIndex).getGreenLightIndex() == -1)
				obj = "NONE";
		}
		if(columnIndex == 2) {
			obj = _junction.get(rowIndex).getInRoads();

		}
		return obj;
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		updateTable(map.getJunctions());		
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		updateTable(map.getJunctions());		
		
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		updateTable(map.getJunctions());		
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		updateTable(map.getJunctions());		
		
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		updateTable(map.getJunctions());				
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}




}
