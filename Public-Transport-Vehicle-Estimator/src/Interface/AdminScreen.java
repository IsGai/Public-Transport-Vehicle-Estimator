/*
 * Class description: AdminScreen GUI
 */

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

	// leftPanel
	private JPanel leftPanel = new JPanel();
	//Passenger Panel - add/remove passenger
	private JPanel passengerPanel = new JPanel();
	private JButton addPassengerButton = new JButton("Add Passenger");
	private JButton removePassengerButton = new JButton("Remove Passenger");
	
	//StationRoute Panel - buttons for Map making
	private JPanel stationRoutePanel = new JPanel();
	private JButton addStationButton = new JButton("Add Station");
	private JButton addRouteButton = new JButton("Add Route ");

	private JPanel saveMapPanel = new JPanel();
	private JButton saveMapButton = new JButton("Save Map");
	private JButton loadMapButton = new JButton("Load Map");
	private JButton clearMapButton = new JButton("Clear Map");

	//Passengers Panel - has JComboBox
	private JPanel passengersPanel = new JPanel();
	private JComboBox<String> passengersComboBox = new JComboBox<String>();// change <String> to <Route>
	private JLabel passengersLabel = new JLabel("Passengers:");
	
	//Time Panel - has a JSlider
	private JPanel timePanel = new JPanel();
	private JSlider timeSlider = new JSlider(0,1439,1);
	private JLabel timeLabel = new JLabel("Time: ");
	private JLabel peopleOnBuses = new JLabel("People on Buses: ");

	//backButton is placed at the way bottom of leftPanel
	private JButton backButton = new JButton("Return to Login Screen");

	// rightPanel - contains Map
	private JPanel rightPanel = new JPanel();
	private GraphOfStations gos = null;
	private Map map = null;
	
	//Miscellaneous Variables
	private TitledBorder titledBorder; //displays current Map name on rightPanel
	private int[][] passengerLocations; // used in the simulation

	public AdminScreen(GraphOfStations gos) {
		this.setTitle("Admin Screen");
		this.setLayout(new GridLayout(1, 2));

		guiSize.width = (int) (SCREEN_SIZE.width / 1.7);
		guiSize.height = (int) (SCREEN_SIZE.height / 1.2);
		this.setSize(guiSize.width, guiSize.height);
		this.setLocationRelativeTo(null);

		//GUI components setup
		initialSetup(gos);
		leftPanel();
		rightPanel();
		adminScreen();

		this.setVisible(true);
		this.toFront();
		this.requestFocus();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	//initialize the essential datas
	public void initialSetup(GraphOfStations gos) {
		this.gos = gos;
		passengerLocations = gos.simulatePlacements();
		/*
		for(int i=0;i<passengerLocations.length;i++) {
			for(int x=0;x<passengerLocations[0].length;x++) {
				System.out.print(passengerLocations[i][x] + " ");
			}
			System.out.println();
		}*/
		map = new Map(false, gos, timeSlider, passengerLocations);
	}

	/**
	 * Returns a JPanel with a Component c in its center
	 * @param c - component to be placed in a JPanel
	 * @return a JPanel with one Component c in the center
	 */
	public JPanel centerToGrid(Component c) {
		JPanel temp = new JPanel();
		temp.add(c);
		return temp;
	}
	
	//setup timePanel
	public void timePanel() {
		timeSlider.addChangeListener(this);
		timePanel.add(timeSlider);
		timePanel.add(timeLabel);
	}

	public void leftPanel() {
		leftPanel.setLayout(new BorderLayout());
		
		//initialize panels
		stationPassengerPanel(addPassengerButton, removePassengerButton, passengerPanel);
		stationPassengerPanel(addStationButton, addRouteButton, stationRoutePanel);
		saveMapPanel();
		passengersPanel();
		timePanel();
		
		//tempPanel is everything going to the top of leftPanel
		JPanel tempPanel = new JPanel();
		tempPanel.setLayout(new BoxLayout(tempPanel, BoxLayout.Y_AXIS));
		tempPanel.add(passengerPanel);
		tempPanel.add(stationRoutePanel);
		tempPanel.add(saveMapPanel);
		tempPanel.add(centerToGrid(clearMapButton));
		tempPanel.add(centerToGrid(passengersPanel));
		tempPanel.add(timePanel);
		tempPanel.add(centerToGrid(peopleOnBuses));

		//keep middle of leftPanel cleared, for looks
		leftPanel.add(tempPanel, BorderLayout.NORTH);
		leftPanel.add(backButton, BorderLayout.SOUTH);
		
		//some additional listeners being set
		backButton.addActionListener(this);
		saveMapButton.addActionListener(this);
		loadMapButton.addActionListener(this);
		clearMapButton.addActionListener(this);
	}
	
	public void saveMapPanel() {
		saveMapPanel.setLayout(new GridLayout(1, 2));
		saveMapPanel.add(centerToGrid(saveMapButton));
		saveMapPanel.add(centerToGrid(loadMapButton));
	}

	public void passengersComboBox() {
		Passengers<Passenger> passengerList = gos.getPassengers();
		String[] passengerNames = new String[passengerList.size()];

		//Populate comboBox with passengers
		for (int x = 0; x < passengerList.size(); x++)
			if (passengerList.get(x) != null)
				passengerNames[x] = "[" + passengerList.get(x).getId() + "] " + passengerList.get(x).getName();
		
		//sort the comboBox
		for (int x = 0; x < passengerNames.length; x++)
			for (int y = 0; y < passengerNames.length - 1; y++)
				if (passengerNames[y].compareTo(passengerNames[y + 1]) > 0) {
					String temp = passengerNames[y];
					passengerNames[y] = passengerNames[y + 1];
					passengerNames[y + 1] = temp;
				}
		
		//set the comboBox to the Passenger data
		passengersComboBox.setModel(new DefaultComboBoxModel(passengerNames));
	}

	//setup passengersPanel
	public void passengersPanel() {
		passengersComboBox(); //update passengersComboBox
		passengersComboBox.addActionListener(this);
		passengersComboBox.setBackground(Color.white);
		passengersPanel.add(passengersLabel);
		passengersPanel.add(passengersComboBox);
	}

	//Sets up passengerPanel or stationRoutePanel, given the parameters
	public void stationPassengerPanel(JButton button1, JButton button2, JPanel panel) {
		button1.addActionListener(this);
		button2.addActionListener(this);
		
		panel.setLayout(new GridLayout(1, 2));
		panel.add(centerToGrid(button1));
		panel.add(centerToGrid(button2));
	}

	//setup rightPanel
	public void rightPanel() {
		rightPanel.setLayout(new BorderLayout());
		addScrollPane(rightPanel, map, gos.getFileName());
	}

	//Change titled on map, called when loading a Map
	public void changeMapTitle() {
		titledBorder.setTitle(gos.getFileName());
		this.repaint();
	}

	/**
	 * Called in rightPanel(), adds title and scroll bars to rightPanel of the AdminScreen
	 * @param panel - parent panel, gets a title
	 * @param panel2 - child panel, gets ScrollPane
	 * @param title - title given to panel
	 */
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

	//Gets a stand string input from user
	public String getInput(String message) {
		return JOptionPane.showInputDialog(message);
	}

	/**
	 * Verifies input is an Integer, whole number, positive, and between 0-975
	 * @param message - message to the user
	 * @return a value between 0-975
	 */
	public int getCoordInput(String message) {
		int coordinate;
		try {
			coordinate = Integer.parseInt(JOptionPane.showInputDialog(message));
			if(coordinate < 0 || coordinate > 975)throw new NumberFormatException();
			return coordinate;
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	//JOptionPane message
	public void outputMessage(String message, String title, int messageType) {
		JOptionPane.showMessageDialog(this, message, title, messageType);
	}
	
	/*
	 * Return current passengers in a station based on the time in the simulation
	 * @param stationId - the given Station's stationId
	 */
	public int getPassengerCount(int stationId){
		int time = timeSlider.getValue();
		return this.passengerLocations[stationId][time];
	}
	
	/**
	 * Converts the minutes in hours and minutes, then sets timeLabel to display it
	 * @param totalMinutes - the number of minutes
	 */
	public void setTime(int totalMinutes) {
		int hours = totalMinutes/60; 
		int minutes = totalMinutes%60;
		String actualTime = String.format("%d:%02d", hours, minutes);
		timeLabel.setText("Time: " + actualTime);
	}
	
	/**
	 * Called when timeSlider changes
	 * @param people - number of people currently on buses
	 */
	public void setPeopleOnBuses(int people) {
		peopleOnBuses.setText("People on Buses: " + people);
	}
	
	/**
	 * Used in actionPerformed(), when clicking remove passenger button, and when
	 * interacting with the passengersComboBox
	 * @return Passenger - the selected passenger from the passengersComboBox
	 */
	public Passenger getSelectedPassenger() {
		String selectedItem = passengersComboBox.getSelectedItem().toString();
		
		//Get Passenger data from selected item in passengersComboBox
		int id = Integer.parseInt(selectedItem.substring(1, selectedItem.lastIndexOf("]")));
		String name = selectedItem.substring(selectedItem.lastIndexOf("] ") + 2, selectedItem.length());

		//Find Passenger in Passengers
		return gos.getPassengers().getPassenger(id, name);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addPassengerButton) {
			String name = JOptionPane.showInputDialog("Enter in your name.");
			
			//Triple checks that something was entered
			if (name!=null && name.trim().length() > 0 && name.length() > 0) {
				Passenger p = new Passenger(name); //create the passenger, generated ID
				gos.addPassenger(p); //adds passenger via GraphOfStations, automatically exports to appropriate Passengers data file
				outputMessage("New passenger " + p.getName() + " was succesfully added.\n" + p.getName() + "'s ID is \""
						+ p.getId() + "\"", "Passenger Added", JOptionPane.PLAIN_MESSAGE);
				passengersComboBox(); // update passenger combo box
			}else {
				outputMessage("You did not enter in anything", "Error Message", JOptionPane.ERROR_MESSAGE);
			}
		}
		if (e.getSource() == removePassengerButton) {
			String id = JOptionPane.showInputDialog("Verify ID.");
			String name = JOptionPane.showInputDialog("Verify name.");
			
			try {
				//remove passenger Route on map
				Passenger enteredPassenger = gos.getPassengers().getPassenger(Integer.parseInt(id), name);
				Passenger selectedPassenger = getSelectedPassenger();
				boolean removeMyRouteOnMap = (enteredPassenger.equals(selectedPassenger));
				
				//true is passenger(id, name) exist, false otherwise
				boolean removed = gos.removePassenger(Integer.parseInt(id), name);
				if (removed) {
					outputMessage("Passenger[ID:" + id + ", Name:" + name + "] was successfully removed",
							"Passenger Removed", JOptionPane.PLAIN_MESSAGE);
					passengersComboBox(); // update passenger combo box
					
					if(removeMyRouteOnMap)
						map.removeMyRoute(); //remove passenger Route on map
				} else //Cannot find Passenger
					outputMessage("Passenger[ID:" + id + ", Name:" + name
							+ "] could not be found.\nPlease make sure you've entered in the correct information.",
							"Passenger Not Found", JOptionPane.PLAIN_MESSAGE);
			} catch (NumberFormatException e2) { //Information entered is invalid
				outputMessage("Please enter in a number. ie: \"1000\".", "Invalid ID Format",
						JOptionPane.ERROR_MESSAGE);
			}
		}
		if (e.getSource() == addStationButton) {
			int stationX = -1, stationY = -1;
			
			do {
				//stationX = -1 if anything but a whole positive number outside of (0 - 975) is entered
				stationX = getCoordInput("Enter in X coordinate for station. (0 - 975)"); 
			} while (stationX == -1);
			do {
				//stationY = -1 if anything but a whole positive number outside of (0 - 975) is entered
				stationY = getCoordInput("Enter in Y coordinate for station. (0 - 975)");
			} while (stationY == -1);
			String stationN = getInput("Enter in NAME for station");
			
			Point stationCoord = new Point(stationX, stationY);
			if (map.clickedOnStation(stationCoord)) {
				outputMessage("There is already a station there!", "Error Message", JOptionPane.ERROR_MESSAGE);
			} else {
				Station s = new Station(stationN, stationCoord);
				gos.addStation(s); //addStation to GOS
				passengerLocations = gos.simulatePlacements(); //update simulation data
				map.updateMap(); //update Map
			}
		}
		if (e.getSource() == addRouteButton) {
			String station1 = getInput("Enter in name of station one");
			String station2 = getInput("Enter in name of station two");
			
			//Check if stations exists
			if (!gos.hasStationByName(station1))
				outputMessage("Cannot find station: " + station1, "Station Not Found", JOptionPane.ERROR_MESSAGE);
			if (!gos.hasStationByName(station2))
				outputMessage("Cannot find station: " + station2, "Station Not Found", JOptionPane.ERROR_MESSAGE);
			
			//Add Edge to GOS, and update Map to reflect changes
			if (gos.hasStationByName(station1) && gos.hasStationByName(station2)) {
				Station startStation = gos.getStationByName(station1);
				Station endStation = gos.getStationByName(station2);
				
				gos.addEdge(startStation, endStation);//add route to GOS
				map.updateMap();//display on Map
			}
		}
		if (e.getSource() == saveMapButton) {
			int choice = JOptionPane.showConfirmDialog(this, "Save to default(Default.map)?",
					"Save to default(Default.map)?", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			
			if (choice == JOptionPane.YES_OPTION) { //Save to Default.map, and Default.pas
				gos.saveGos("Default"); //save "".map
				gos.getPassengers().exportPassengers("Default"); //save "".pas
			}
			
			if (choice == JOptionPane.NO_OPTION) { //Save to "fileName".map, and "fileName".pas
				String fileName = JOptionPane.showInputDialog(this, "Enter in name to save file as.", "Save File As",
						JOptionPane.PLAIN_MESSAGE);
				gos.saveGos(fileName); //save "".map
				gos.getPassengers().exportPassengers(fileName); //save "".pas
			}
		}
		if (e.getSource() == loadMapButton) {
			JFileChooser jfl = new JFileChooser("src/"); //change to relative path for Jar File
			jfl.setFileSelectionMode(JFileChooser.FILES_ONLY);
			
			int option = jfl.showOpenDialog(this); // open JFileChooser
			if (option == JFileChooser.APPROVE_OPTION) {
				//gos.loadGOS() automatically loads GOS data to current GOS
				if (gos.loadGOS(jfl.getSelectedFile().getPath())) {
					//import Passengers to current GOS
					String passengerFilePath = jfl.getSelectedFile().getPath().toString().substring(0,
							jfl.getSelectedFile().getPath().toString().length() - 4) + ".pas";
					gos.getPassengers().importPassengers(passengerFilePath);
					
					//change GOS mapFileName to the one that just for imported
					String mapFileName = jfl.getSelectedFile().getName().substring(0,
							jfl.getSelectedFile().getName().length() - 4);
					gos.setFileName(mapFileName);
					
					passengersComboBox(); // update passenger combo box
					map.updateMap(); // update Map
				} else {
					//called when any type of exceptions occur in the GOS import process
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
				gos.clearGOS(); //clear station/route/passenger data
				map.updateMap(); //update Map
				passengersComboBox(); //update JComboBox
				//does not automaticall save the file, must use Save button to see effects stay
			}
		}
		if (e.getSource() == passengersComboBox) {
			Passenger selectedPassenger = getSelectedPassenger();
			if (selectedPassenger.getRoute() != null) //if passenger has a route, display it on Map
				map.updateMap(selectedPassenger.getRoute().copy(), true);
		}
		if (e.getSource() == backButton) {
			//reload current Map, must use Save button if wishes to see changes
			gos.loadGOS(gos.getFileName() + ".map");
			gos.getPassengers().importPassengers(gos.getFileName() + ".pas");
			
			//call custom BusPlannerGUI constructor
			new BusPlannerGUI(gos, gos.getPassengers());
			this.dispose();
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if(e.getSource() == timeSlider) {
			//display current time, and current passengers on buses
			setTime(timeSlider.getValue());
			//System.out.println(gos.getPassengers().size() + ", " + timeSlider.getValue());
			//System.out.println(passengerLocations[gos.getPassengers().size()+1][timeSlider.getValue()]);
			//System.out.println(gos.getPassengers().size() + " " + passengerLocations.length);
			setPeopleOnBuses(passengerLocations[passengerLocations.length-1][timeSlider.getValue()]);
		}
	}
}
