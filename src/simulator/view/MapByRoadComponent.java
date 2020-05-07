package simulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JLabel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.VehicleStatus;

public class MapByRoadComponent extends JComponent implements TrafficSimObserver {

	
	private static final long serialVersionUID = 1L; 
	private static final Color _BG_COLOR = Color.WHITE;
	private static final int _JRADIUS = 10;
	private static final Color _JUNCTION_COLOR = Color.BLUE;
	private static final Color _JUNCTION_LABEL_COLOR = new Color(200, 100, 0);
	private static final Color _GREEN_LIGHT_COLOR = Color.GREEN;
	private static final Color _RED_LIGHT_COLOR = Color.RED;
	private RoadMap _map;
	private Image _car, _cloud, _c0, _c1, _c2, _c3, _c4, _c5, _rain, _storm, _sun, _wind;
	private JLabel label;

	
	public MapByRoadComponent(Controller ctrl) {
		initGUI();
		setPreferredSize (new Dimension (300, 200));
		ctrl.addObserver(this);

	}
	
	private void initGUI() {
		_car = loadImage("car.png");
		_cloud = loadImage("cloud.png");
		_c0 = loadImage("cont_0.png");
		_c1 = loadImage("cont_1.png");
		_c2 = loadImage("cont_2.png");
		_c3 = loadImage("cont_3.png");
		_c4 = loadImage("cont_4.png");
		_c5 = loadImage("cont_5.png");
		_rain = loadImage("rain.png");
		_storm = loadImage("storm.png");
		_sun = loadImage("sun.png");
		_wind = loadImage("wind.png");


	}
	
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// clear with a background color
		g.setColor(_BG_COLOR);
		g.clearRect(0, 0, getWidth(), getHeight());

		if (_map == null || _map.getJunctions().isEmpty()) {
			g.setColor(Color.red);
			g.drawString("No map yet!", getWidth() / 2 - 50, getHeight() / 2);
		} else {
			//updatePrefferedSize();
			drawMap(g);
		}
	}
	
	private void drawMap(Graphics g) {
		drawRoads(g);
		drawVehicles(g);
		drawJunctions(g);
	}
	
	private void drawRoads(Graphics g) {
		int  x1 = 50, x2 = getWidth() - 100 , y;
		g.setColor(Color.black);
		List<Road> r = _map.getRoads();

		for(int i = 0; i < r.size(); i++) {
			y = (i + 1)*50;
			g.drawString(r.get(i).toString(), 20, y + 3);
			g.drawLine(x1, y, x2, y);
		}
	}
	
	
	private void drawVehicles(Graphics g) {
		int  x1 = 50, x2 = getWidth() - 100;
		List<Vehicle> v = _map.getVehicles();
		
		for(int i = 0; i < v.size(); i++) {
			Road r = v.get(i).getRoad();
			int A = v.get(i).getLocalitation();
			int B = r.getLength();
			int y = (i + 1)*50;
			int x = x1 + (int) ((x2 - x1) * ((double) A / (double) B));
			g.drawImage(_car, x, y, 16, 16, this);
		}
	}

	private void drawJunctions(Graphics g) {
		int x = 50;
		int x2 = getWidth() - 100;
		List<Road> r = _map.getRoads();
		for (int i = 0 ; i < r.size(); i++) {
			int y = (i + 1)*50;
			// draw a circle with center at (x,y) with radius _JRADIUS
			g.setColor(_JUNCTION_COLOR);
			g.fillOval(x - _JRADIUS / 2, y - _JRADIUS / 2, _JRADIUS, _JRADIUS);

			// draw the junction's identifier at (x,y)
			g.setColor(_JUNCTION_LABEL_COLOR);
			g.drawString(r.get(i).getCruceO().toString(), x - 5, y - 10);

		}
		for (int i = 0 ; i < r.size(); i++) {
			int y = (i + 1)*50;
			// draw a circle with center at (x,y) with radius _JRADIUS
			g.setColor(_RED_LIGHT_COLOR);
			int idx = r.get(i).getCruceD().getGreenLightIndex();
			if (idx != -1 && r.get(i).equals(_map.getRoads().get(i).getCruceD().getInRoads().get(idx))) {
				g.setColor(_GREEN_LIGHT_COLOR);
			}
			g.fillOval(x2 - _JRADIUS / 2, y - _JRADIUS / 2, _JRADIUS, _JRADIUS);

			// draw the junction's identifier at (x,y)
			g.setColor(_JUNCTION_LABEL_COLOR);
			g.drawString(r.get(i).getCruceD().toString(), x2 - 5, y - 10);

		}
	}

	
	private Image loadImage(String img) {
		Image i = null;
		try {
			return ImageIO.read(new File("resources/icons/" + img));
		} catch (IOException e) {
		}
		return i;
	}
	
	public void update(RoadMap map) {
		_map = map;
		repaint();
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		update(map);
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onError(String err) {
	}
}
