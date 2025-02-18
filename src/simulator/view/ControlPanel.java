package simulator.view;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

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


public class ControlPanel extends JPanel implements TrafficSimObserver, ActionListener{

	private static final long serialVersionUID = 1L;
	
	Controller controller;
	
	private JButton load , contaminacion, weather, run, stop , exit;
	private JSpinner ticks;
	private JFileChooser fc;
	private JToolBar toolbar;
	private Frame f;
	private boolean _stopped;
	private int _time;
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
		f = (Frame) SwingUtilities.getWindowAncestor(this);
		toolbar = new JToolBar();
		
		load = new JButton();
		load.setIcon(new ImageIcon("resources/icons/open.png"));
		load.addActionListener(this);
		load.setToolTipText("Load a file");
		
		toolbar.add(load);

		toolbar.addSeparator();
		
		contaminacion = new JButton();
		contaminacion.setIcon(new ImageIcon("resources/icons/co2class.png"));
		contaminacion.setToolTipText("Change C02 Class of a Vehicle");
		contaminacion.addActionListener(this);

		toolbar.add(contaminacion);

		weather = new JButton();
		weather.setIcon(new ImageIcon("resources/icons/weather.png"));
		weather.setToolTipText("Change Weather of a Road");
		weather.addActionListener(this);

		toolbar.add(weather);
		
		toolbar.addSeparator();

		run = new JButton();
		run.setIcon(new ImageIcon("resources/icons/run.png"));
		run.setToolTipText("Run the simulator");
		run.addActionListener(this);
	
		toolbar.add(run);
		
		stop = new JButton();
		stop.setIcon(new ImageIcon("resources/icons/stop.png"));
		stop.setToolTipText("Stop the simulator");
		stop.addActionListener(this);
		
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
		exit.addActionListener(this);
		
		toolbar.add(exit);
			
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
					JOptionPane.showMessageDialog(getParent(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					enableToolBar(true);
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
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == load) Load();
		else if(e.getSource() == contaminacion) Contaminacion();
		else if(e.getSource() == weather) Weather();
		else if(e.getSource() == run) Run();
		else if(e.getSource() == stop) Stop();
		else if(e.getSource() == exit) Exit();


	}

	
	private void Load() {
		fc = new JFileChooser();
		fc.setCurrentDirectory(new File("resources/examples"));
		int returnVal = fc.showOpenDialog(getParent());
		if(returnVal == JFileChooser.APPROVE_OPTION){
			File file = fc.getSelectedFile();
			controller.reset();
			InputStream in;
			try {
				in = new FileInputStream(file);
				controller.loadEvents(in);
			} catch (FileNotFoundException er) {
				JOptionPane.showMessageDialog(getParent(),
						 er.getMessage(),
						 "Error",
						 JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	
	private void Contaminacion() {
		try {
		cccd = new ChangeCO2ClassDialog(f);
		if(cccd.open(_vehicles) == 1) {
			List<Pair<String,Integer>> pair = new ArrayList<>();
			pair.add(new Pair<String,Integer>(cccd.getVehicle().toString(),cccd.getContClass()));
			controller.addEvent(new NewSetContClassEvent(cccd.getTicks() + _time, pair));	
		}
		}catch (IllegalArgumentException er) {
			JOptionPane.showMessageDialog(getParent(),
					 er.getMessage(),
					 "Error",
					 JOptionPane.ERROR_MESSAGE);
			}
	}
	
	private void Weather() {
		try {
		cwd = new ChangeWeatherDialog(f);
		if(cwd.open(_roads) == 1) {
			List<Pair<String,Weather>> pair = new ArrayList<>();
			pair.add(new Pair<String,Weather>(cwd.getRoad().toString(),cwd.getWeather()));
			controller.addEvent(new SetWeatherEvent(cwd.getTicks() + _time, pair));
		}
		}catch (IllegalArgumentException er) {
			JOptionPane.showMessageDialog(getParent(),
					 er.getMessage(),
					 "Error",
					 JOptionPane.ERROR_MESSAGE);
			}
		
	}
	
	private void Run() {
		_stopped = false;
		enableToolBar(false);
		run_sim(getTicks());
	}
	
	private void Stop() {
		_stopped = true;
		enableToolBar(true);
	}
	
	private void Exit() {
		ImageIcon icon = new ImageIcon("resources/icons/exit.png");
	   	int n = JOptionPane.showOptionDialog(getParent(), "Are sure you want to quit?", "Quit", JOptionPane.YES_NO_OPTION,
	   			JOptionPane.PLAIN_MESSAGE, icon, null, null);
	   	if(n == 0) System.exit(0);
	}
	
	void update(RoadMap map, int time) {
		_roads = map.getRoads();
		_vehicles = map.getVehicles();
		_time = time;

	}
	
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		update(map,time);
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		update(map,time);
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		update(map,time);
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		update(map,time);
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		update(map,time);
	}

	@Override
	public void onError(String err) {
		JOptionPane.showMessageDialog(getParent(),err, "Error",JOptionPane.ERROR_MESSAGE);
	}
	
	public  int getTicks(){
		return (int) ticks.getValue();
	}

}
