/*
 * Class description: Map is used in both admin and user screens
 * userscreen does not have allowed any actionlisteners
 * adminscreen can interact wiht the map
 */

package Interface;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.EmptyStackException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;

import collections.GraphOfStations;
import planner.Route;
import planner.Station;

public class Map extends JPanel implements MouseListener, MouseMotionListener {
	// ovals = masterList of all the ovals(Stations) that exist
	// lines = mastetList of all the lines(Edges) that exist
	private ArrayList<Oval> ovals = new ArrayList<Oval>();
	private ArrayList<Line> lines = new ArrayList<Line>();

	// Variables used when dragging and drawing routes on Map
	private Oval routeStart;
	private boolean drawingRoute = false;
	// Variables used to check authenticity when drawing routes on Map
	private Line lineTemp;
	private Oval ovalTemp;

	// Oval(Station) sizes, and route circle size
	private static int STATION_DIAMETER = 25;
	private static int ROUTE_START_DIAMETER = 15;

	// Variables used in drawing bestRoute on Map
	private boolean drawBestRoute = false;
	private ArrayList<Oval> routeOvals = new ArrayList<Oval>();
	private ArrayList<Line> routeLines = new ArrayList<Line>();

	// Variables used in drawing passenger myRoute on Map
	private boolean drawMyRoute = false;
	private ArrayList<Oval> routeMyOvals = new ArrayList<Oval>();
	private ArrayList<Line> routeMyLines = new ArrayList<Line>();

	// Used when hovering mouse over an oval
	// and displaying the corresponding statation data from it
	private boolean hoveringOval = false;
	private Oval hoveredOval;

	// Miscellaneous Variables
	private GraphOfStations gos;
	private JSlider ts; // timeSlider
	private int[][] pl; // passengerLocations

	public Map(boolean restricted, GraphOfStations gos, JSlider timeSlider, int[][] passengerLocations) {
		this.setBackground(Color.white);
		this.setForeground(null);
		this.gos = gos; // passed from User/Admin screen
		this.ts = timeSlider; // passed from AdminScreen
		this.pl = passengerLocations; // passed from AdminScreen
		this.updateMap(); // Draw data to map
		this.setPreferredSize(new Dimension(1000, 1000));

		// userScreen won't be able to edit
		if (!restricted) {
			this.addMouseListener(this);
			this.addMouseMotionListener(this);
		}
	}

	//Constantly draw whatever is in this method to Map
	@Override
	protected void paintComponent(Graphics g2) {
		super.paintComponent(g2);
		Graphics2D g = (Graphics2D) g2;
		
		drawOvalsAndLines(g);
		if (drawingRoute) {// draws red circle
			g.setColor(Color.red);
			g.fillOval(routeStart.getX(), routeStart.getY(), routeStart.getD(), routeStart.getD());
		}
		if (drawBestRoute)
			drawBestRoute(g);
		if (drawMyRoute)
			drawMyRoute(g);
		
		drawOvalNames(g);
		if (hoveringOval)
			showStationData(g);
		
		repaint();
	}
	
	public void drawOvalsAndLines(Graphics2D g) {
		g.setColor(Color.black);
		for (Oval o : ovals) // constantly draws stations
			if (o != null)
				g.fillOval(o.getX(), o.getY(), o.getD(), o.getD());
		for (Line l : lines) // constantly draw routes
			if (l != null) {
				g.setStroke(new BasicStroke(5));
				g.drawLine(l.getStart().x, l.getStart().y, l.getEnd().x, l.getEnd().y);
			}
	}

	public void drawBestRoute(Graphics2D g) {
		g.setColor(Color.green);
		//draw all the bestRoute stations green
		for (Oval ro : routeOvals) {
			if (ro != null) {
				g.fillOval(ro.getX(), ro.getY(), ro.getD(), ro.getD());
			}
		}
		//draw all the bestRoute edges green
		for (Line rl : routeLines)
			if (rl != null) {
				g.setStroke(new BasicStroke(5));
				g.drawLine(rl.getStart().x, rl.getStart().y, rl.getEnd().x, rl.getEnd().y);
			}
		g.setColor(Color.black); //change color back to black
	}

