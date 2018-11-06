package planner;

/*
 * Class represents a passenger. It includes their name and 
 * data for their route.
 */
public class Passenger {
	private String name;
	private Route myRoute;
	
	Passenger(String name, Station start, Station end){
		this.name = name;
		//myRoute = bestpath
	}
	
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
