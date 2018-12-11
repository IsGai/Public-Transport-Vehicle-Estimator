/*
 * Class description: Node used for easier Djikstra's calculation in
 * GraphOfStations class.
 */

package collections;


public class StationNode implements Comparable<StationNode> {
	int Station;
	double cost;
	
	//Constructor.
	public StationNode(int station, double cost) {
		super();
		Station = station;
		this.cost = cost;
	}
	
	//Mutators.
	public int getStation() {
		return Station;
	}
	public void setStation(int station) {
		Station = station;
	}
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	
	//Used to compate two station nodes. 
	//Specifically compares in a way helpful for Djikstra's
	//using cost.
	@Override
	public int compareTo(StationNode other) {
		return (int)(this.getCost() - other.getCost());
	}
}