	public void drawMyRoute(Graphics2D g) {
		g.setColor(Color.yellow);
		boolean drawStart = true; //distincts the starting station
		Oval endOval = null;
		for (Oval o : routeMyOvals) {
			if (o != null) {
				if (drawStart) {
					g.fillOval(o.getX(), o.getY(), o.getD(), o.getD());
					drawStart = false;
				}
				endOval = o;
			}
		}
		//draw ending station
		g.drawOval(endOval.getX(), endOval.getY(), endOval.getD(), endOval.getD());
		
		for (Line rl : routeMyLines) // constantly draw routes
			if (rl != null) {
				g.setStroke(new BasicStroke(3));
				g.drawLine(rl.getStart().x, rl.getStart().y, rl.getEnd().x, rl.getEnd().y);
			}
		g.setColor(Color.black);
	}
	
	// constantly draws ovalNames
	public void drawOvalNames(Graphics2D g) {
		for (Oval o : ovals) 
			if (o != null) {
				g.setColor(Color.blue);
				g.setFont(new Font("Arial", Font.BOLD, 15));
				g.drawString(o.getName(), o.getX(), o.getY());
				g.setColor(Color.black);
			}
	}

	public int getPassengerCount(int id) {
		int time = ts.getValue();
		// System.out.println(id + " " + time + "|" + this.pl[id][time]);
		return this.pl[id][time];
	}

	public void showStationData(Graphics2D g) {
		int x = hoveredOval.x, y = hoveredOval.y;
		// draw black-bordered white background rectangle to display data
		g.setColor(Color.white);
		g.fillRect(x, y, 100, 50);
		g.setColor(Color.black);
		g.setStroke(new BasicStroke(1));
		g.drawRect(x, y, 100, 50);

		g.setFont(new Font("Arial", Font.PLAIN, 12));
		Station temp = gos.getStationByPoint(hoveredOval.getPoint());
		g.drawString("StationName: " + temp.getName(), x, y + 10);

		if (temp.getPassengers() != null)
			g.drawString("Passengers : " + getPassengerCount(temp.getStationId()), x, y + 20);
	}

	// Used when trying to draw routes on the Map
	// makes sure that the user has dragged and moved to another oval
	public boolean sameStation(Oval temp) {
		if (ovalTemp == temp)
			return true;
		else
			return false;
	}

	/**
	 * Given a Point, returns an Oval if such an Oval exist with the similar Point
	 * 
	 * @precondition - clickedOnStation() should be called before this method is
	 *               called
	 * @param p
	 *            - a point on the Mao
	 * @return Oval - loops through ovals array to find oval with the matching Point
	 */
	public Oval getClickedStation(Point p) {
		for (Oval o : ovals)
			if (p.x >= o.getXMin() && p.x <= o.getXMax() && p.y >= o.getYMin() && p.y <= o.getYMax())
				return o;
		return null;
	}

	/**
	 * Checks that p is in the vicinity of an Oval in the Ovals ArrayList
	 * 
	 * @param p
	 *            - the point clicked by the mouse
	 * @return true if p is in the vicinity of existing Oval
	 */
	public boolean clickedOnStation(Point p) {
		for (Oval o : ovals)
			if (p.x >= o.getXMin() && p.x <= o.getXMax() && p.y >= o.getYMin() && p.y <= o.getYMax())
				return true;
		return false;
	}

	// stops drawing passenger myRoute on Map
	public void removeMyRoute() {
		drawMyRoute = false;
	}

