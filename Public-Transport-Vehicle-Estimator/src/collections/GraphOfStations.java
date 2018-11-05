package collections;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

import planner.Route;
import planner.Station;

public class GraphOfStations {
	ArrayList<ArrayList<Double>> AdjacencyList = new ArrayList<ArrayList<Double>>();
	ArrayList<Station> stationList = new ArrayList<Station>();
	int stationCount;
	
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
		//Loop to add column to other stations.
		for(int i = 0; i < stationCount; i++) {
			AdjacencyList.get(i).add((double) 0);
		}
		
		//Increment station count.
		stationCount++;
		
		//Create row for new station.
		ArrayList<Double> newStationList = new ArrayList<Double>(stationCount);
		for(int i = 0; i < stationCount; i++) {
			newStationList.add((double) 0);
		}
		
		//Set station ID
		stationToAdd.setStationId(stationCount - 1);
		
		//Add station to station list.
		stationList.add(stationToAdd);
	}
	
	/*
	 * Description: Adds an edge to the graph
	 * 
	 * Arguments: vertexOne - One of the two stations you would like to 
	 * include in the edge.
	 * vertexTwo - The other station you would like to include.
	 * 
	 * Precondition: Stations should have populated instance variables.
	 * 
	 * Postcondition: A double will be returned with the weight value.
	 */
	public void addEdge(Station vertexOne, Station vertexTwo) {
		double weight = getWeight(vertexOne, vertexTwo);
		int id1 = vertexOne.getStationId();
		int id2 = vertexTwo.getStationId();
		
		AdjacencyList.get(id1).set(id2, weight);
		AdjacencyList.get(id2).set(id1, weight);
		
	}
	
	/*
	 * Description: Finds the weight between two stations.
	 * 
	 * Arguments: vertexOne - One of the two stations you would like to 
	 * include in your weight.
	 * vertexTwo - The other station you would like to include.
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
		//Create structures for algorithm to use.
		HashMap<Integer, Double> totalCosts = new HashMap<Integer, Double>(stationCount - 1);
		HashMap<Integer, Integer> previousStation = new HashMap<Integer, Integer>(stationCount - 1);
		PriorityQueue<StationNode> minimumQueue = new PriorityQueue<StationNode>();
		ArrayList<Integer> visited = new ArrayList<Integer>();
		
		//Set initial values.
		for(int i = 0; i < stationCount; i++) {
			totalCosts.put(i, Double.POSITIVE_INFINITY);
		}
		totalCosts.put(start, (double) 0);
		StationNode startNode = new StationNode(start, (double) 0);
		minimumQueue.add(startNode);
		
		//Main loop.
		while(!minimumQueue.isEmpty()) {
			StationNode newSmallest = minimumQueue.poll();
			int smallest = newSmallest.getStation();
			
			//Loop through neighbors.
			for(int i = 0; i < stationCount; i++) {
				double weight = AdjacencyList.get(smallest).get(i);
				if(!(weight == 0)) { //Check if actually neighbor.
					if(!visited.contains(i)) {  //Check if visited.
						
						double altPath = totalCosts.get(smallest) +
								weight; // Generate alternate path.
						
						if(altPath < totalCosts.get(i)) { //If alternate path is quicker.
							minimumQueue.remove(new StationNode(i,totalCosts.get(i)));
							minimumQueue.add(new StationNode(i, altPath));
							totalCosts.put(i, altPath);
							previousStation.put(i, smallest);
						}
						
					}
				}
			}
		}
		
		Route bestRoute = new Route();
		for(int i = end; i == start; i = previousStation.get(i)) {
			bestRoute.pushStation(stationList.get(i));
		}
		
		return bestRoute;
		
	}
	
}
