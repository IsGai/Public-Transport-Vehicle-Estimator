/*
 * Class description: Implementation of Graph data structure for type Station.
 * Also includes methods for Djikstras and a simulation.
 */

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

public class GraphOfStations implements Serializable {
	private static final long serialVersionUID = 1L;

	private AdjacencyList<ArrayList<Double>> adjacencyList = new AdjacencyList<ArrayList<Double>>();

	// doing this to quickly implement serializable
	private class AdjacencyList<T> extends ArrayList<T> implements Serializable {
		private static final long serialVersionUID = 1L;

		public AdjacencyList() {
		}

		public AdjacencyList(int s) {
			super(s);
		}
	}

	private StationList<Station> stationList = new StationList<Station>();

	// doing this to quickly implement serializable
	private class StationList<T> extends ArrayList<T> implements Serializable {
		private static final long serialVersionUID = 1L;
	}

	private int stationCount = 0;

	// used to store which stations are connected to which stations
	// "asdflmfao" is used to seperate the two stationNames
	private StationEdges<String> stationEdges = new StationEdges<String>();

	// doing this to quickly implement serializable
	private class StationEdges<T> extends ArrayList<T> implements Serializable {
		private static final long serialVersionUID = 1L;
	}

	private Passengers<Passenger> passengers;// imported from BusPlannerGUI()
	private String fileName = "Default";

	public GraphOfStations() {
		// for testing purposes in Driver.java
		this.loadGOS("src/Data/" + fileName + ".map");
		this.passengers = new Passengers<Passenger>(fileName);
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
		// System.out.println(stationCount);
		/*
		 * for (int i = 0; i <= stationCount; i++) { // adds 0.0 to every station, for
		 * when every new station is created for(int
		 * j=adjacencyList.get(i).size();j<=stationCount;j++) {
		 * adjacencyList.get(i).add(0.0); stationEdges.get(i).add(null); } }
		 */

		// Increment station count.
		stationCount++;
		adjacencyList.add(new AdjacencyList<Double>(stationCount));

		for (int x = 0; x < stationCount; x++)
			for (int y = adjacencyList.get(x).size(); y < stationCount; y++)
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
		// System.out.println("addStation(): " + stationToAdd.getVertexCoordinate());
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
		bestRoute.pushStation(stationList.get(start)); // adds starting station, because loops doesn't

		return bestRoute;

	}

	public int[][] simulatePlacements() {
		int numberOfPassengers = passengers.size();
		int numberOfStations = stationList.size();
		int bus = numberOfStations;

		int[][] passengerLocations = new int[numberOfStations + 1][1440];
		// Initialize all locations and times to 0.
		for (int i = 0; i < 1440; i++) {
			for (int j = 0; j < numberOfStations + 1; j++) {
				passengerLocations[j][i] = 0;
			}
		}

		// Outer loop goes through all passengers.
		Station firstStation;
		int firstID;
		Station secondStation;
		int secondID;
		Route routeForSim;
		int time;
		boolean loopFlag;
		for (int pass = 0; pass < numberOfPassengers; pass++) {
			System.out.println(passengers.get(pass).getName());
			System.out.println(passengers.get(pass).getRoute().nextStation().getName());
			routeForSim = passengers.get(pass).getRoute().copy();
			time = passengers.get(pass).getTime();
			// Loop through each station.
			loopFlag = true;
			while (loopFlag) {
				firstStation = routeForSim.pop();
				firstID = firstStation.getStationId();
				// Catches if last station.
				try {
					secondStation = routeForSim.nextStation();
					secondID = secondStation.getStationId();
				} catch (Exception e) {
					// Last station no value to write because passenger leaves.
					loopFlag = false;
					break;
				}

				int roundedWeight = (int) Math.ceil(adjacencyList.get(firstID).get(secondID));

				// Write values for station.
				while ((time % roundedWeight) != 0) {
					passengerLocations[firstID][time]++;
					time++;
				}

				// Write values for bus.
				while (roundedWeight > 0) {
					passengerLocations[bus][time]++;
					roundedWeight--;
					time++;
				}
			}

		}
		for (int i = 0; i < numberOfStations + 1; i++) {
			for (int j = 0; j < 1440; j++) {
				System.out.print(passengerLocations[i][j] + " ");
			}
			System.out.print("\n");
		}
		return passengerLocations;
	}

	public void clearPassengersandStationLists() {
		stationList = new StationList();
		passengers = new Passengers();
	}

	// William's defined methods
	public boolean hasStationByName(String stationName) {
		for (Station s : stationList)
			if (s != null)
				if (s.getName().equalsIgnoreCase(stationName))
					return true;
		return false;
	}

	public Station getStationByName(String stationName) {
		for (Station s : stationList)
			if (s != null)
				if (s.getName().equalsIgnoreCase(stationName))
					return s;
		return null;
	}

	public void removeStation(Station station) { // not yet implemented
		stationList.remove(station); // might have issues with how stationID is implemented and used
	}

	public ArrayList<Station> getStationList() {
		return this.stationList;
	}

	public ArrayList<String> getStationEdges() {
		return this.stationEdges;
	}

	public boolean hasStationByPoint(Point point) {
		for (Station s : stationList)
			if (s != null)
				if (s.getVertexCoordinate().equals(point))
					return true;
		return false;
	}

	public Station getStationByPoint(Point point) {
		for (Station s : stationList)
			if (s != null)
				if (s.getVertexCoordinate().equals(point))
					return s;
		return null;
	}

	public void saveGos(String fileName) {
		try {
			ObjectOutputStream outputStream = new ObjectOutputStream(
					new FileOutputStream("src/Data/" + fileName + ".map"));
			outputStream.writeObject(adjacencyList);
			outputStream.writeObject(stationList);
			outputStream.writeObject(stationCount);
			outputStream.writeObject(stationEdges);
			outputStream.close();
			passengers.exportPassengers(fileName);
			this.fileName = fileName;
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
				this.fileName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length() - 4);
				// System.out.println(fileName);
				loaded = true;
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

	public Passengers<Passenger> getPassengers() {
		return this.passengers;
	}

	public void setPassengers(Passengers<Passenger> passengers) {
		this.passengers = passengers;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void addPassenger(Passenger p) {
		this.getPassengers().add(p);
		this.getPassengers().exportPassengers(fileName);
	}

	public boolean removePassenger(int id, String name) {
		Passenger p = getPassengers().getPassenger(id, name);
		if (p != null) {
			if (p.getRoute() != null)
				p.getRoute().nextStation().passengerRemove(p);
			boolean successfulRemove = this.getPassengers().remove(p);
			this.getPassengers().exportPassengers(fileName);
			
			return successfulRemove;
		} else
			return false;
	}
}