	// draw map given the GraphOfStation variables
	public void updateMap() {
		// Converts stations to ovals
		ArrayList<Station> stationList = gos.getStationList();
		ArrayList<Oval> newOvals = new ArrayList<Oval>();
		for (int x = 0; x < stationList.size(); x++)
			if (stationList.get(x) != null)
				newOvals.add(new Oval(stationList.get(x).getVertexCoordinate(), STATION_DIAMETER,
						stationList.get(x).getName(), true));
		ovals = newOvals; // completely write over the old ovals

		// Convert edges to lines
		ArrayList<String> stationEdges = gos.getStationEdges();
		ArrayList<Line> newLines = new ArrayList<Line>();// newLines is used to get rid of duplicated drawn lines
		for (int x = 0; x < stationEdges.size(); x++)
			newLines.add(new Line(gos.getStationByName(stationEdges.get(x).split("asdflmfao")[0]).getVertexCoordinate(),
					gos.getStationByName(stationEdges.get(x).split("asdflmfao")[1]).getVertexCoordinate()));
		this.lines = newLines; // completely write over the old lines
	}

	/**
	 * 
	 * @param route
	 *            - can either be bestRoute from GOS, or myRoute from Passenger
	 * @param myRoute
	 *            - true is passed myRoute, false if passed bestRoute
	 */
	public void updateMap(Route route, boolean myRoute) {
		Station temp = null;
		if (myRoute) {
			routeMyOvals = new ArrayList<Oval>();
			routeMyLines = new ArrayList<Line>();
		} else {
			routeOvals = new ArrayList<Oval>();
			routeLines = new ArrayList<Line>();
		}
		try {
			// convert stations to ovals
			while (true) {
				temp = route.pop();
				if (myRoute)
					routeMyOvals.add(new Oval(temp.getVertexCoordinate(), STATION_DIAMETER, temp.getName(), true));
				else
					routeOvals.add(new Oval(temp.getVertexCoordinate(), STATION_DIAMETER, temp.getName(), true));
			}
		} catch (EmptyStackException e) {
			// no more stations to pop
			// connect the dots(ovals)
			if (myRoute) {
				for (int x = 1; x < routeMyOvals.size(); x++) {
					Line tempLine;
					tempLine = new Line(routeMyOvals.get(x - 1).getPoint(), routeMyOvals.get(x).getPoint());
					routeMyLines.add(tempLine);
				}
			} else {
				for (int x = 1; x < routeOvals.size(); x++) {
					Line tempLine;
					tempLine = new Line(routeOvals.get(x - 1).getPoint(), routeOvals.get(x).getPoint());
					routeLines.add(tempLine);
				}
			}
			if (myRoute)
				drawMyRoute = true;
			else
				drawBestRoute = true; // draw in paintComponents()
		}
	}

	/*
	 * DOUBLE BUFFERING import java.awt.Graphics; import java.awt.Image;
	 * paintComponent(Graphics g), paint(Graphics g) constantly copies gui image,
	 * inorder to replicate and reprint it constantly to Map
	 */
	private Image dbImage;
	private Graphics dbg;

	public void paint(Graphics g) {
		dbImage = createImage(getWidth(), getHeight());
		dbg = dbImage.getGraphics();// screenshot gui into dbImage
		paintComponent(dbg);
		g.drawImage(dbImage, 0, 0, this);
	}

