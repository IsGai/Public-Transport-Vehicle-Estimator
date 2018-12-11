package Interface;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import collections.GraphOfStations;
import collections.Passengers;
import planner.Passenger;

public class BusPlannerGUI extends JFrame implements ActionListener {
	public static void main(String[] args) {
		new BusPlannerGUI();
	}

	//GUI LoginScreen size
	private static int LOGINSCREEN_WIDTH = 500;
	private static int LOGINSCREEN_HEIGHT = 500;
	
	// LoginScreen Components
	private JPanel buttonsPanel = new JPanel();
	private JButton userButton = new JButton("Passenger");
	private JButton adminButton = new JButton("Admin");

	//User Login Screen
	private JPanel userPanel = new JPanel();
	private JPanel idPanel = new JPanel();
	private JLabel idLabel = new JLabel("ID: ");
	private JTextField idTextField = new JTextField(20);
	private JPanel namePanel = new JPanel();
	private JLabel nameLabel = new JLabel("Name: ");
	private JTextField nameTextField = new JTextField(20);

	//Admin Login Screen
	private JPanel adminPanel = new JPanel();
	private JPanel usernamePanel = new JPanel();
	private JLabel usernameLabel = new JLabel("Username: ");
	private JTextField usernameTextField = new JTextField(20);
	private JPanel passwordPanel = new JPanel();
	private JLabel passwordLabel = new JLabel("Password: ");
	private JTextField passwordTextField = new JTextField(20);

	//Part of User Login Screen
	private JPanel loginPanel = new JPanel();
	private JButton loginButton = new JButton("Login");
	private JButton addUserButton = new JButton("New User");
	private JButton remUserButton = new JButton("Remove User");

	// other variables
	private GraphOfStations gos;

	// with default MAP and PASSENGERS
	public BusPlannerGUI() {
		gos = new GraphOfStations(); // default
		busPlannerGUI();
	}
	
	//When loaded MAP and PASSENGERS
	public BusPlannerGUI(GraphOfStations gos, Passengers<Passenger> passengers) {
		this.gos = gos;
		gos.setPassengers(passengers);
		busPlannerGUI();
	}

