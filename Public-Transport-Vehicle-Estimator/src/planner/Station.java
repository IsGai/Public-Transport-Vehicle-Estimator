package planner;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

import collections.ArrayQueue;

public class Station implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String name;
	
	private int stationId; //station uid, generated in GraphOfStations
	
	private Point vertexCoordinate; //station coordinates
	
	
	private ArrayQueue<Passenger> stationOccupance = new ArrayQueue<Passenger>();
	private ArrayList<Passenger> passengers = new ArrayList<Passenger>();
	
	public Station(String name, Point vertexCoordinate){
		this.name = name;
		this.vertexCoordinate = vertexCoordinate;
		return;
	}
	


	public void addPassenger(Passenger toAdd){
		stationOccupance.add(toAdd);
		return;
	}
	
	public Passenger removePassenger(Passenger toRemove) throws Exception{
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
	
	public ArrayQueue<Passenger> getStationOccupance(){
		return this.stationOccupance;
	}
	
	public void passengerAdd(Passenger passenger) {
		this.passengers.add(passenger);
	}
	public void passengerRemove(Passenger passenger) {
		this.passengers.remove(passenger);
	}
	public ArrayList<Passenger> getPassengers(){
		return this.passengers;
	}
}
