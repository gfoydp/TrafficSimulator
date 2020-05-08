package simulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;


import org.json.JSONObject;

import extra.dialog.Dish;
import simulator.control.Controller;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.NewSetContClassEvent;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.SetWeatherEvent;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.Weather;


public class ControlPanel extends JPanel implements TrafficSimObserver{

	private static final long serialVersionUID = 1L; //NO SE.
	
	Controller controller;
	
	private JButton load , contaminacion, weather, run, stop , exit;
	private JSpinner ticks;
	private JFileChooser fc;
	private boolean _stopped;
	private int _time, _status;
	List<Road> _roads;
	List<Vehicle> _vehicles ;

	ChangeWeatherDialog cwd = null;
	ChangeCO2ClassDialog cccd = null;

	

	public ControlPanel(Controller _ctrl) { 
		controller = _ctrl;
		_stopped = true;
		initGUI();
		controller.addObserver(this);
	}
	
	
	private  void initGUI() { 
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		JToolBar toolbar = new JToolBar();
		
		load = new JButton();
		load.setIcon(new ImageIcon("resources/icons/open.png"));
		toolbar.add(load);
		load.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				fc = new JFileChooser();
				fc.setCurrentDirectory(new File("resources/examples"));
				int returnVal = fc.showOpenDialog(null);
				if(returnVal == JFileChooser.APPROVE_OPTION){
					File file = fc.getSelectedFile();
					controller.reset();
					InputStream in;
					try {
						in = new FileInputStream(file);
						controller.loadEvents(in);
					} catch (FileNotFoundException e) {
						JOptionPane.showMessageDialog(toolbar,
								 "Error to import the events.",
								 "Error",
								 JOptionPane.ERROR_MESSAGE);
					}
					}
		}});
		
		load.setToolTipText("Load a file");
		
		toolbar.addSeparator();
		
		contaminacion = new JButton();
		contaminacion.setIcon(new ImageIcon("resources/icons/co2class.png"));
		contaminacion.setToolTipText("Change C02 Class");
		Frame f = (Frame) SwingUtilities.getWindowAncestor(this);
		contaminacion.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {

				ChangeCO2ClassDialog cccd = new ChangeCO2ClassDialog(f);
				_status = cccd.open(_vehicles);
				if(_status == 1) {
					List<Pair<String,Integer>> pair = new ArrayList<>();
					pair.add(new Pair<String,Integer>(cccd.getVehicle(),cccd.getContClass()));
					controller.addEvent(new NewSetContClassEvent(cccd.getTicks() + _time, pair));	
				}
				
		}});
		
	

		toolbar.add(contaminacion);

		weather = new JButton();
		weather.setIcon(new ImageIcon("resources/icons/weather.png"));
		weather.setToolTipText("Change Road Weather");
		weather.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				cwd = new ChangeWeatherDialog(f);
				if(cwd.open(_roads) == 1) {
					List<Pair<String,Weather>> pair = new ArrayList<>();
					pair.add(new Pair<String,Weather>(cwd.getRoad(),cwd.getWeather()));
					controller.addEvent(new SetWeatherEvent(cwd.getTicks() + _time, pair));
				}
				
		}});

		toolbar.add(weather);
		
		toolbar.addSeparator();

		run = new JButton();
		run.setIcon(new ImageIcon("resources/icons/run.png"));
		run.setToolTipText("Start the simulator");
		run.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				_stopped = false;
				enableToolBar(false);
				run_sim(getTicks());
			}
		});
		toolbar.add(run);
	
		
		stop = new JButton();
		stop.setIcon(new ImageIcon("resources/icons/stop.png"));
		stop.setToolTipText("Stop the simulator");
		stop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				enableToolBar(true);
			}
		});
		toolbar.add(stop);
		
		toolbar.add(new JLabel("Ticks: "));
		ticks = new JSpinner(new SpinnerNumberModel(10, 1, 10000, 1));
		ticks.setToolTipText("Simulation tick to run: 1 - 10000");
		ticks.setPreferredSize(new Dimension(80, 35));
		ticks.setMinimumSize(new Dimension(80, 35));
		ticks.setMaximumSize(new Dimension(80, 35));
		toolbar.add(ticks);
		
		toolbar.add(Box.createHorizontalGlue());

		exit = new JButton();

		exit.setIcon(new ImageIcon("resources/icons/exit.png"));
		exit.setToolTipText("Exit");
		toolbar.add(exit);
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				Object[] options = { "Sí", "No"};
				int n = JOptionPane.showOptionDialog(toolbar,"¿Quiere salir?","Salir",
						 JOptionPane.YES_NO_OPTION,
						 JOptionPane.QUESTION_MESSAGE,null ,options, options[1]);
				if(n == 0){
					System.exit(0);
				}}
		});	
		this.add(toolbar);

	}
	
	void enableToolBar(boolean b) {
		load.setEnabled(b);
		contaminacion.setEnabled(b);
		weather.setEnabled(b);
		run.setEnabled(b);
		exit.setEnabled(b);
	}

	
	private void run_sim(int n) {
		if (n > 0 && !_stopped) {
			try {
				controller.run(1);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					_stopped = true;
					return;
					}
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					run_sim(n - 1);
					}
				});
			} else { 
				enableToolBar(true);
				_stopped = true;
			}
		}
	
	
private void stop() {
		_stopped = true;
		}

	
	
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		_roads = map.getRoads();
		_vehicles = map.getVehicles();
		_time = time;



		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		_roads = map.getRoads();
		_vehicles = map.getVehicles();
		_time = time;



		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
	/*	if(cccd.getEvent()!=null && cccd!=null) {
			Event ev;
			ev = cccd.getEvent();
			events.add(ev);
		}*/
		

		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		_roads = map.getRoads();
		_vehicles = map.getVehicles();
		_time = time;


		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		_roads = map.getRoads();
		_vehicles = map.getVehicles();
		_time = time;

		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}
	public  int getTicks(){
		return (int) ticks.getValue();
	}
}
