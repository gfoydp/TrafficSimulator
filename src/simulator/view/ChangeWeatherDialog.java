package simulator.view;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import com.sun.jdi.event.Event;

import simulator.control.Controller;
import simulator.misc.Pair;
import simulator.model.NewSetContClassEvent;
import simulator.model.Road;
import simulator.model.SetWeatherEvent;
import simulator.model.Weather;


public class ChangeWeatherDialog extends JDialog{

	private static final long serialVersionUID = 1L; //NO SE.
	
	private int _status;
	private JComboBox<Road> _roads;
	private DefaultComboBoxModel<Road> _roadsModel;
	Weather w [] = {Weather.CLOUDY,Weather.RAINY, Weather.STORM, Weather.SUNNY,Weather.WINDY};
	JSpinner ticks;
	JComboBox weather;
	SetWeatherEvent event = null;
	Controller ctr;
	int _time;



	public ChangeWeatherDialog(Controller ctr, int time) {
		time = _time;
		this.ctr = ctr;
		initGUI();
	}

	private void initGUI() {

		_status = 0;

		setTitle("Change Road Weather");
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		setContentPane(mainPanel);

		JTextArea helpMsg = new JTextArea("Schedule an event to change the weather of a road after a given number of simulation ticks from now.");
	    helpMsg.setLineWrap(true);
	    helpMsg.setOpaque(false);
	    helpMsg.setEditable(false);
	    helpMsg.setFont(UIManager.getFont("Label.font"));

		mainPanel.add(helpMsg);

		mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

		JPanel viewsPanel = new JPanel();
		viewsPanel.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(viewsPanel);

		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setAlignmentX(CENTER_ALIGNMENT);

		mainPanel.add(buttonsPanel);

		_roadsModel = new DefaultComboBoxModel<>();
		_roads = new JComboBox<>(_roadsModel);
		_roads.setPreferredSize(new Dimension(45, 25));
		ticks = new JSpinner();
		ticks.setPreferredSize(new Dimension(45, 25));

		weather = new JComboBox(w);
		viewsPanel.add(new JLabel("Road: "));
		viewsPanel.add(_roads);
		viewsPanel.add(new JLabel("Weather: "));
		viewsPanel.add(weather);
		viewsPanel.add(new JLabel("Ticks: "));
		viewsPanel.add(ticks);
		

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_status = 0;
				ChangeWeatherDialog.this.setVisible(false);
			}
		});
		buttonsPanel.add(cancelButton);

		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (_roadsModel.getSelectedItem() != null) {
					List<Pair<String,Weather>> pair = new ArrayList<>();
					pair.add(new Pair<String,Weather>(getRoad(),getWeather()));
					ctr.addEvent(new SetWeatherEvent(getTicks(), pair));
					ChangeWeatherDialog.this.setVisible(false);
				}
			}
		});
		buttonsPanel.add(okButton);

		setPreferredSize(new Dimension(420, 180));
		pack();
		setVisible(true);
	}

	public int open(List<Road> roads) {

		_roadsModel.removeAllElements();
		for (Road v : roads)
			_roadsModel.addElement(v);

		return _status;
	}

	public String getRoad() {
		return _roadsModel.getSelectedItem().toString();
	}
	public int getTicks() {
		return (int) ticks.getValue() + _time;
	}
	public Weather getWeather() {
		return (Weather) weather.getSelectedItem();
	}

}


