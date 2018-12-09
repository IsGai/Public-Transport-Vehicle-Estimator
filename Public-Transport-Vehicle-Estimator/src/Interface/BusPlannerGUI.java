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

	// GUI related
	static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
	private Dimension guiSize = new Dimension();
	private Point guiLocation = new Point();

	// LoginScreen Components
	private JPanel buttonsPanel = new JPanel();
	private JButton userButton = new JButton("Passenger");
	private JButton adminButton = new JButton("Admin");

	private JPanel userPanel = new JPanel();
	private JPanel idPanel = new JPanel();
	private JLabel idLabel = new JLabel("ID: ");
	private JTextField idTextField = new JTextField(20);
	private JPanel namePanel = new JPanel();
	private JLabel nameLabel = new JLabel("Name: ");
	private JTextField nameTextField = new JTextField(20);

	private JPanel adminPanel = new JPanel();
	private JPanel usernamePanel = new JPanel();
	private JLabel usernameLabel = new JLabel("Username: ");
	private JTextField usernameTextField = new JTextField(20);
	private JPanel passwordPanel = new JPanel();
	private JLabel passwordLabel = new JLabel("Password: ");
	private JTextField passwordTextField = new JTextField(20);

	private JPanel loginPanel = new JPanel();
	private JButton loginButton = new JButton("Login");
	private JButton addUserButton = new JButton("New User");
	private JButton remUserButton = new JButton("Remove User");

	// other variables
	// reserved: program automatically loads from Passengers.dat
	private GraphOfStations gos;
	private String fileName = "";

	// with default MAP and PASSENGERS
	public BusPlannerGUI() {
		gos = new GraphOfStations(); // default
		fileName = gos.getFileName();
		busPlannerGUI();
	}

	// with predefined MAP and PAASSENGERS
	public BusPlannerGUI(GraphOfStations gos) {
		this.gos = gos;
		fileName = gos.getFileName();
		busPlannerGUI();
	}
	
	public BusPlannerGUI(GraphOfStations gos, Passengers<Passenger> passengers) {
		this.gos = gos;
		gos.setPassengers(passengers);
		fileName = gos.getFileName();
		busPlannerGUI();
	}

	public void busPlannerGUI() {
		this.setTitle("Bus Planner");
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

		guiSize.width = (int) 500;// (SCREEN_SIZE.width / 1.7);
		guiSize.height = (int) 500;// (SCREEN_SIZE.height / 1.7);
		this.setSize(guiSize.width, guiSize.height);
		guiLocation.x = SCREEN_SIZE.width / 2 - guiSize.width / 2;
		guiLocation.y = SCREEN_SIZE.height / 2 - guiSize.height / 2;
		this.setLocation(guiLocation.x, guiLocation.y);

		panelSetups();
		GUIComponents();
		addComponents();

		this.setVisible(true);
		this.toFront();
		this.requestFocus();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void panelSetups() {
		buttonsPanel.add(userButton);
		buttonsPanel.add(adminButton);
		userPanelSetup();
		adminPanelSetup();
		loginPanelSetup();
	}

	public void userPanelSetup() {
		idPanel.add(idLabel);
		idPanel.add(idTextField);
		namePanel.add(nameLabel);
		namePanel.add(nameTextField);
		panelSetup(userPanel, idPanel, namePanel);
	}

	public void adminPanelSetup() {
		usernamePanel.add(usernameLabel);
		usernamePanel.add(usernameTextField);
		passwordPanel.add(passwordLabel);
		passwordPanel.add(passwordTextField);
		panelSetup(adminPanel, usernamePanel, passwordPanel);
	}

	public void panelSetup(JPanel panel, JPanel panel1, JPanel panel2) {
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(panel1);
		panel.add(panel2);
	}

	public void loginPanelSetup() {
		loginPanel.add(loginButton);
		loginPanel.add(addUserButton);
		loginPanel.add(remUserButton);
	}

	public void GUIComponents() {
		userButton.setEnabled(false);
		userButton.addActionListener(this);
		adminButton.addActionListener(this);
		loginButton.addActionListener(this);
		addUserButton.addActionListener(this);
		remUserButton.addActionListener(this);
	}

	public void addComponents() {
		this.add(buttonsPanel);
		this.add(userPanel);
		this.add(loginPanel);
	}

	public void changeScreenTo(JPanel screen) {
		this.getContentPane().removeAll();
		this.repaint();
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

		this.add(buttonsPanel);
		this.add(screen);
		this.add(loginPanel);

		this.validate();
	}

	public boolean validateAdmin() {
		String[] admin = readAdmin("src/Data/Admin.dat");
		if (!usernameTextField.getText().equals(admin[0])) {
			return false;
		}
		if (!passwordTextField.getText().equals(admin[1])) {
			return false;
		}
		return true;
	}

	public String[] readAdmin(String filePath) {// has to be changed according to data structures
		String username = "";
		String password = "";
		try {
			ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filePath));
			try {
				Object temp = inputStream.readObject(); // for security measures, is not used
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

	public void outputMessage(String message, String title, int messageType) {
		JOptionPane.showMessageDialog(this, message, title, messageType);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton temp = (JButton) e.getSource();
		if (temp == userButton) {
			adminButton.setEnabled(true);
			userButton.setEnabled(false);
			addUserButton.setVisible(true);
			remUserButton.setVisible(true);
			changeScreenTo(userPanel);
		}
		if (temp == adminButton) {
			userButton.setEnabled(true);
			adminButton.setEnabled(false);
			addUserButton.setVisible(false);
			remUserButton.setVisible(false);
			changeScreenTo(adminPanel);
		}
		if (temp == addUserButton) {
			String name = JOptionPane.showInputDialog("Enter in your name.");
			if (name.length() > 0) {
				Passenger p = new Passenger(name);
				gos.addPassenger(p);
				outputMessage("New passenger " + p.getName() + " was succesfully added.\n" + p.getName() + "'s ID is \""
						+ p.getId() + "\"", "Passenger Added", JOptionPane.PLAIN_MESSAGE);
			}
		}
		if (temp == remUserButton) {
			String id = JOptionPane.showInputDialog("Verify ID.");
			String name = JOptionPane.showInputDialog("Verify name.");
			try {
				boolean removed = gos.getPassengers().removePassenger(Integer.parseInt(id), name);
				if (removed) {
					outputMessage("Passenger[ID:" + id + ", Name:" + name + "] was successfully removed",
							"Passenger Removed", JOptionPane.PLAIN_MESSAGE);
					gos.getPassengers().exportPassengers(fileName);
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
						new UserScreen(currentPassenger, gos);
						this.dispose();
					} else {
						outputMessage("Could not find passenger with information.\nPassenger[ID:" + id + ", Name:"
								+ name + "]", "Passenger Not Found", JOptionPane.PLAIN_MESSAGE);
					}
				} catch (NumberFormatException nfe) {
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