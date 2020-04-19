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

	
	private static final long serialVersionUID = 1L; //NO SE.
	
	private static final String _nameTime = " Time:  ";
	
	private Controller controller;
	private JLabel _currTime; // for current time
	
	
	public StatusBar(Controller _ctrl) {
		controller = _ctrl; 
		initGUI();
		controller.addObserver(this);
	}

	private void initGUI() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.setBorder(BorderFactory.createBevelBorder(1));
		_currTime = new JLabel(_nameTime + 0);
		_currTime.setPreferredSize(new Dimension(120, 15));
		this.add(_currTime);
		//FALTAN LOS EVENTOS.
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		_currTime.setText(_nameTime + time);
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}

}
