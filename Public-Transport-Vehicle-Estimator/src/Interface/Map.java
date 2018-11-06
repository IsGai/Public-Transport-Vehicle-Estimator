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

import javax.swing.JPanel;

public class Map extends JPanel implements MouseListener {
	private Line lineTemp;
	private Oval ovalTemp;
	private ArrayList<Oval> ovals = new ArrayList<Oval>();
	private ArrayList<Line> lines = new ArrayList<Line>();
	private Oval routeStart;
	private boolean drawingRoute = false;

	public Map(boolean restricted) {
		this.setBackground(Color.white);
		this.setForeground(null);

		if (!restricted)//userScreen won't be able to edit
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
		for (Oval o : ovals) {
			if (p.x >= o.getXMin() && p.x <= o.getXMax() && p.y >= o.getYMin() && p.y <= o.getYMax()) {
				return o;
			}
		}
		return null;
	}

	public boolean clickedOnStation(Point p) {
		for (Oval o : ovals) {
			if (p.x >= o.getXMin() && p.x <= o.getXMax() && p.y >= o.getYMin() && p.y <= o.getYMax()) {
				return true;
			}
		}
		return false;
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

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3) {
			ovals.add(new Oval(e.getX() - 25 / 2, e.getY() - 25 / 2, 25));
			// System.out.println((e.getX() - 25 / 2) + ", " + (e.getY() - 25 / 2));
			// System.out.println(ovals.get(0).getXMin() + ": " + ovals.get(0).getXMax());
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			drawingRoute = true;
			routeStart = new Oval(e.getX() - 15 / 2, e.getY() - 15 / 2, 15);
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
			// routeEnd = new Oval(e.getX() - 25 / 2, e.getY() - 25 / 2, 25);
			if (clickedOnStation(e.getPoint()) && !sameStation(getClickedStation(e.getPoint()))) {
				if (lineTemp != null) {
					lineTemp.setEnd(e.getPoint());
					lines.add(lineTemp);//
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

	private class Oval { // stations
		private int x, y;
		private int d;

		public Oval(Point location, int d) {
			this.x = location.x;
			this.y = location.y;
			this.d = d;
		}

		public Oval(int x, int y, int d) {
			this.x = x;
			this.y = y;
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

		public void setEnd(Point end) {
			this.end = end;
		}
	}
}