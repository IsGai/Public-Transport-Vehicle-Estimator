package planner;

import java.io.Serializable;

/*
 * Class represents a passenger. It includes their name and 
 * data for their route.
 */
public class Passenger implements Serializable{
	private static final long serialVersionUID = 1L;
	private String name;
	private Route myRoute;
	private int startTime;
	
	private static int uID = 1000;
	private int id;
	public Passenger(int uID) {
		/*BusPlannerGUI
		 * automatically load map and pas data
		 * this method assures that the passengers ID start back where they left off
		 */
		Passenger.uID = uID; 
	}
	public Passenger(String name) {
		this.name = name;
		this.id = uID+=10;
		this.myRoute = null;
	}
	public Passenger(String name, Station start, Station end){
		this.name = name;
		this.id = uID+=10;
		//myRoute = bestpath
	}
	
	public int getTime() {
		return this.startTime;
	}
	public void setTime(int time) {
		this.startTime = time;
	}	
	
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return this.id;
	}
	public static int getUid() {
		return Passenger.uID;
	}
	public void setRoute(Route route) {
		this.myRoute = route;
		this.myRoute.nextStation().addPassenger(this); //reference passenger in station
	}
	public Route getRoute() {
		return this.myRoute;
	}
}
