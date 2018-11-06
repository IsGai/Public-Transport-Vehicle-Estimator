package Interface;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

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
	//Passengers[] passengers; //reserved: program automatically loads from Passengers.dat
	//Route[] routes; //reserved: program automaticall loads from Routes.dat

	public BusPlannerGUI() {
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
		//default values
		//addUserButton.setVisible(false);
		//remUserButton.setVisible(false);
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

	public boolean validateUser() {
		String[] user = readUser("src\\Data\\Passengers.dat");
		if (!idTextField.getText().equals(user[0])) {
			return false;
		}
		if (!nameTextField.getText().equalsIgnoreCase(user[1])) { //case insensitive
			return false;
		}
		return true;
	}

	public boolean validateAdmin() {
		String[] admin = readAdmin("src\\Data\\Admin.dat");
		if (!usernameTextField.getText().equals(admin[0])) {
			return false;
		}
		if (!passwordTextField.getText().equals(admin[1])) {
			return false;
		}
		return true;
	}

	public String[] readUser(String filePath) {//has to be changed according to data structures
		String id = "";
		String name = "";
		try {
			ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filePath));
			try {
				id = inputStream.readUTF(); // = "1000"
				name = inputStream.readUTF(); // = "Ug Lee"
			} catch (Exception e) {
				e.printStackTrace();
			}
			inputStream.close();
		} catch (IOException e) {

		}
		return new String[] { id, name };
	}

	public String[] readAdmin(String filePath) {//has to be changed according to data structures
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
		if(temp == addUserButton) {
			/*
			 * Whatever class holds the main Passengers data
			 * run its method to add passenger
			 * This GUI will only pass the new passengers name, ID should be self generated
			 */
		}
		if(temp == remUserButton) {
			/*
			 * Whatever class holds the main Passengers data
			 * run its method to remove passenger
			 * search through Passengers.dat to find matching ID and name for passenger
			 * if found, then remove that passenger and save Passengers.dat
			 */
		}
		if (temp == loginButton) {
			if (!userButton.isEnabled() && validateUser()) {
				//find passenger, pass Passenger to UserScreen()
				//load routes and stations to map(), set map to editable(false)
				new UserScreen(idTextField.getText(), nameTextField.getText());
				this.dispose();
			}
			if (!adminButton.isEnabled() && validateAdmin()) {
				//load routes and stations to map(), set map to editable(true)
				new AdminScreen();
				this.dispose();
			}
		}
	}
}