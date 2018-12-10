package Interface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import collections.GraphOfStations;
import planner.Passenger;
import planner.Route;
import planner.Station;

public class UserScreen extends JFrame implements ActionListener {

	// GUI related
	static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
	private Dimension guiSize = new Dimension();

	// UserScreen
	private JPanel leftPanel = new JPanel();
	private JPanel passengerPanel = new JPanel();
	private JLabel idLabel = new JLabel("ID:");
	private JTextField idTextField = new JTextField(10);
	private JLabel nameLabel = new JLabel("Name:");
	private JTextField nameTextField = new JTextField(10);
	
	//
	private JPanel stationPanel = new JPanel();
	private JLabel departureLabel = new JLabel("Departure:");
	private JComboBox<String> departureComboBox;//Station names
	private JLabel destinationLabel = new JLabel("Destination:");
	private JComboBox<String> destinationComboBox;//Station names
	
	//Panel for users to select save/remove/save their routes
	private JPanel routePanel = new JPanel();
	private JButton saveRouteButton = new JButton("Save Route");
	private JButton removeRouteButton = new JButton("Remove Route");
	private JButton showRouteButton = new JButton("Show My Route");
	
	//Panel on the bottom of the left side
	private JPanel removePanel = new JPanel();
	private JButton removeButton = new JButton("Remove Trip/Passenger");
	private JButton backButton = new JButton("Return to Login Screen");
	
	//Simulation Panel
	private JPanel timePanel = new JPanel();
	private JComboBox<String> timeComboBox;
	private JButton timeButton = new JButton("Save time");

	private JPanel rightPanel = new JPanel(); //holds the Map
	private Map map;
	
	private GraphOfStations gos;
	private Passenger passenger; //the current passenger being worked on

