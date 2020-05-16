package simulator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class StatusBar extends JPanel implements TrafficSimObserver{

	
	private static final long serialVersionUID = 1L; 
		
	private Controller controller;
	private JLabel _currTime; 
	private JLabel _event;
	List<Event> _events;
	
	
	public StatusBar(Controller _ctrl) {
		controller = _ctrl; 
		initGUI();
		controller.addObserver(this);
	}

	private void initGUI() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.setBorder(BorderFactory.createBevelBorder(1));
		_currTime = new JLabel("Time:  " + 0);
		_currTime.setPreferredSize(new Dimension(120, 15));
		this.add(_currTime);
		_event = new JLabel("Welcome !");
		this.add(_event);
		}
	

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		_currTime.setText("Time:  " + time);
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		_event.setText("Event added (" + e.toString() + ")");
		
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		_currTime.setText("Time:  " + time);
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onError(String err) {
		
	}

}
