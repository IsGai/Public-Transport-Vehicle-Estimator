package Interface;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import collections.GraphOfStations;
import planner.Station;

public class Map extends JPanel implements MouseListener {
	private Line lineTemp;
	private Oval ovalTemp;
	private ArrayList<Oval> ovals = new ArrayList<Oval>();
	private ArrayList<Line> lines = new ArrayList<Line>();
	private Oval routeStart;
	private boolean drawingRoute = false;
	private static int STATION_DIAMETER = 25;
	private static int ROUTE_START_DIAMETER = 15;
	private GraphOfStations gos;

	public Map(boolean restricted, GraphOfStations gos) {
		this.setBackground(Color.white);
		this.setForeground(null);
		this.gos = gos;

		if (!restricted)// userScreen won't be able to edit
			this.addMouseListener(this);
	}

	@Override
	protected void paintComponent(Graphics g2) {
		super.paintComponent(g2);
		Graphics2D g = (Graphics2D) g2;

		g.setColor(Color.black);
		for (Oval o : ovals) {// constantly draws stations
			g.fillOval(o.getX(), o.getY(), o.getD(), o.getD());
		}
		for (Line l : lines) {// constantly draw routes
			g.setStroke(new BasicStroke(5));
			g.drawLine(l.getStart().x, l.getStart().y, l.getEnd().x, l.getEnd().y);
		}
		if (drawingRoute) {// draws red circle
			g.setColor(Color.red);
			g.fillOval(routeStart.getX(), routeStart.getY(), routeStart.getD(), routeStart.getD());
		} else {
			g.setColor(Color.black);
		}

		repaint();
	}

	public boolean sameStation(Oval temp) {
		if (ovalTemp == temp)
			return true;
		else
			return false;
	}

	public Oval getClickedStation(Point p) {
		for (Oval o : ovals)
			if (p.x >= o.getXMin() && p.x <= o.getXMax() && p.y >= o.getYMin() && p.y <= o.getYMax())
				return o;
		return null;
	}

	public boolean clickedOnStation(Point p) {
		for (Oval o : ovals)
			if (p.x >= o.getXMin() && p.x <= o.getXMax() && p.y >= o.getYMin() && p.y <= o.getYMax())
				return true;
		return false;
	}

	public void updateMap() { // works when using buttons to add stations/routes
		ArrayList<Point> temp = gos.getStationsCoords();
		ArrayList<Oval> ovals = new ArrayList<Oval>();
		for (int x = 0; x < temp.size(); x++) {
			if (temp.get(x) != null) {
				ovals.add(x, new Oval(temp.get(x), STATION_DIAMETER, true));
			}
		}
		this.ovals = ovals;// overwrite old ovals list

		//not accurate, do get stationLinked, then find the corresponding oval, and link ovals
		//instead of trying to link stations
		ArrayList<Line> lines = new ArrayList<Line>();
		ArrayList<ArrayList<Station>> stationEdges = gos.getStationEdges();
		for (int x = 0; x < stationEdges.size(); x++) {
			if (stationEdges.get(x) != null) {
				for (int y = 0; y < stationEdges.get(x).size(); y++) {
					if (stationEdges.get(x).get(y) != null && x != y) {
						Point startPoint = stationEdges.get(x).get(x).getVertexCoordinate();
						Point endPoint = stationEdges.get(x).get(y).getVertexCoordinate();
						Line tempLine = new Line();
						tempLine.setStart(startPoint); //startPoint don't need update?
						tempLine.setEndWithUpdate(endPoint); //but endPoint doe?
						System.out.println(x + ":" + x + "]" + stationEdges.get(x).get(x).getVertexCoordinate());
						System.out.println(x + ":" + y + "]" + stationEdges.get(x).get(y).getVertexCoordinate());
						lines.add(x, tempLine);
					}
				}
			}
		}
		this.lines = lines; // overwrite old lines list
	}

	/*
	 * DOUBLE BUFFERING import java.awt.Graphics; import java.awt.Image;
	 * paintComponent(Graphics g), paint(Graphics g) constantly copies gui image,in
	 * order to replicate and reprint it
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
			Oval tempOval = new Oval(e.getX(), e.getY(), STATION_DIAMETER);
			ovals.add(tempOval);
			String stationName = JOptionPane.showInputDialog("Enter in station name");
			gos.addStation(new Station(stationName, tempOval.getPoint())); // update GraphOfStations
			//System.out.println(stationName + ": " + tempOval.getPoint());
			//System.out.println(new Point(e.getX(), e.getY()));
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			drawingRoute = true;
			routeStart = new Oval(e.getX(), e.getY(), 15);
			if (clickedOnStation(e.getPoint())) {
				ovalTemp = getClickedStation(e.getPoint());
				lineTemp = new Line();
				lineTemp.setStart(e.getPoint());
			} else {
				lineTemp = null;
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			drawingRoute = false;
			Oval startOval = ovalTemp;
			Oval endOval = getClickedStation(e.getPoint());
			if (clickedOnStation(e.getPoint()) && !sameStation(endOval)) {
				if (lineTemp != null) {
					lineTemp.setEnd(e.getPoint());
					lines.add(lineTemp);

					Station startStation = null, endStation = null;
					if (gos.hasStationByPoint(startOval.getPoint())) {
						startStation = gos.getStationByPoint(startOval.getPoint());
						// System.out.println("hasStation: " + startStation.getName());
					}
					if (gos.hasStationByPoint(endOval.getPoint())) {
						endStation = gos.getStationByPoint(endOval.getPoint());
						// System.out.println("hasStation: " + endStation.getName());
					}
					gos.addEdge(startStation, endStation);
					// System.out.println(gos.getWeight(startStation, endStation));
				}
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	// Visual Graphic Classes, oval = stations, line = edges/routes
	private class Oval { // stations
		private int x, y;
		private int d;

		// constructor with "mouse point" centering
		public Oval(Point location, int d) {
			this.x = location.x - d / 2;
			this.y = location.y - d / 2;
			this.d = d;
		}

		// constructor without -d/2, used in updateMap
		public Oval(Point location, int d, boolean blank) {
			this.x = location.x;
			this.y = location.y;
			this.d = d;
		}

		public Oval(int x, int y, int d) {
			this.x = x - d / 2;
			this.y = y - d / 2;
			this.d = d;
		}

		public Point getPoint() {
			return new Point(this.x, this.y);
		}

		public int getXMin() {
			return x;
		}

		public int getXMax() {
			return x + d - 8;
		}

		public int getYMin() {
			return y;
		}

		public int getYMax() {
			return y + d - 8;
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

	private class Line { // individual route
		private Point start;
		private Point end;

		public Line() {
			this.start = null;
			this.end = null;
		}

		public Line(Point start, Point end) {
			this.start = start;
			this.end = end;
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

		public void setStartWithUpdate(Point start) {
			start.x += STATION_DIAMETER/2;
			start.y += STATION_DIAMETER/2;
			this.start = start;
		}

		public void setEnd(Point end) {
			this.end = end;
		}

		public void setEndWithUpdate(Point end) {
			end.x += STATION_DIAMETER/2;
			end.y += STATION_DIAMETER/2;
			this.end = end;
		}
	}
}