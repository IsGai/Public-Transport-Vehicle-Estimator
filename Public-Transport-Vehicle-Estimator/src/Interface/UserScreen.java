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

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class UserScreen extends JFrame implements ActionListener {

	// GUI related
	static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
	private Dimension guiSize = new Dimension();
	private Point guiLocation = new Point();

	// UserScreen
	private JPanel leftPanel = new JPanel();
	private JPanel passengerPanel = new JPanel();
	private JLabel idLabel = new JLabel("ID:");
	private JTextField idTextField = new JTextField(10);
	private JLabel nameLabel = new JLabel("Name:");
	private JTextField nameTextField = new JTextField(10);
	private JPanel stationPanel = new JPanel();
	private JLabel departureLabel = new JLabel("Departure:");
	private JComboBox<String> departureComboBox;// change <String> to <Route>
	private JLabel destinationLabel = new JLabel("Destination:");
	private JComboBox<String> destinationComboBox;// change <String> to <Route>
	private JPanel removePanel = new JPanel();
	private JButton removeButton = new JButton("Remove Trip/Passenger");
	private JButton backButton = new JButton("Return to Login Screen");

	private JPanel rightPanel = new JPanel();
	//Map map = new Map(restricted)
	public UserScreen(String id, String name) {
		this.setTitle("Passenger Screen");
		this.setLayout(new GridLayout(1,2));

		guiSize.width = (int) (SCREEN_SIZE.width / 1.7);
		guiSize.height = (int) (SCREEN_SIZE.height / 1.2);
		this.setSize(guiSize.width, guiSize.height);
		guiLocation.x = SCREEN_SIZE.width / 2 - guiSize.width / 2;
		guiLocation.y = SCREEN_SIZE.height / 2 - guiSize.height / 2;
		// this.setLocationRelativeTo(null);
		this.setLocation(guiLocation.x, guiLocation.y);

		initialSetup(id, name);
		leftPanel();
		rightPanel();
		userScreen();

		this.setVisible(true);
		this.toFront();
		this.requestFocus();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void initialSetup(String id, String name) {
		idTextField.setText(id);
		idTextField.setEditable(false);
		nameTextField.setText(name);
		nameTextField.setEditable(false);
	}

	public void leftPanel() {
		passengerPanel();
		stationPanel();
		removePanel();
		leftPanel.setLayout(new BorderLayout());
		
		JPanel tempPanel = new JPanel(new GridLayout(3,1,5,5));
		tempPanel.add(passengerPanel);
		tempPanel.add(stationPanel);
		
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
		String[] tempStations = { "station1", "station2", "station3" };
		departureComboBox = new JComboBox<String>(tempStations);
		departureComboBox.setBackground(Color.white);
		destinationComboBox = new JComboBox<String>(tempStations);
		destinationComboBox.setBackground(Color.white);
		
		stationPanel.setLayout(new BoxLayout(stationPanel, BoxLayout.X_AXIS));
		stationPanel.add(departureLabel);
		stationPanel.add(departureComboBox);
		stationPanel.add(destinationLabel);
		stationPanel.add(destinationComboBox);
	}

	public void removePanel() {
		removePanel.add(removeButton);
		removeButton.addActionListener(this);
		removePanel.add(backButton);
		backButton.addActionListener(this);
	}

	public void rightPanel() {
		//Map map = getMapData();
		rightPanel.setLayout(new BorderLayout());
		rightPanel.add(new Map(true, null)); //new Map(true, GraphOfStations) should be read from a textfile?
	}

	public void userScreen() {
		this.add(leftPanel);
		this.add(rightPanel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == removeButton) {
			String id = JOptionPane.showInputDialog(this, "Please verify in ID");
			String name = JOptionPane.showInputDialog(this, "Please verify in Name");
			System.out.println(id + ", " + name);
			// JOptionPane "Please confirm ID and Name to remove.
		}
		if(e.getSource() == backButton) {
			new BusPlannerGUI();
			this.dispose();
		}
	}
}
