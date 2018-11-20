package collections;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

import planner.Route;
import planner.Station;

public class GraphOfStations {
	private ArrayList<ArrayList<Double>> adjacencyList = new ArrayList<ArrayList<Double>>();
	private ArrayList<Station> stationList = new ArrayList<Station>();
	private int stationCount = 0;

	//stationEdges holds references to which stations are connect to which station
	//<Station> replicate of adjacencyList
	private ArrayList<ArrayList<Station>> stationEdges = new ArrayList<ArrayList<Station>>();
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
		adjacencyList.add(new ArrayList<Double>());
		stationEdges.add(new ArrayList<Station>());
		for (int i = 0; i <= stationCount; i++) {
			// adds 0.0 to every station, for when every new station is created
			for(int j=adjacencyList.get(i).size();j<=stationCount;j++) {
				adjacencyList.get(i).add(0.0);
				stationEdges.get(i).add(null);
			}
		}

		// Increment station count.
		stationCount++;

		// Create row for new station.
		ArrayList<Double> newStationList = new ArrayList<Double>(stationCount);
		for (int i = 0; i < stationCount; i++) {
			newStationList.add((double) 0);
		}

		// Set station ID
		stationToAdd.setStationId(stationCount - 1);

		// Add station to station list.
		stationList.add(stationToAdd);
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
		/*debugging
		System.out.println(id1 + ", " + id2 + ": " + weight);
		System.out.println(adjacencyList.get(0).size());
		System.out.println(adjacencyList.get(1).size());*/
		
		adjacencyList.get(id1).set(id2, weight);
		adjacencyList.get(id2).set(id1, weight);
		
		//refer to itself
		stationEdges.get(id1).set(id1, vertexOne);
		stationEdges.get(id2).set(id2, vertexTwo);
		//connect it to other station
		stationEdges.get(id1).set(id2, vertexTwo);
		stationEdges.get(id2).set(id1, vertexOne);
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
	public void removeStation(Station station) {
		stationList.remove(station); //might have issues with how stationID is implemented, and used
	}
	public ArrayList<Point> getStationsCoords(){
		ArrayList<Point> temp = new ArrayList<Point>();
		for(Station s: stationList)
			if(s!=null)
				temp.add(s.getVertexCoordinate());
		return temp;
	}	
	public ArrayList<ArrayList<Station>> getStationEdges(){
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

}
