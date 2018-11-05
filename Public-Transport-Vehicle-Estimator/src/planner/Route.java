package planner;
/*Description: This class wraps the LinkedStack class to create a model of
 * a "route" keeping track of the stations a passenger will need to 
 * go to.
 */
import collections.LinkedStack;

public class Route {
	LinkedStack<Station> route = new LinkedStack<Station>();

	
	public Route(){
		super();		
	}
	
	/*
	 * Description: See the next station in the route. 
	 * Wraps the peek method from LinkedStack class.
	 * 
	 * Precondition: Route should not be empty.
	 * 
	 * Postcondition: <code>Station</code> object will be returned.
	 */
	public Station nextStation() {
		return route.peek();
	}
	
	/*
	 * Description: Add a new station to the route.
	 * Wraps the push method of the LinkedStack method.
	 * 
	 * Precondition: Stations instance variables should be populated.
	 * 
	 * Postcondition: Station will be added to the route.
	 */
	public void pushStation(Station toAdd) {
		route.push(toAdd);
		return;
	}
	
	/*
	 * Description: Remove the next station in the route. 
	 * Wraps the remove method from LinkedStack class.
	 * 
	 * Precondition: Route should not be empty. Should no longer need the
	 * current station to be accessed.
	 * 
	 * Postcondition: A station will be removed from the queue.
	 */
	public void arrived() {
		route.pop();
		return;
	}
	
	/*
	 * Description: See the next station in the route. 
	 * Wraps the isEmpty method from LinkedStack class.
	 * 
	 * Precondition: 
	 * 
	 * Postcondition: Returns false if there is still data in the route.
	 *  Returns true if route is empty.
	 */
	public boolean isEmpty() {
		return route.isEmpty();
	}
}
