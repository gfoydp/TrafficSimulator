package simulator.view;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import simulator.model.Road;
import simulator.model.Weather;


public class ChangeWeatherDialog extends JDialog{

	private static final long serialVersionUID = 1L;
	private int _status;
	private JComboBox<Road> _roads;
	Weather w [] = { Weather.SUNNY,Weather.CLOUDY,Weather.RAINY,Weather.WINDY, Weather.STORM};
	JSpinner ticks;
	JComboBox<Weather> weather;



	public ChangeWeatherDialog(Frame parent) {
		super(parent, true);
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

		_roads = new JComboBox<>();
		_roads.setPreferredSize(new Dimension(45, 25));
		ticks = new JSpinner();
		ticks.setPreferredSize(new Dimension(45, 25));

		weather = new JComboBox<> (w);
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
				if (_roads.getSelectedItem() != null) {
					_status = 1;
					ChangeWeatherDialog.this.setVisible(false);
				}
			}
		});
		buttonsPanel.add(okButton);

		setPreferredSize(new Dimension(420, 180));
		pack();
		setVisible(false);
	}

	public int open(List<Road> roads) {
		if(roads.isEmpty()) throw new IllegalArgumentException("Roads empty list or not events loaded");
		_roads.removeAllItems();
		for (Road v : roads)
			_roads.addItem(v);
		
			setLocationRelativeTo(getParent());
			setVisible(true);

		return _status;
	}

	public Road getRoad() {
		return _roads.getItemAt(_roads.getSelectedIndex());
	}
	public int getTicks() {
		return (int) ticks.getValue();
	}
	public Weather getWeather() {
		return weather.getItemAt(weather.getSelectedIndex());
	}

}