	public UserScreen(Passenger passenger, GraphOfStations gos) {
		this.setTitle("Passenger Screen");
		this.setLayout(new GridLayout(1, 2));

		guiSize.width = (int) (SCREEN_SIZE.width / 1.7);
		guiSize.height = (int) (SCREEN_SIZE.height / 1.2);
		this.setSize(guiSize.width, guiSize.height);
		this.setLocationRelativeTo(null);

		initialSetup(passenger, gos);
		leftPanel();
		rightPanel();
		userScreen();

		this.setVisible(true);
		this.toFront();
		this.requestFocus();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void initialSetup(Passenger passenger, GraphOfStations gos) {
		this.passenger = passenger;
		idTextField.setText(passenger.getId() + "");
		idTextField.setEditable(false);
		nameTextField.setText(passenger.getName());
		nameTextField.setEditable(false);

		this.gos = gos;
		this.map = new Map(true, gos, null, null);
	}

	public void leftPanel() {
		passengerPanel();
		stationPanel();
		routePanel();
		removePanel();
		timePanel();
		leftPanel.setLayout(new BorderLayout());

		JPanel tempPanel = new JPanel(new GridLayout(4, 1, 5, 5));
		tempPanel.add(passengerPanel);
		tempPanel.add(stationPanel);
		tempPanel.add(routePanel);
		tempPanel.add(timePanel);
		
		leftPanel.add(tempPanel, BorderLayout.NORTH);
		leftPanel.add(removePanel, BorderLayout.SOUTH);
	}

	public void passengerPanel() {
		passengerPanel.setLayout(new BoxLayout(passengerPanel, BoxLayout.X_AXIS));
		passengerPanel.add(idLabel);
		passengerPanel.add(idTextField);
		passengerPanel.add(nameLabel);
		passengerPanel.add(nameTextField);
	}

	public void stationPanel() {
		ArrayList<Station> stationList = gos.getStationList();
		String[] stationNames = new String[stationList.size()];
		for (int x = 0; x < stationList.size(); x++)
			if (stationList.get(x) != null)
				stationNames[x] = stationList.get(x).getName();
		for(int x=0;x<stationNames.length;x++)
			for(int y=0;y<stationNames.length-1;y++)
				if(stationNames[y].compareTo(stationNames[y+1]) > 0) {
					String temp = stationNames[y];
					stationNames[y] = stationNames[y+1];
					stationNames[y+1] = temp;
				}

		departureComboBox = new JComboBox<String>(stationNames);
		departureComboBox.setBackground(Color.white);
		destinationComboBox = new JComboBox<String>(stationNames);
		destinationComboBox.setBackground(Color.white);

		stationPanel.setLayout(new BoxLayout(stationPanel, BoxLayout.X_AXIS));
		stationPanel.add(departureLabel);
		stationPanel.add(departureComboBox);
		stationPanel.add(destinationLabel);
		stationPanel.add(destinationComboBox);
		departureComboBox.addActionListener(this);
		destinationComboBox.addActionListener(this);
	}

	public void routePanel() {
		saveRouteButton.addActionListener(this);
		removeRouteButton.addActionListener(this);
		showRouteButton.addActionListener(this);
		routePanel.add(saveRouteButton);
		routePanel.add(removeRouteButton);
		routePanel.add(showRouteButton);
	}

	public void removePanel() {
		removePanel.add(removeButton);
		removeButton.addActionListener(this);
		removePanel.add(backButton);
		backButton.addActionListener(this);
	}
	
	public void timePanel() {
		String[] timeStrings = { "7:00am", "7:30am", "8:00am", "8:30am", "9:00am",
				"9:30am", "10:00am", "10:30am", "11:00am", "11:30am", "12:00pm",
				"12:30pm", "1:00pm", "1:30pm", "2:00pm", "2:30pm", "3:00pm"};
		timeComboBox = new JComboBox(timeStrings);
		timeComboBox.addActionListener(this);
		timeButton.addActionListener(this);
		timePanel.add(timeComboBox);
		timePanel.add(timeButton);
	}

	public void rightPanel() {
		rightPanel.setLayout(new BorderLayout());
		addScrollPane(rightPanel, map, "" + gos.getFileName() + "");
	}

	public void addScrollPane(JPanel panel, JPanel panel2, String title) {
		JScrollPane temp = new JScrollPane(panel2, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		panel.setBorder(BorderFactory.createTitledBorder(title));
		panel.setBackground(null);
		panel.add(temp);
	}

	public void userScreen() {
		this.add(leftPanel);
		this.add(rightPanel);
	}

	public void outputMessage(String message, String title, int messageType) {
		JOptionPane.showMessageDialog(this, message, title, messageType);
	}

	Station departureStation = null, destinationStation = null;

	@Override
	public void actionPerformed(ActionEvent e) {
		String selectedStation = "";
		if (e.getSource() == removeButton) {
			String id = JOptionPane.showInputDialog(this, "Please verify in ID");
			String name = JOptionPane.showInputDialog(this, "Please verify in Name");
			try {
				int actualId = Integer.parseInt(id);
				if (passenger.getId() == actualId && passenger.getName().equalsIgnoreCase(name)) {
					int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove\n"
							+ "Passenger[ID:" + passenger.getId() + ", Name:" + passenger.getName() + "]?");
					if (option == JOptionPane.OK_OPTION) {
						gos.removePassenger(passenger.getId(), passenger.getName());
						outputMessage(
								"Passenger[ID:" + passenger.getId() + ", Name:" + passenger.getName()
										+ "] has been deleted.\n" + "Returning to mainscreen.",
								"Passenger Removed", JOptionPane.PLAIN_MESSAGE);
						new BusPlannerGUI(gos, gos.getPassengers());
						this.dispose();
					}
				}
			} catch (NumberFormatException exception) {
				outputMessage("The information is incorrect!", "Error Message", JOptionPane.ERROR_MESSAGE);
			}
			// JOptionPane "Please confirm ID and Name to remove.
		}
		if (e.getSource() == backButton) {
			new BusPlannerGUI(gos, gos.getPassengers());
			this.dispose();
		}
		if (e.getSource() == departureComboBox) {
			selectedStation = departureComboBox.getSelectedItem().toString();
			departureStation = gos.getStationByName(selectedStation);
		}
		if (e.getSource() == destinationComboBox) {
			selectedStation = destinationComboBox.getSelectedItem().toString();
			destinationStation = gos.getStationByName(selectedStation);
		}
		if (e.getSource() == saveRouteButton) {
			if (departureStation == null && destinationStation == null)
				outputMessage("Please select a route first!", "Error Message", JOptionPane.ERROR_MESSAGE);
			else {
				outputMessage(departureStation.getName() + " to " + destinationStation.getName() + " route saved!",
						"Route Saved", JOptionPane.PLAIN_MESSAGE);
				passenger.setRoute(gos.bestPath(departureStation.getStationId(), destinationStation.getStationId()));
				gos.getPassengers().exportPassengers(gos.getFileName());
				gos.saveGos(gos.getFileName());
			}
		}
		if (e.getSource() == removeRouteButton) {
			if (passenger.getRoute() != null) {
				String startStation = passenger.getRoute().pop().getName();
				String endStation = "";
				while(!passenger.getRoute().isEmpty()) 
					endStation = passenger.getRoute().pop().getName();
				outputMessage("Route " + startStation + " to " + endStation + " removed" , "Route Removed", JOptionPane.PLAIN_MESSAGE);
				passenger.setRoute(null);
				gos.getPassengers().exportPassengers(gos.getFileName());
			} else {
				map.removeMyRoute();
				outputMessage("Passenger has no route saved!", "Error Message", JOptionPane.ERROR_MESSAGE);
			}
		}
		if(e.getSource() == showRouteButton) {
			if(passenger.getRoute()!=null) {
				Route tempRoute = passenger.getRoute().copy();
				Station s1 = tempRoute.pop();
				Station sLast = null;
				while(!tempRoute.isEmpty())
					sLast = tempRoute.pop();
				map.updateMap(gos.bestPath(s1.getStationId(), sLast.getStationId()), true);
			}else {
				map.removeMyRoute();
				outputMessage("No route saved", "Error Message", JOptionPane.ERROR_MESSAGE);
			}
		}
		if (e.getSource() == timeButton) {
			int time = 420 + (30 * timeComboBox.getSelectedIndex());
			passenger.setTime(time);
			System.out.println(passenger.getTime());
		}
		if (departureStation != null && destinationStation != null) {
			// draw route on GUI map
			map.updateMap(gos.bestPath(departureStation.getStationId(), destinationStation.getStationId()), false);
		}

	}
}
