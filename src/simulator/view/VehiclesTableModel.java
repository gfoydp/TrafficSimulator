package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;

public class VehiclesTableModel extends AbstractTableModel implements TrafficSimObserver{
	
	
	private static final long serialVersionUID = 1L; //NO SE

	private final String columnNames[] = {"Id", "Location", "Itinerary", "CO2 Class", "Max. Speed", "Speed", "Total CO2", "Distance"};
	private List<Vehicle> _vehicles;
	
	public VehiclesTableModel(Controller _ctrl) {
		_vehicles = new ArrayList<Vehicle>();
		_ctrl.addObserver(this);
	}
	
	
	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}	
	
	
	private void updateTable(List<Vehicle> v) {
		_vehicles = v;
		this.fireTableStructureChanged();
	}
	
	@Override
	public int getRowCount() {
		return _vehicles.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object obj = null;
		if(columnIndex == 0) {
			obj = _vehicles.get(rowIndex).getId();
		}
		if(columnIndex == 1) {
			obj =  _vehicles.get(rowIndex).getRoad() + ":" + _vehicles.get(rowIndex).getLocalitation();
		}
		if(columnIndex == 2) {
			obj =_vehicles.get(rowIndex).getItinerary();
		}
		if(columnIndex == 3) {
			obj = _vehicles.get(rowIndex).getContaminationClass();
		}
		if(columnIndex == 4) {
			obj = _vehicles.get(rowIndex);
		}
		if(columnIndex == 5) {
			obj = _vehicles.get(rowIndex);
		}
		if(columnIndex == 6) {
			obj = _vehicles.get(rowIndex);
		}
		return obj;
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		updateTable(map.getVehicles());				
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		updateTable(map.getVehicles());				
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		updateTable(map.getVehicles());			}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		updateTable(map.getVehicles());		
		
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		updateTable(map.getVehicles());				
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}
	
}