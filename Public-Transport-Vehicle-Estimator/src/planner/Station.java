package planner;

import java.awt.Point;

import collections.ArrayQueue;

public class Station {
	
	String name;
	
	int stationId;
	
	Point vertexCoordinate;
	
	
	ArrayQueue<Passenger> stationOccupance = new ArrayQueue<Passenger>();
	
	public Station(String name, Point vertexCoordinate){
		this.name = name;
		this.vertexCoordinate = vertexCoordinate;
		return;
	}
	


	public void addPassenger(Passenger toAdd){
		stationOccupance.add(toAdd);
		return;
	}
	
	public Passenger removePassenger(Passenger toRemove){
		return stationOccupance.remove();
	}
	

	
	public int getStationId() {
		return stationId;
	}

	public void setStationId(int stationId) {
		this.stationId = stationId;
	}
	
	public String getName() {
		return name;
	}

	public Point getVertexCoordinate() {
		return vertexCoordinate;
	}
}
