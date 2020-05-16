package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Road;

public class RoadsTableModel extends AbstractTableModel implements TrafficSimObserver{
	
	
	private static final long serialVersionUID = 1L;

	private final String columnNames[] = {"Id", "Lenght", "Weather", "Max. Speed", "Speed Limit", "Total CO2", "CO2 Limit"};
	private List<Road> _roads;
	
	public RoadsTableModel(Controller _ctrl) {
		_roads = new ArrayList<Road>();
		_ctrl.addObserver(this);
	}
	
	
	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}	
	
	
	private void updateTable(List<Road> r) {
		_roads = r;
		this.fireTableStructureChanged();
	}
	
	@Override
	public int getRowCount() {
		return _roads.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object obj = null;
		if(columnIndex == 0) {
			obj = _roads.get(rowIndex).getId();
		}
		if(columnIndex == 1) {
			obj =  _roads.get(rowIndex).getLength();
		}
		if(columnIndex == 2) {
			obj =_roads.get(rowIndex).getWeather();
		}
		if(columnIndex == 3) {
			obj = _roads.get(rowIndex).getMaxSpeed();
		}
		if(columnIndex == 4) {
			obj = _roads.get(rowIndex).getSpeedLimit();
		}
		if(columnIndex == 5) {
			obj = _roads.get(rowIndex).getTotalCO2();
		}
		if(columnIndex == 6) {
			obj = _roads.get(rowIndex).getCO2Limit();
		}
		return obj;
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
				
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		updateTable(map.getRoads());				
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		updateTable(map.getRoads());				
	}
	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		updateTable(map.getRoads());				
		
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		updateTable(map.getRoads());				
	}

	@Override
	public void onError(String err) {
		
	}
}