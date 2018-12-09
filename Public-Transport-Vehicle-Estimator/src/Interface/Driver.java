package Interface;
import java.awt.Point;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import collections.GraphOfStations;
import collections.Passengers;
import planner.Passenger;
import planner.Route;
import planner.Station;

public class Driver {
	static Passengers<Passenger> p = new Passengers<Passenger>();
	public static void main(String[] args) {
		exportPassengers("Tamriel");
	}
	public static void exportPassengers(String fileName) {
		try {
			ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("src/Data/" + fileName + ".pas"));
			outputStream.writeInt(Passenger.getUid());
			outputStream.writeObject(p);
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
