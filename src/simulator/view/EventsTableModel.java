package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;
import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class EventsTableModel extends AbstractTableModel implements TrafficSimObserver {
	
	private static final long serialVersionUID = 1L; 
	private final String columnNames[] = {"Time", "Desc."};
	private List<Event> _events;

	
	
	public EventsTableModel(Controller _ctrl) {
		_events = new ArrayList<Event>();
		_ctrl.addObserver(this);
	}

	
	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}	
	
	
	private void updateTable(List<Event> events) {
		_events = events;
		this.fireTableStructureChanged();
	}
	
	@Override
	public int getRowCount() {
		return _events.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object obj = null;
		if(columnIndex == 0) {
			obj = _events.get(rowIndex).getTime();
		}
		if(columnIndex == 1) {
			obj = _events.get(rowIndex).toString();
		}
		return obj;
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		updateTable(events);		
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		updateTable(events);		
		
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		updateTable(events);		
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		updateTable(events);		
		
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		updateTable(events);		
		
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}


}
