package planner;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

import collections.ArrayQueue;

public class Station implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String name; //station name
	private int stationId; //station uid, generated in GraphOfStations
	private Point vertexCoordinate; //station coordinates
	
	//used to keep track of passengers in the station
	private ArrayList<Passenger> passengers = new ArrayList<Passenger>();
	
	//Constructor.
	public Station(String name, Point vertexCoordinate){
		this.name = name;
		this.vertexCoordinate = vertexCoordinate;
	}
	
	//Mutators and Assessors
	//
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
	
	//Pertain to ArrayList<Passenger> passengers
	//add passenger to the ArrayList
	public void passengerAdd(Passenger passenger) {
		this.passengers.add(passenger);
	}
	//remove passenger from the ArrayList
	public void passengerRemove(Passenger passenger) {
		this.passengers.remove(passenger);
	}
	//@return - passengers
	public ArrayList<Passenger> getPassengers(){
		return this.passengers;
	}
}