	//GUI setup
	public void busPlannerGUI() {
		this.setTitle("Bus Planner");
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

		//GUI Sizing and Locationing
		this.setSize(LOGINSCREEN_WIDTH, LOGINSCREEN_HEIGHT);
		this.setLocationRelativeTo(null);

		//GUI setup and intialization
		panelSetups();
		GUIComponents();
		addComponents();

		this.setVisible(true);
		this.toFront();
		this.requestFocus();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	//Setup all of the panels
	public void panelSetups() {
		buttonsPanel.add(userButton);
		buttonsPanel.add(adminButton);
		userPanelSetup();
		adminPanelSetup();
		loginPanelSetup();
	}

	//User Login Screen Setup
	public void userPanelSetup() {
		idPanel.add(idLabel);
		idPanel.add(idTextField);
		namePanel.add(nameLabel);
		namePanel.add(nameTextField);
		panelSetup(userPanel, idPanel, namePanel);
	}

	//Admin Login Screen Setup
	public void adminPanelSetup() {
		usernamePanel.add(usernameLabel);
		usernamePanel.add(usernameTextField);
		passwordPanel.add(passwordLabel);
		passwordPanel.add(passwordTextField);
		panelSetup(adminPanel, usernamePanel, passwordPanel);
	}

	/**
	 * Used to setup both User/Admin Login Screens
	 * @param panel - the User/Admin panel
	 * @param panel1 - first panel to add
	 * @param panel2 - second panel to add
	 */
	public void panelSetup(JPanel panel, JPanel panel1, JPanel panel2) {
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(panel1);
		panel.add(panel2);
	}

	//User Login Screen
	public void loginPanelSetup() {
		loginPanel.add(loginButton);
		loginPanel.add(addUserButton);
		loginPanel.add(remUserButton);
	}

	//default settings, and setting action listeners for components
	public void GUIComponents() {
		userButton.setEnabled(false);
		userButton.addActionListener(this);
		adminButton.addActionListener(this);
		loginButton.addActionListener(this);
		addUserButton.addActionListener(this);
		remUserButton.addActionListener(this);
	}

	//Adds the three major panels to the GUI
	public void addComponents() {
		this.add(buttonsPanel);
		this.add(userPanel);
		this.add(loginPanel);
	}

	/**
	 * Swtiches between User/Admin login screens
	 * @param screen - the screen to switch to (User/Admin)
	 */
	public void changeScreenTo(JPanel screen, boolean goingToAdminScreen) {
		this.getContentPane().removeAll();
		this.repaint();
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		
		adminButton.setEnabled(goingToAdminScreen);
		userButton.setEnabled(!goingToAdminScreen);
		addUserButton.setVisible(goingToAdminScreen);
		remUserButton.setVisible(goingToAdminScreen);

		this.add(buttonsPanel);
		this.add(screen);
		this.add(loginPanel);

		this.validate();
	}

	/**
	 * Validate that username/password matche those in the Admin.dat
	 * @return true - if Admin.dat username/passwords match what's on the textFields
	 */
	public boolean validateAdmin() {
		String[] admin = readAdmin("Admin.dat");
		if (!usernameTextField.getText().equals(admin[0])) {
			return false;
		}
		if (!passwordTextField.getText().equals(admin[1])) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param filePath - relative path to Admin.data
	 * @return String[0] = username, String[1] = password
	 */
	public String[] readAdmin(String filePath) {
		String username = "";
		String password = "";
		try {
			ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filePath));
			try {
				Object temp = inputStream.readObject(); // for security measures only, is not used
				username = inputStream.readUTF(); // = "username"
				password = inputStream.readUTF(); // = "password"
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			inputStream.close();
		} catch (IOException e) {

		}
		return new String[] { username, password };
	}

	//JOptionPane popup message dialogue
	public void outputMessage(String message, String title, int messageType) {
		JOptionPane.showMessageDialog(this, message, title, messageType);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton temp = (JButton) e.getSource();
		if (temp == userButton) {
			changeScreenTo(userPanel, true);
		}
		if (temp == adminButton) {
			changeScreenTo(adminPanel, false);
		}
		if (temp == addUserButton) {
			String name = JOptionPane.showInputDialog("Enter in your name.");
			//Triple checks that something was entered
			if (name!=null && name.trim().length() > 0 && name.length() > 0) {
				Passenger p = new Passenger(name); //create the passenger, generated ID
				gos.addPassenger(p); //adds passenger via GraphOfStations, automatically exports to appropriate Passengers data file
				outputMessage("New passenger " + p.getName() + " was succesfully added.\n" + p.getName() + "'s ID is \""
						+ p.getId() + "\"", "Passenger Added", JOptionPane.PLAIN_MESSAGE);
			}else {
				outputMessage("You did not enter in anything", "Error Message", JOptionPane.ERROR_MESSAGE);
			}
		}
		if (temp == remUserButton) {
			String id = JOptionPane.showInputDialog("Verify ID.");
			String name = JOptionPane.showInputDialog("Verify name.");
			try {
				//display successful removal message if such a passenger exist
				if (gos.removePassenger(Integer.parseInt(id), name)) {
					outputMessage("Passenger[ID:" + id + ", Name:" + name + "] was successfully removed",
							"Passenger Removed", JOptionPane.PLAIN_MESSAGE);
				} else
					outputMessage("Passenger[ID:" + id + ", Name:" + name
							+ "] could not be found.\nPlease make sure you've entered in the correct information.",
							"Passenger Not Found", JOptionPane.PLAIN_MESSAGE);
			} catch (NumberFormatException e2) {
				outputMessage("Please enter in a number. ie: \"1000\".", "Invalid ID Format",
						JOptionPane.ERROR_MESSAGE);
			}
		}
		if (temp == loginButton) {
			if (!userButton.isEnabled()) {
				// find passenger, pass Passenger to UserScreen()
				// load routes and stations to map(), set map to editable(false)
				int id = 0;
				String name = nameTextField.getText();
				try {
					id = Integer.parseInt(idTextField.getText());
					Passenger currentPassenger = gos.getPassengers().getPassenger(id, name); // search for passenger
					if (currentPassenger != null) {
						//Passenger is found, go to userScreen
						new UserScreen(currentPassenger, gos);
						this.dispose();
					} else {
						//Passenger not found, display error message
						outputMessage("Could not find passenger with information.\nPassenger[ID:" + id + ", Name:"
								+ name + "]", "Passenger Not Found", JOptionPane.PLAIN_MESSAGE);
					}
				} catch (NumberFormatException nfe) {
					//ID input was not in number format
					outputMessage("Make sure ID textfield is only numbers.\nie: \"1000\".", "Invalid ID Format",
							JOptionPane.ERROR_MESSAGE);
				}
			}
			if (!adminButton.isEnabled() && validateAdmin()) {
				// load routes and stations to map(), set map to editable(true)
				new AdminScreen(gos);
				this.dispose();
			}
		}
	}
}