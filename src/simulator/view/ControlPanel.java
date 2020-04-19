package simulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.json.JSONObject;

import extra.dialog.Dish;
import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;


public class ControlPanel extends JPanel implements TrafficSimObserver{

	private static final long serialVersionUID = 1L; //NO SE.
	
	Controller controller;
	
	private JButton load , contaminacion, weather, run, stop , exit;
	private JSpinner ticks;
	private JFileChooser fc;
	private boolean _stopped;
	

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
		contaminacion.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				ChangeCO2ClassDialog cccd = new ChangeCO2ClassDialog();
				List<Dish> dishes = new ArrayList<Dish>();
				for (int i = 0; i < 10; i++) {
					
					dishes.add(new Dish("" + i));
				}

				int status = cccd.open(dishes);

				if (status == 0) {
					System.out.println("Canceled");
				} else {
					System.out.println("Your favorite dish is: " + cccd.getDish());
				}
		}});
		
	

		toolbar.add(contaminacion);

		weather = new JButton();
		weather.setIcon(new ImageIcon("resources/icons/weather.png"));
		weather.setToolTipText("Change Road Weather");
		weather.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				ChangeWeatherDialog cccd = new ChangeWeatherDialog();
				List<Dish> dishes = new ArrayList<Dish>();
				for (int i = 0; i < 10; i++) {
					
					dishes.add(new Dish("" + i));
				}

				int status = cccd.open(dishes);

				if (status == 0) {
					System.out.println("Canceled");
				} else {
					System.out.println("Your favorite dish is: " + cccd.getDish());
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
					// TODO show error message
					_stopped = true;
					return;
					}
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					run_sim(n - 1);
					}
				});
			} else 
				enableToolBar(true);
		_stopped = true;
		}
	
	
private void stop() {
		_stopped = true;
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
