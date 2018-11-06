package Interface;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class AdminScreen extends JFrame implements ActionListener{

	//GUI related
	static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
	private Dimension guiSize = new Dimension();
	private Point guiLocation = new Point();
	
	//leftPanel
	private JPanel leftPanel = new JPanel();
	private JPanel passengerPanel = new JPanel();
	private JButton addPassengerButton = new JButton("Add Passenger");
	private JButton removePassengerButton = new JButton("Remove Passenger");
	private JPanel stationPanel = new JPanel();
	private JButton addStationButton = new JButton("Add Station");
	private JButton removeStationButton = new JButton("Remove Station");
	private JPanel routePanel = new JPanel();
	private JButton addRouteButton = new JButton("Add Route ");
	private JButton removeRouteButton  = new JButton("Remove Route ");
	private JButton backButton = new JButton("Return to Login Screen");
	
	//rightPanel
	private JPanel rightPanel = new JPanel();
	private Map map = new Map(false);
	public AdminScreen() {
		this.setTitle("Admin Screen");
		this.setLayout(new GridLayout(1, 2));

		guiSize.width = (int) (SCREEN_SIZE.width / 1.7);
		guiSize.height = (int) (SCREEN_SIZE.height / 1.2);
		this.setSize(guiSize.width, guiSize.height);
		guiLocation.x = SCREEN_SIZE.width / 2 - guiSize.width / 2;
		guiLocation.y = SCREEN_SIZE.height / 2 - guiSize.height / 2;
		this.setLocation(guiLocation.x, guiLocation.y);

		initialSetup();
		leftPanel();
		rightPanel();
		adminScreen();
		
		this.setVisible(true);
		this.toFront();
		this.requestFocus();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void initialSetup() {
		
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
		
		leftPanel.add(tempPanel, BorderLayout.NORTH);
		leftPanel.add(tempPanel, BorderLayout.NORTH);
		leftPanel.add(backButton, BorderLayout.SOUTH);
		backButton.addActionListener(this);
	}
	public void passengerPanel() {
		addPassengerButton.addActionListener(this);
		removePassengerButton.addActionListener(this);
		passengerPanel.add(addPassengerButton);
		passengerPanel.add(removePassengerButton);
	}
	public void stationPanel() {
		addStationButton.addActionListener(this);
		removeStationButton.addActionListener(this);
		stationPanel.add(addStationButton);
		stationPanel.add(removeStationButton);
	}
	public void routePanel() {
		addRouteButton.addActionListener(this);
		removeRouteButton.addActionListener(this);
		routePanel.add(addRouteButton);
		routePanel.add(removeRouteButton);
	}
	public void rightPanel() {
		rightPanel.setLayout(new GridLayout(1,1));
		rightPanel.add(new Map(false));
	}
	public void adminScreen() {
		this.add(leftPanel);
		this.add(rightPanel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton buttonClicked = (JButton) e.getSource();
		if(buttonClicked == addPassengerButton) {
			//JOptionPane "enter in a passenger name"
		}
		if(buttonClicked == removePassengerButton) {
			//JOptionPane "enter in passenger name and ID"
		}
		if(buttonClicked == addStationButton) {
			//JOptionPane enter in station POINTS or ID or Name?
		}
		if(buttonClicked == removeStationButton) {
			
		}
		if(buttonClicked == addRouteButton) {
			//Enter in 1st station, and then 2nd station
		}
		if(buttonClicked == removeRouteButton) {
			
		}
		if(buttonClicked == backButton) {
			new BusPlannerGUI();
			this.dispose();
		}
	}
}
