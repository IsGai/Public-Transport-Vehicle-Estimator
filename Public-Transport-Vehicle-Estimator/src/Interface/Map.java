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
import java.util.ArrayList;
import java.util.EmptyStackException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import collections.GraphOfStations;
import planner.Route;
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
	//test variables
	private boolean drawBestRoute = false;
	private ArrayList<Oval> routeOvals = new ArrayList<Oval>();
	private ArrayList<Line> routeLines = new ArrayList<Line>();

	public Map(boolean restricted, GraphOfStations gos) {
		this.setBackground(Color.white);
		this.setForeground(null);
		this.gos = gos;
		this.updateMap();
		this.setPreferredSize(new Dimension(1000,1000));

		if (!restricted)// userScreen won't be able to edit
			this.addMouseListener(this);
	}

	@Override
	protected void paintComponent(Graphics g2) {
		super.paintComponent(g2);
		Graphics2D g = (Graphics2D) g2;

		g.setColor(Color.black);
		for (Oval o : ovals) // constantly draws stations
			if (o != null)
				g.fillOval(o.getX(), o.getY(), o.getD(), o.getD());
		for (Line l : lines) // constantly draw routes
			if (l != null) {
				g.setStroke(new BasicStroke(5));
				g.drawLine(l.getStart().x, l.getStart().y, l.getEnd().x, l.getEnd().y);
			}
		if (drawingRoute) {// draws red circle
			g.setColor(Color.red);
			g.fillOval(routeStart.getX(), routeStart.getY(), routeStart.getD(), routeStart.getD());
		} else
			g.setColor(Color.black);
		if(drawBestRoute) {
			//System.out.println("drawBestRoute");
			g.setColor(Color.green);
			for(Oval ro: routeOvals) {
				if(ro!=null) {
					//System.out.println(ro.getX() +","+ ro.getY()+","+ ro.getD()+","+ ro.getD());
					g.fillOval(ro.getX(), ro.getY(), ro.getD(), ro.getD());
				}
			}
			for (Line rl: routeLines) // constantly draw routes
				if (rl != null) {
					g.setStroke(new BasicStroke(5));
					g.drawLine(rl.getStart().x, rl.getStart().y, rl.getEnd().x, rl.getEnd().y);
				}
			g.setColor(Color.black);
		}
		for (Oval o : ovals) // constantly draws ovalNames
			if (o != null) {
				g.setColor(Color.blue);
				g.setFont(new Font("Arial", Font.BOLD, 15));
				g.drawString(o.getName(), o.getX(), o.getY());
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

	public void updateMap() {
		
		// add new stations to ovals
		ArrayList<Station> stationList = gos.getStationList();
		ArrayList<Oval> newOvals = new ArrayList<Oval>();
		for (int x = 0; x < stationList.size(); x++)
			if (stationList.get(x) != null)
				newOvals.add(new Oval(stationList.get(x).getVertexCoordinate(), STATION_DIAMETER,
						stationList.get(x).getName(), true));
		ovals = newOvals;
		
		// add new edges to lines
		ArrayList<String> stationEdges = gos.getStationEdges();
		ArrayList<Line> newLines = new ArrayList<Line>();// newLines is used to get rid of duplicated drawn lines
		for (int x = 0; x < stationEdges.size(); x++)
			newLines.add(new Line(gos.getStationByName(stationEdges.get(x).split("asdflmfao")[0]).getVertexCoordinate(),
					gos.getStationByName(stationEdges.get(x).split("asdflmfao")[1]).getVertexCoordinate()));
		this.lines = newLines;
	}
	
	public void updateMap(Station departureStation, Station destinationStation) {
		//Clean this method up
		Route bestPath = gos.bestPath(departureStation.getStationId(), destinationStation.getStationId()).copy();
		Station temp = null;
		routeOvals = new ArrayList<Oval>();
		routeLines = new ArrayList<Line>();
		try {
			while(true) {
				temp = bestPath.pop();
				routeOvals.add(new Oval(temp.getVertexCoordinate(), STATION_DIAMETER,
						temp.getName(), true));
			}
		}catch(EmptyStackException e) {
			for(int x=1;x<routeOvals.size();x++) {
				Line tempLine;
				tempLine = new Line(routeOvals.get(x-1).getPoint(), routeOvals.get(x).getPoint());
				routeLines.add(tempLine);
			}
			drawBestRoute = true;
		}
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
			Oval tempOval = new Oval(new Point(e.getX(), e.getY()), STATION_DIAMETER, true);
			ovals.add(tempOval);
			String stationName = JOptionPane.showInputDialog("Enter in station name");
			tempOval.setName(stationName);
			gos.addStation(new Station(stationName, tempOval.getPoint())); // update GraphOfStations
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
			if (clickedOnStation(e.getPoint())) { // clciked on oval
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

		// fixed: routes shifting to bottom right
		// was referencing Station.coordinates
		public void setStartWithUpdate(Point start) {
			Point newStart = new Point(start);
			newStart.x += STATION_DIAMETER / 2;
			newStart.y += STATION_DIAMETER / 2;
			this.start = newStart;
		}

		public void setEnd(Point end) {
			this.end = end;
		}

		public void setEndWithUpdate(Point end) {
			Point newEnd = new Point(end);
			newEnd.x += STATION_DIAMETER / 2;
			newEnd.y += STATION_DIAMETER / 2;
			this.end = newEnd;
		}
	}
}