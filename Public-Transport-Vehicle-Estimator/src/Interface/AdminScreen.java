package Interface;

import collections.GraphOfStations;
import planner.Station;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class AdminScreen extends JFrame implements ActionListener {

	// GUI related
	static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
	private Dimension guiSize = new Dimension();
	private Point guiLocation = new Point();

	// leftPanel
	private JPanel leftPanel = new JPanel();
	private JPanel passengerPanel = new JPanel();
	private JButton addPassengerButton = new JButton("Add Passenger");
	private JButton removePassengerButton = new JButton("Remove Passenger");
	private JPanel stationPanel = new JPanel();
	private JButton addStationButton = new JButton("Add Station");
	private JButton removeStationButton = new JButton("Remove Station");
	private JPanel routePanel = new JPanel();
	private JButton addRouteButton = new JButton("Add Route ");
	private JButton removeRouteButton = new JButton("Remove Route ");
	private JButton saveMapButton = new JButton("Save Map");
	private JButton loadMapButton = new JButton("Load Map");
	private JButton clearMapButton = new JButton("Clear Map");
	private JButton showAllPassengersButton = new JButton("Show Passengers");
	private JButton backButton = new JButton("Return to Login Screen");
	// addbuttons to show all passengers
	// do everything else userscreen can do

	// rightPanel
	private JPanel rightPanel = new JPanel();
	private GraphOfStations gos = null;
	private Map map = null;

	public AdminScreen(GraphOfStations gos) {
		this.setTitle("Admin Screen");
		this.setLayout(new GridLayout(1, 2));

		guiSize.width = (int) (SCREEN_SIZE.width / 1.7);
		guiSize.height = (int) (SCREEN_SIZE.height / 1.2);
		this.setSize(guiSize.width, guiSize.height);
		guiLocation.x = SCREEN_SIZE.width / 2 - guiSize.width / 2;
		guiLocation.y = SCREEN_SIZE.height / 2 - guiSize.height / 2;
		this.setLocation(guiLocation.x, guiLocation.y);

		initialSetup(gos);
		leftPanel();
		rightPanel();
		adminScreen();

		this.setVisible(true);
		// this.setResizable(false); //addScrollBars() to map?
		this.toFront();
		this.requestFocus();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void initialSetup(GraphOfStations gos) {
		this.gos = gos;
		map = new Map(false, gos);
		//BUTTONS YET TO BE IMPLEMENTED
		removeStationButton.setEnabled(false);
		addPassengerButton.setEnabled(false);
		removePassengerButton.setEnabled(false);
		removeRouteButton.setEnabled(false);
		showAllPassengersButton.setEnabled(false);
	}

	public JPanel centerToGrid(Component c) {
		JPanel temp = new JPanel();
		temp.add(c);
		return temp;
	}

	public void leftPanel() {
		passengerPanel();
		stationPanel();
		routePanel();
		leftPanel.setLayout(new BorderLayout());

		JPanel tempPanel = new JPanel();
		tempPanel.setLayout(new BoxLayout(tempPanel, BoxLayout.Y_AXIS));
		tempPanel.add(passengerPanel);
		tempPanel.add(stationPanel);
		tempPanel.add(routePanel);
		
		JPanel saveMapPanel = new JPanel(new GridLayout(1, 2));
		saveMapPanel.add(centerToGrid(saveMapButton));
		saveMapPanel.add(centerToGrid(loadMapButton));
		tempPanel.add(saveMapPanel);
		tempPanel.add(clearMapButton);
		tempPanel.add(showAllPassengersButton);

		leftPanel.add(tempPanel, BorderLayout.NORTH);
		leftPanel.add(backButton, BorderLayout.SOUTH);
		backButton.addActionListener(this);
		saveMapButton.addActionListener(this);
		loadMapButton.addActionListener(this);
		clearMapButton.addActionListener(this);
		showAllPassengersButton.addActionListener(this);
	}

	public void passengerPanel() {
		addPassengerButton.addActionListener(this);
		removePassengerButton.addActionListener(this);
		passengerPanel.setLayout(new GridLayout(1, 2));
		passengerPanel.add(centerToGrid(addPassengerButton));
		passengerPanel.add(centerToGrid(removePassengerButton));
	}

	public void stationPanel() {
		addStationButton.addActionListener(this);
		removeStationButton.addActionListener(this);
		stationPanel.setLayout(new GridLayout(1, 2));
		stationPanel.add(centerToGrid(addStationButton));
		stationPanel.add(centerToGrid(removeStationButton));
	}

	public void routePanel() {
		addRouteButton.addActionListener(this);
		removeRouteButton.addActionListener(this);
		routePanel.setLayout(new GridLayout(1, 2));
		routePanel.add(centerToGrid(addRouteButton));
		routePanel.add(centerToGrid(removeRouteButton));
	}

	public void rightPanel() {
		rightPanel.setLayout(new BorderLayout());
		addScrollPane(rightPanel, map, "Map (Default: 1000x1000)");
		// rightPanel.add(scrollp);
	}

	public void addScrollPane(JPanel panel, JPanel panel2, String title) {
		JScrollPane temp = new JScrollPane(panel2, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		panel.setBorder(BorderFactory.createTitledBorder(title));
		panel.setBackground(null);
		panel.add(temp);
	}

	public void adminScreen() {
		this.add(leftPanel);
		this.add(rightPanel);
	}

	public String getInput(String message) {
		return JOptionPane.showInputDialog(message);
	}

	public int getCoordInput(String message) {
		int coordinate;
		try {
			coordinate = Integer.parseInt(JOptionPane.showInputDialog(message));
			return coordinate;
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	public void outputMessage(String message, String title, int messageType) {
		JOptionPane.showMessageDialog(this, message, title, messageType);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton buttonClicked = (JButton) e.getSource();
		if (buttonClicked == addPassengerButton) {
			outputMessage("Not yet implemented", "Not yet implemented", JOptionPane.PLAIN_MESSAGE);
		}
		if (buttonClicked == removePassengerButton) {
			outputMessage("Not yet implemented", "Not yet implemented", JOptionPane.PLAIN_MESSAGE);
		}
		if (buttonClicked == addStationButton) {
			int stationX = -1, stationY = -1;
			do {
				stationX = getCoordInput("Enter in X coordinate for station");
			} while (stationX == -1);
			do {
				stationY = getCoordInput("Enter in Y coordinate for station");
			} while (stationY == -1);
			String stationN = getInput("Enter in NAME for station");
			// add input validation for stationX/stationY
			Station s = new Station(stationN, new Point(stationX, stationY));
			gos.addStation(s);
			map.updateMap();
		}
		if (buttonClicked == removeStationButton) {
			// does not work yet
			outputMessage("Does not work yet, may conflict with\nhow collections are programed", "Not yet implemented",
					JOptionPane.PLAIN_MESSAGE);
		}
		if (buttonClicked == addRouteButton) {
			String station1 = getInput("Enter in name of station one");
			String station2 = getInput("Enter in name of station two");
			if (!gos.hasStationByName(station1))
				outputMessage("Cannot find station: " + station1, "Station Not Found", JOptionPane.ERROR_MESSAGE);
			if (!gos.hasStationByName(station2))
				outputMessage("Cannot find station: " + station2, "Station Not Found", JOptionPane.ERROR_MESSAGE);
			if (gos.hasStationByName(station1) && gos.hasStationByName(station2)) {
				Station startStation = gos.getStationByName(station1);
				Station endStation = gos.getStationByName(station2);
				gos.addEdge(startStation, endStation);
				map.updateMap();
			}
		}
		if (buttonClicked == removeRouteButton) {
			outputMessage("Not yet implemented", "Not yet implemented", JOptionPane.PLAIN_MESSAGE);
		}
		if (buttonClicked == saveMapButton) {
			int choice = JOptionPane.showConfirmDialog(this, "Save to default(Default.map)?",
					"Save to default(Map.dat)?", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			if (choice == JOptionPane.YES_OPTION) {
				gos.saveGos("Default");
			}
			if (choice == JOptionPane.NO_OPTION) {
				String fileName = JOptionPane.showInputDialog(this, "Enter in name to save file as.", "Save File As",
						JOptionPane.PLAIN_MESSAGE);
				gos.saveGos(fileName);
			}
			if (choice == JOptionPane.CANCEL_OPTION) {

			}
		}
		if (buttonClicked == loadMapButton) {
			JFileChooser jfl = new JFileChooser("src/Data");
			jfl.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int option = jfl.showOpenDialog(this); // open JFileChooser
			if (option == JFileChooser.APPROVE_OPTION) {
				if (gos.loadGOS(jfl.getSelectedFile().getPath())) {
					map.updateMap();
				} else {
					outputMessage("Could not load file.\nPlease make sure you selected a \".map\" file.",
							"Could Not Load File", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		if (buttonClicked == clearMapButton) {
			int choice = JOptionPane.showConfirmDialog(this,
					"Click \"Yes\" will reset all unsaved map and passenger data.", "Are you sure?",
					JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
			if (choice == JOptionPane.YES_OPTION) {
				gos.clearGOS();
				map.updateMap();
			}
			if (choice == JOptionPane.NO_OPTION) {

			}
		}
		if (buttonClicked == backButton) {
			new BusPlannerGUI();
			this.dispose();
		}
	}
}