	// Listeners
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3) {
			Point mousePoint = e.getPoint(); // get mousePoint

			// adjust mousePoint to shift up-left on Map,
			// so that the circle is center on the sharp point of the mouse
			mousePoint.x = mousePoint.x - STATION_DIAMETER / 2;
			mousePoint.y = mousePoint.y - STATION_DIAMETER / 2;
			// makes sure it's not clicked on an existing station
			if (!clickedOnStation(e.getPoint())) {
				Oval tempOval = new Oval(mousePoint, STATION_DIAMETER, true);
				ovals.add(tempOval);
				String stationName = JOptionPane.showInputDialog("Enter in station name");
				if (stationName != null) {
					tempOval.setName(stationName);
					gos.addStation(new Station(stationName, tempOval.getPoint())); // update GraphOfStations
					pl = gos.simulatePlacements(); // update simulation
				} else
					ovals.remove(tempOval); // remove station, because invalid station name
			}
		}
	}

	/*
	 * if clicking on a station then lineTemp is not null else it is null
	 * 
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			drawingRoute = true; // used to draw the red Circle
			routeStart = new Oval(e.getX(), e.getY(), ROUTE_START_DIAMETER); // the red circle
			if (clickedOnStation(e.getPoint())) { // if clicked on a already present station
				ovalTemp = getClickedStation(e.getPoint());
				lineTemp = new Line();
				lineTemp.setStart(e.getPoint());
			} else {
				ovalTemp = null;
				lineTemp = null;
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			drawingRoute = false; // stops drawing the red circle
			Oval startOval = ovalTemp; // either an Oval or null
			if (clickedOnStation(e.getPoint())) { // clicked on oval
				Oval endOval = getClickedStation(e.getPoint());// get which oval was clicked on
				if (!sameStation(endOval)) { // make sure the user actually drew a route
					if (lineTemp != null) { // if lineTemp==null then the first click was not on a station

						Station startStation = null, endStation = null;
						// double checks GraphOfStation, to make sure a Station with the same points as
						// the oval exists in the stationList. note: stations should exist
						if (gos.hasStationByPoint(startOval.getPoint())) {
							startStation = gos.getStationByPoint(startOval.getPoint());
						}
						if (gos.hasStationByPoint(endOval.getPoint())) {
							endStation = gos.getStationByPoint(endOval.getPoint());
						}
						// if all goes well, then add the edge to GOS, and to Map
						if (startStation != null && endStation != null) {
							gos.addEdge(startStation, endStation);
							lineTemp.setEnd(e.getPoint());
							lines.add(lineTemp);
						}
					}
				}
			}
		}
	}

	// Hovering over an oval displays some of its information on Map
	@Override
	public void mouseMoved(MouseEvent e) {
		Point cursor = e.getPoint();
		if (clickedOnStation(cursor)) {
			hoveredOval = getClickedStation(cursor);
			hoveringOval = true;
		} else
			hoveringOval = false;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// not used
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// not used
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// not used
	}

	// Visual Graphic Classes, oval = stations, line = edges/routes
	//
	private class Oval { // represents stations
		private int x, y;
		private int d;
		private String name;

		// constructor without -d/2, used in updateMap
		public Oval(Point location, int d, boolean blank) {
			this.x = location.x;
			this.y = location.y;
			this.d = d;
			this.name = "";
		}

		public Oval(Point location, int d, String name, boolean blank) {
			this.x = location.x;
			this.y = location.y;
			this.d = d;
			this.name = name;
		}

		public Oval(int x, int y, int d) {
			this.x = x - d / 2;
			this.y = y - d / 2;
			this.d = d;
		}

		public String getName() {
			return this.name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Point getPoint() {
			return new Point(this.x, this.y);
		}

		public int getXMin() {
			return x;
		}

		public int getXMax() {
			return x + d;
		}

		public int getYMin() {
			return y;
		}

		public int getYMax() {
			return y + d;
		}

		public int getX() {
			return this.x;
		}

		public int getY() {
			return this.y;
		}

		public int getD() {
			return this.d;
		}
	}

	// represents the edges from GOS
	private class Line { // individual route
		private Point start;
		private Point end;

		public Line() {
			this.start = null;
			this.end = null;
		}

		public Line(Point start, Point end) {
			setStartWithUpdate(start);
			setEndWithUpdate(end);
		}

		public Point getStart() {
			return this.start;
		}

		public Point getEnd() {
			return this.end;
		}

		public void setStart(Point start) {
			this.start = start;
		}

		// sets startPoint, and adjusts it to be centered in the oval
		public void setStartWithUpdate(Point start) {
			Point newStart = new Point(start);
			newStart.x += STATION_DIAMETER / 2;
			newStart.y += STATION_DIAMETER / 2;
			this.start = newStart;
		}

		public void setEnd(Point end) {
			this.end = end;
		}

		// sets endPoint, and adjusts it to be centered in the oval
		public void setEndWithUpdate(Point end) {
			Point newEnd = new Point(end);
			newEnd.x += STATION_DIAMETER / 2;
			newEnd.y += STATION_DIAMETER / 2;
			this.end = newEnd;
		}
	}
}