package simulator.view;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public class ChangeCO2ClassDialog extends JDialog{

	private static final long serialVersionUID = 1L; //NO SE.
	
	private int _status;
	private JComboBox<Dish> _dishes;
	private DefaultComboBoxModel<Dish> _dishesModel;

	public ChangeCO2ClassDialog() {
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

		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		JPanel viewsPanel = new JPanel();
		viewsPanel.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(viewsPanel);

		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setAlignmentX(CENTER_ALIGNMENT);
		//buttonsPanel.setAlignmentY(CENTER_ALIGNMENT);

		mainPanel.add(buttonsPanel);

		_dishesModel = new DefaultComboBoxModel<>();
		_dishes = new JComboBox<>(_dishesModel);
		Integer i[]= {0,1,2,3,4,5,6,7,8,9};
		JComboBox <String> s = new JComboBox<>();
		JComboBox  jc = new JComboBox(i);

		JSpinner sp = new JSpinner();
		viewsPanel.add(new JLabel("Vehicle: "));
		viewsPanel.add(s);
		viewsPanel.add(new JLabel("C02 Class: "));
		viewsPanel.add(jc);
		viewsPanel.add(new JLabel("Ticks: "));
		viewsPanel.add(sp);
		

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_status = 0;
				ChangeCO2ClassDialog.this.setVisible(false);
			}
		});
		buttonsPanel.add(cancelButton);

		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (_dishesModel.getSelectedItem() != null) {
					_status = 1;
					ChangeCO2ClassDialog.this.setVisible(false);
				}
			}
		});
		buttonsPanel.add(okButton);

		setPreferredSize(new Dimension(400, 180));
		pack();
		setVisible(true);
	}

	public int open(List<Dish> dishes) {

		// update the comboxBox model -- if you always use the same no
		// need to update it, you can initialize it in the constructor.
		//
		_dishesModel.removeAllElements();
		for (Dish v : dishes)
			_dishesModel.addElement(v);

		// You can chenge this to place the dialog in the middle of the parent window.
		// It can be done using uing getParent().getWidth, this.getWidth(),
		// getParent().getHeight, and this.getHeight(), etc.
		//
		setLocation(getParent().getLocation().x + 10, getParent().getLocation().y + 10);

		setVisible(true);
		return _status;
	}

	Dish getDish() {
		return (Dish) _dishesModel.getSelectedItem();
	}

}


