package Interface;

import collections.GraphOfStations;
import collections.Passengers;
import planner.Passenger;
import planner.Station;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class AdminScreen extends JFrame implements ActionListener, ChangeListener {

	// GUI related
	static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
	private Dimension guiSize = new Dimension();
	private Point guiLocation = new Point();

	// leftPanel
	private JPanel leftPanel = new JPanel();
	private JPanel passengerPanel = new JPanel();
	private JButton addPassengerButton = new JButton("Add Passenger");
	private JButton removePassengerButton = new JButton("Remove Passenger");
	private JPanel stationRoutePanel = new JPanel();
	private JButton addStationButton = new JButton("Add Station");
	private JButton addRouteButton = new JButton("Add Route ");

	private JButton saveMapButton = new JButton("Save Map");
	private JButton loadMapButton = new JButton("Load Map");
	private JButton clearMapButton = new JButton("Clear Map");

	private JPanel passengersPanel = new JPanel();
	private JComboBox<String> passengersComboBox = new JComboBox<String>();// change <String> to <Route>
	private JLabel passengersLabel = new JLabel("Passengers:");
	
	//time
	private JPanel timePanel = new JPanel();
	private JSlider timeSlider = new JSlider(0,1439,1);
	private JLabel timeLabel = new JLabel("Time: ");
	private JLabel peopleOnBuses = new JLabel("People on Buses: ");

	private JButton backButton = new JButton("Return to Login Screen");
	// addbuttons to show all passengers
	// do everything else userscreen can do

	// rightPanel
	private JPanel rightPanel = new JPanel();
	private GraphOfStations gos = null;
	private Map map = null;
	private String mapFileName;
	private TitledBorder titledBorder;
	private int[][] passengerLocations;

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
		mapFileName = gos.getFileName();
		passengerLocations = gos.simulatePlacements();
		map = new Map(false, gos, timeSlider, passengerLocations);
		timeSlider.addChangeListener(this);

		// BUTTONS YET TO BE IMPLEMENTED

	}

	public JPanel centerToGrid(Component c) {
		JPanel temp = new JPanel();
		temp.add(c);
		return temp;
	}
	
	public void timePanel() {
		timePanel.add(timeSlider);
		timePanel.add(timeLabel);
	}

	public void leftPanel() {
		passengerPanel();
		stationRoutePanel();
		passengersPanel();
		timePanel();
		leftPanel.setLayout(new BorderLayout());

		JPanel tempPanel = new JPanel();
		tempPanel.setLayout(new BoxLayout(tempPanel, BoxLayout.Y_AXIS));
		tempPanel.add(passengerPanel);
		tempPanel.add(stationRoutePanel);

		JPanel saveMapPanel = new JPanel(new GridLayout(1, 2));
		saveMapPanel.add(centerToGrid(saveMapButton));
		saveMapPanel.add(centerToGrid(loadMapButton));
		tempPanel.add(saveMapPanel);
		tempPanel.add(clearMapButton);
		tempPanel.add(passengersPanel);
		tempPanel.add(timePanel);
		tempPanel.add(peopleOnBuses);

		leftPanel.add(tempPanel, BorderLayout.NORTH);
		leftPanel.add(backButton, BorderLayout.SOUTH);
		backButton.addActionListener(this);
		saveMapButton.addActionListener(this);
		loadMapButton.addActionListener(this);
		clearMapButton.addActionListener(this);
	}

	public void passengersComboBox() {
		Passengers<Passenger> passengerList = gos.getPassengers();
		String[] passengerNames = new String[passengerList.size()];

		for (int x = 0; x < passengerList.size(); x++)
			if (passengerList.get(x) != null)
				passengerNames[x] = "[" + passengerList.get(x).getId() + "] " + passengerList.get(x).getName();
		for (int x = 0; x < passengerNames.length; x++)
			for (int y = 0; y < passengerNames.length - 1; y++)
				if (passengerNames[y].compareTo(passengerNames[y + 1]) > 0) {
					String temp = passengerNames[y];
					passengerNames[y] = passengerNames[y + 1];
					passengerNames[y + 1] = temp;
				}

		passengersComboBox.setModel(new DefaultComboBoxModel(passengerNames));
	}

	public void passengersPanel() {
		passengersComboBox();
		passengersComboBox.addActionListener(this);
		passengersComboBox.setBackground(Color.white);
		passengersPanel.add(passengersLabel);
		passengersPanel.add(passengersComboBox);
	}

	public void passengerPanel() {
		addPassengerButton.addActionListener(this);
		removePassengerButton.addActionListener(this);
		passengerPanel.setLayout(new GridLayout(1, 2));
		passengerPanel.add(centerToGrid(addPassengerButton));
		passengerPanel.add(centerToGrid(removePassengerButton));
	}

	public void stationRoutePanel() {
		addStationButton.addActionListener(this);
		stationRoutePanel.setLayout(new GridLayout(1, 2));
		stationRoutePanel.add(centerToGrid(addStationButton));
		stationRoutePanel.add(centerToGrid(addRouteButton));
	}

	public void rightPanel() {
		rightPanel.setLayout(new BorderLayout());
		addScrollPane(rightPanel, map, "" + mapFileName + "");
	}

	public void changeMapTitle(String mapFileName) {
		titledBorder.setTitle(mapFileName);
		this.repaint();
	}

	public void addScrollPane(JPanel panel, JPanel panel2, String title) {
		JScrollPane temp = new JScrollPane(panel2, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		titledBorder = BorderFactory.createTitledBorder(title);
		panel.setBorder(titledBorder);
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

	public void changeMapFileName(String mapFileName) {
		this.mapFileName = mapFileName;
	}

	public String getMapFileName() {
		return this.mapFileName;
	}
	
	public int getPassengerCount(int StationId){
		int time = timeSlider.getValue();
		return this.passengerLocations[StationId][time];
	}
	
	public void setTime(int time) {
		int hours = time/60; 
		int minutes = time%60;
		String actualTime = String.format("%d:%02d", hours, minutes);
		timeLabel.setText("Time: " + actualTime);
	}
	
	public void setPeopleOnBuses(int people) {
		peopleOnBuses.setText("People on Buses: " + people);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addPassengerButton) {
			String name = JOptionPane.showInputDialog("Enter in your name.");
			Passenger p = new Passenger(name);
			gos.getPassengers().add(p);
			outputMessage("New passenger " + p.getName() + " was succesfully added.\n" + p.getName() + "'s ID is \""
					+ p.getId() + "\"", "Passenger Added", JOptionPane.PLAIN_MESSAGE);
			gos.getPassengers().exportPassengers(gos.getFileName());
			passengersComboBox(); // update passenger combo box
		}
		if (e.getSource() == removePassengerButton) {
			String id = JOptionPane.showInputDialog("Verify ID.");
			String name = JOptionPane.showInputDialog("Verify name.");
			try {
				boolean removed = gos.removePassenger(Integer.parseInt(id), name);
				if (removed) {
					outputMessage("Passenger[ID:" + id + ", Name:" + name + "] was successfully removed",
							"Passenger Removed", JOptionPane.PLAIN_MESSAGE);
					passengersComboBox(); // update passenger combo box
					map.removeMyRoute();
				} else
					outputMessage("Passenger[ID:" + id + ", Name:" + name
							+ "] could not be found.\nPlease make sure you've entered in the correct information.",
							"Passenger Not Found", JOptionPane.PLAIN_MESSAGE);
			} catch (NumberFormatException e2) {
				outputMessage("Please enter in a number. ie: \"1000\".", "Invalid ID Format",
						JOptionPane.ERROR_MESSAGE);
			}
		}
		if (e.getSource() == addStationButton) {
			int stationX = -1, stationY = -1;
			do {
				stationX = getCoordInput("Enter in X coordinate for station");
			} while (stationX == -1);
			do {
				stationY = getCoordInput("Enter in Y coordinate for station");
			} while (stationY == -1);
			String stationN = getInput("Enter in NAME for station");
			// add input validation for stationX/stationY
			Point stationCoord = new Point(stationX, stationY);
			if (map.clickedOnStation(stationCoord)) {
				outputMessage("There is already a station there!", "Error Message", JOptionPane.ERROR_MESSAGE);
			} else {
				Station s = new Station(stationN, new Point(stationX, stationY));
				gos.addStation(s);
				passengerLocations = gos.simulatePlacements();
				map.updateMap();
			}
		}
		if (e.getSource() == addRouteButton) {
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
		if (e.getSource() == saveMapButton) {
			int choice = JOptionPane.showConfirmDialog(this, "Save to default(Default.map)?",
					"Save to default(Default.map)?", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			if (choice == JOptionPane.YES_OPTION) {
				gos.saveGos("Default");
				gos.getPassengers().exportPassengers("Default");
			}
			if (choice == JOptionPane.NO_OPTION) {
				String fileName = JOptionPane.showInputDialog(this, "Enter in name to save file as.", "Save File As",
						JOptionPane.PLAIN_MESSAGE);
				gos.saveGos(fileName);
				gos.getPassengers().exportPassengers(fileName);
			}
			if (choice == JOptionPane.CANCEL_OPTION) {

			}
		}
		if (e.getSource() == loadMapButton) {
			JFileChooser jfl = new JFileChooser("Data");
			jfl.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int option = jfl.showOpenDialog(this); // open JFileChooser
			if (option == JFileChooser.APPROVE_OPTION) {
				if (gos.loadGOS(jfl.getSelectedFile().getPath())) {
					mapFileName = jfl.getSelectedFile().getName().substring(0,
							jfl.getSelectedFile().getName().length() - 4);
					String passengerFilePath = jfl.getSelectedFile().getPath().toString().substring(0,
							jfl.getSelectedFile().getPath().toString().length() - 4) + ".pas";
					gos.getPassengers().importPassengers(passengerFilePath);
					changeMapTitle(mapFileName);
					gos.setFileName(mapFileName);
					passengersComboBox(); // update passenger combo box
					map.updateMap();
				} else {
					outputMessage("Could not load file.\nPlease make sure you selected a \".map\" file.",
							"Could Not Load File", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		if (e.getSource() == clearMapButton) {
			int choice = JOptionPane.showConfirmDialog(this,
					"Click \"Yes\" will reset all unsaved map and passenger data.", "Are you sure?",
					JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
			if (choice == JOptionPane.YES_OPTION) {
				gos.clearGOS();
				map.updateMap();
				passengersComboBox();
			}
			if (choice == JOptionPane.NO_OPTION) {

			}
		}
		if (e.getSource() == passengersComboBox) {
			String selectedItem = passengersComboBox.getSelectedItem().toString();
			int id = Integer.parseInt(selectedItem.substring(1, selectedItem.lastIndexOf("]")));
			String name = selectedItem.substring(selectedItem.lastIndexOf("] ") + 2, selectedItem.length());

			Passenger p = gos.getPassengers().getPassenger(id, name);
			if (p.getRoute() != null)
				map.updateMap(p.getRoute().copy(), true);
		}
		if (e.getSource() == backButton) {
			gos.loadGOS("" + mapFileName + ".map");
			gos.getPassengers().importPassengers("" + mapFileName + ".pas");
			new BusPlannerGUI(gos, gos.getPassengers());
			this.dispose();
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if(e.getSource() == timeSlider) {
			setTime(timeSlider.getValue());
			//System.out.println(passengerLocations.length);
			setPeopleOnBuses(passengerLocations[gos.getPassengers().size()+1][timeSlider.getValue()]);
		}
	}
}
