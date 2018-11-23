package collections;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import planner.Passenger;

public class Passengers<T> extends ArrayList<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	private int uID;
	public Passengers() {
		importPassengers("src/Data/Passengers.dat"); //default loads from
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
		for (T p : this)
			if (p != null)
				if (((Passenger) p).getName().equalsIgnoreCase(name) && ((Passenger) p).getId() == id) {
					this.remove(p);
					return true;
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

		}
		this.uID = uID;
		this.addAll(p);
	}

	public void exportPassengers(String filePath) {
		try {
			ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filePath));
			outputStream.writeInt(Passenger.getUid());
			outputStream.writeObject(this);
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
