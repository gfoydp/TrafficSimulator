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

import extra.dialog.Dish;
import simulator.control.Controller;
import simulator.misc.Pair;
import simulator.model.NewSetContClassEvent;
import simulator.model.Road;
import simulator.model.SetWeatherEvent;
import simulator.model.Vehicle;
import simulator.model.Weather;

public class ChangeCO2ClassDialog extends JDialog{

	private static final long serialVersionUID = 1L;
	
	private int _status;
	private JComboBox<Vehicle> _vehicles;
	private JComboBox  ContClass;
	private DefaultComboBoxModel<Vehicle> _vehiclesModel;
	private Controller ctr;
	private JSpinner ticks;
	private int _time;
	
	public ChangeCO2ClassDialog(Controller ctr, int time) {
		_time = time;
		this.ctr = ctr;
		initGUI();
	}

	private void initGUI() {

		_status = 0;

		setTitle("Change CO2 Class");
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		setContentPane(mainPanel);

		JTextArea helpMsg = new JTextArea("Schedule an event to change the CO2 class of a vehicle after a given number of simulation ticks from now.");
	    helpMsg.setLineWrap(true);
	    helpMsg.setOpaque(false);
	    helpMsg.setEditable(false);
	    helpMsg.setFont(UIManager.getFont("Label.font"));

		mainPanel.add(helpMsg);

		mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

		JPanel viewsPanel = new JPanel();
		viewsPanel.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(viewsPanel);

		mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setAlignmentX(CENTER_ALIGNMENT);

		mainPanel.add(buttonsPanel);

		_vehiclesModel = new DefaultComboBoxModel<>();
		_vehicles = new JComboBox<>(_vehiclesModel);
		Integer i[]= {0,1,2,3,4,5,6,7,8,9};
		_vehicles.setPreferredSize(new Dimension(45, 25));

		ContClass = new JComboBox(i);
		ContClass.setPreferredSize(new Dimension(45, 25));

		ticks = new JSpinner();
		ticks.setPreferredSize(new Dimension(45, 25));
		
		viewsPanel.add(new JLabel("Vehicle: "));
		viewsPanel.add(_vehicles);
		viewsPanel.add(new JLabel("C02 Class: "));
		viewsPanel.add(ContClass);
		viewsPanel.add(new JLabel("Ticks: "));
		viewsPanel.add(ticks);
		

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			setVisible(false);
			}
		});
		buttonsPanel.add(cancelButton);

		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (_vehiclesModel.getSelectedItem() != null) {
					List<Pair<String,Integer>> pair = new ArrayList<>();
					pair.add(new Pair<String,Integer>(getVehicle(),getContClass()));
					ctr.addEvent(new NewSetContClassEvent(getTicks(), pair));
					ChangeCO2ClassDialog.this.setVisible(false);
				}
			}
		});
		buttonsPanel.add(okButton);

		setPreferredSize(new Dimension(400, 180));
		pack();
	}

	public void open(List<Vehicle> vehicles) {

		_vehiclesModel.removeAllElements();
		for (Vehicle v : vehicles)
			_vehiclesModel.addElement(v);

		setVisible(true);
	}

	public String getVehicle() {
		return _vehiclesModel.getSelectedItem().toString();
	}
	public int getTicks() {
		return (int) ticks.getValue() + _time;
	}
	public int getContClass() {
		return (int) ContClass.getSelectedItem();
	}
	

}


