/*
 * Class description: Implementation of ArrayList for type Passenger.
 * Also includes methods to write data to disk.
 */
package collections;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import planner.Passenger;
import planner.Route;
import planner.Station;

public class Passengers<T> extends ArrayList<T> implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;
	private int uID = 1000;
	public Passengers() {
		new Passenger(uID);
	}
	public Passengers(String fileName) {
		importPassengers("" + fileName + ".pas"); //default loads from
		new Passenger(uID);
	}

	public Passenger getPassenger(int id, String name) {
		for(T t: this) {
			Passenger p = ((Passenger)t);
			if(p!=null)
				if(p.getId() == id && p.getName().equalsIgnoreCase(name))
					return p;
		}
		return null;
	}
	public boolean removePassenger(int id, String name) {
		for (T t : this)
			if (t != null) {
				Passenger p = ((Passenger) t);
				if (p.getName().equalsIgnoreCase(name) && p.getId() == id) {
					this.remove(p);
					return true;
				}
			}
		return false;
	}

	public void importPassengers(String filePath) {// has to be changed according to data structures
		Passengers<T> p = null;
		int uID = 1000;
		try {
			ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filePath));
			try {
				uID = inputStream.readInt();
				p = (Passengers<T>) inputStream.readObject();
			} catch (Exception e) {
				e.printStackTrace();
			}
			inputStream.close();
		} catch (IOException e) {
			System.out.println(filePath);
		}
		this.uID = uID;
		this.clear();
		this.addAll(p);
	}

	public void exportPassengers(String fileName) {
		try {
			ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("" + fileName + ".pas"));
			outputStream.writeInt(Passenger.getUid());
			outputStream.writeObject(this);
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public Passengers<T> copy() {
		Passengers copy = null;
		copy = (Passengers<T>)super.clone();
		return copy;
	}
}
