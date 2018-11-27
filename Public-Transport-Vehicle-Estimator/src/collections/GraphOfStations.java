package collections;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import java.awt.Point;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

import planner.Passenger;
import planner.Route;
import planner.Station;

public class GraphOfStations implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private AdjacencyList<ArrayList<Double>> adjacencyList = new AdjacencyList<ArrayList<Double>>();
	//doing this to quickly implement serializable
	private class AdjacencyList<T> extends ArrayList<T> implements Serializable{
		private static final long serialVersionUID = 1L;
		public AdjacencyList() {
		}
		public AdjacencyList(int s) {
			super(s);
		}
	}
	private StationList<Station> stationList = new StationList<Station>();
	//doing this to quickly implement serializable
	private class StationList<T> extends ArrayList<T> implements Serializable{
		private static final long serialVersionUID = 1L;
	}
	private int stationCount = 0;

	//used to store which stations are connected to which stations
	// "asdflmfao" is used to seperate the two stationNames
	private StationEdges<String> stationEdges = new StationEdges<String>();
	//doing this to quickly implement serializable
	private class StationEdges<T> extends ArrayList<T> implements Serializable{
		private static final long serialVersionUID = 1L;
	}
	private Passengers<Passenger> passengers;//imported from BusPlannerGUI()
	private String fileName = "Default";
	
	public GraphOfStations() {
		//for testing purposes in Driver.java
		this.loadGOS("src/Data/" + fileName + ".map");
		this.passengers = new Passengers<Passenger>(fileName);
		//this.passengers = new ;
	}
	/*
	 * Description: Adds a station to the graph
	 * 
	 * Arguments: stationToAdd - The station you are adding to the graph.
	 * 
	 * Precondition: Station should have populated instance variables.
	 * 
	 * Postcondition: The graph will be extended to contain the new station.
	 */
	public void addStation(Station stationToAdd) {
		// Loop to add column to other stations.
		//System.out.println(stationCount);
		/*
		for (int i = 0; i <= stationCount; i++) {
			// adds 0.0 to every station, for when every new station is created
			for(int j=adjacencyList.get(i).size();j<=stationCount;j++) {
				adjacencyList.get(i).add(0.0);
				stationEdges.get(i).add(null);
			}
		}*/

		// Increment station count.
		stationCount++;
		adjacencyList.add(new AdjacencyList<Double>(stationCount));
	
		for(int x=0;x<stationCount;x++)
			for(int y=adjacencyList.get(x).size();y<stationCount;y++) 
				adjacencyList.get(x).add(0.0);

		// Create row for new station.
		ArrayList<Double> newStationList = new ArrayList<Double>(stationCount);
		for (int i = 0; i < stationCount; i++) {
			newStationList.add((double) 0);
		}

		// Set station ID
		stationToAdd.setStationId(stationCount - 1);

		// Add station to station list.
		stationList.add(stationToAdd);
		//System.out.println("addStation(): " + stationToAdd.getVertexCoordinate());
	}

	/*
	 * Description: Adds an edge to the graph
	 * 
	 * Arguments: vertexOne - One of the two stations you would like to include in
	 * the edge. vertexTwo - The other station you would like to include.
	 * 
	 * Precondition: Stations should have populated instance variables.
	 * 
	 * Postcondition: A double will be returned with the weight value.
	 */
	public void addEdge(Station vertexOne, Station vertexTwo) {
		double weight = getWeight(vertexOne, vertexTwo);
		int id1 = vertexOne.getStationId();
		int id2 = vertexTwo.getStationId();
		
		adjacencyList.get(id1).set(id2, weight);
		adjacencyList.get(id2).set(id1, weight);
		
		stationEdges.add(vertexOne.getName() + "asdflmfao" + vertexTwo.getName());
	}

	/*
	 * Description: Finds the weight between two stations.
	 * 
	 * Arguments: vertexOne - One of the two stations you would like to include in
	 * your weight. vertexTwo - The other station you would like to include.
	 * 
	 * Precondition: Stations should have populated instance variables.
	 * 
	 * Postcondition: A double will be returned with the weight value.
	 */
	public double getWeight(Station vertexOne, Station vertexTwo) {
		double x2 = vertexTwo.getVertexCoordinate().getX();
		double x1 = vertexOne.getVertexCoordinate().getX();
		double run = x2 - x1;

		double y2 = vertexTwo.getVertexCoordinate().getY();
		double y1 = vertexOne.getVertexCoordinate().getY();
		double rise = y2 - y1;

		return sqrt(pow(run, 2) + pow(rise, 2));
	}

	public Route bestPath(int start, int end) {
		// Create structures for algorithm to use.
		HashMap<Integer, Double> totalCosts = new HashMap<Integer, Double>(stationCount - 1);
		HashMap<Integer, Integer> previousStation = new HashMap<Integer, Integer>(stationCount - 1);
		PriorityQueue<StationNode> minimumQueue = new PriorityQueue<StationNode>();
		ArrayList<Integer> visited = new ArrayList<Integer>();

		// Set initial values.
		for (int i = 0; i < stationCount; i++) {
			totalCosts.put(i, Double.POSITIVE_INFINITY);
		}
		totalCosts.put(start, (double) 0);
		StationNode startNode = new StationNode(start, (double) 0);
		minimumQueue.add(startNode);

		// Main loop.
		while (!minimumQueue.isEmpty()) {
			StationNode newSmallest = minimumQueue.poll();
			int smallest = newSmallest.getStation();

			// Loop through neighbors.
			for (int i = 0; i < stationCount; i++) {
				double weight = adjacencyList.get(smallest).get(i);
				if (!(weight == 0)) { // Check if actually neighbor.
					if (!visited.contains(i)) { // Check if visited.

						double altPath = totalCosts.get(smallest) + weight; // Generate alternate path.

						if (altPath < totalCosts.get(i)) { // If alternate path is quicker.
							minimumQueue.remove(new StationNode(i, totalCosts.get(i)));
							minimumQueue.add(new StationNode(i, altPath));
							totalCosts.put(i, altPath);
							previousStation.put(i, smallest);
						}

					}
				}
			}
		}

		Route bestRoute = new Route();
		for (int i = end; i != start; i = previousStation.get(i)) {
			bestRoute.pushStation(stationList.get(i));
		}
		bestRoute.pushStation(stationList.get(start)); //adds starting station, because loops doesn't

		return bestRoute;

	}

	//William's defined methods
	public boolean hasStationByName(String stationName) {
		for(Station s: stationList) 
			if(s!=null) 
				if(s.getName().equalsIgnoreCase(stationName))
					return true;
		return false;
	}
	public Station getStationByName(String stationName) {
		for(Station s: stationList) 
			if(s!=null) 
				if(s.getName().equalsIgnoreCase(stationName))
					return s;
		return null;
	}
	public void removeStation(Station station) { //not yet implemented
		stationList.remove(station); //might have issues with how stationID is implemented and used
	}
	public ArrayList<Station> getStationList(){
		return this.stationList;
	}
	public ArrayList<String> getStationEdges(){
		return this.stationEdges;
	}
	public boolean hasStationByPoint(Point point) {
		for(Station s: stationList)
			if(s!=null)
				if(s.getVertexCoordinate().equals(point))
					return true;
		return false;
	}
	public Station getStationByPoint(Point point) {
		for(Station s: stationList)
			if(s!=null)
				if(s.getVertexCoordinate().equals(point))
					return s;
		return null;
	}
	/*
	 * @predcondition - id = an integer
	 */
	public boolean removePassenger(int id, String name) {
		for(Passenger p: passengers)
			if(p!=null)
				if(p.getName().equalsIgnoreCase(name) && p.getId()==id) {
					passengers.remove(p);
					return true;
				}
		return false;		
	}

	public void saveGos(String fileName) {
		try {
			ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("src/Data/"+fileName + ".map"));
			outputStream.writeObject(adjacencyList);
			outputStream.writeObject(stationList);
			outputStream.writeObject(stationCount);
			outputStream.writeObject(stationEdges);
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public boolean loadGOS(String filePath) {// has to be changed according to data structures
		boolean loaded = false;
		try {
			ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filePath));
			try {
				this.adjacencyList = (AdjacencyList<ArrayList<Double>>) inputStream.readObject();
				this.stationList = (StationList<Station>) inputStream.readObject();
				this.stationCount = (int) inputStream.readObject();
				this.stationEdges = (StationEdges<String>) inputStream.readObject();
				inputStream.close();
				loaded =  true;
			} catch (Exception e) {
				loaded = false;
			}
			inputStream.close();
		} catch (IOException e) {
			loaded = false;
		}
		return loaded;
	}
	public void clearGOS() {
		this.adjacencyList = new AdjacencyList<ArrayList<Double>>();
		this.stationList = new StationList<Station>();
		this.stationCount = 0;
		this.stationEdges = new StationEdges<String>();
		this.passengers = new Passengers<Passenger>();
	}
	public Passengers<Passenger> getPassengers(){
		return this.passengers;
	}
	public void setPassengers(Passengers<Passenger> passengers) {
		this.passengers = passengers;
	}
}
