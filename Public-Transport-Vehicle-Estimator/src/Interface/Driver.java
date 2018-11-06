package Interface;
import java.awt.Point;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import collections.GraphOfStations;
import planner.Station;

public class Driver {
	public static void main(String[] args) {
		GraphOfStations gos = new GraphOfStations();
		Station s1 = new Station("station1", new Point(50, 50));
		Station s2 = new Station("station2", new Point(100, 100));
		Station s3 = new Station("station3", new Point(150, 150));
		gos.addStation(s1);
		gos.addStation(s2);
		gos.addEdge(s1, s2);
		System.out.println(gos.getWeight(s1, s2));
		gos.addStation(s3);
		gos.addEdge(s2, s3);
		System.out.println(gos.getWeight(s2, s3));
		
		//new UserScreen("1000", "Ug Lee");
		//new AdminScreen();
		//save();
	}
	public static void save() { //generate placeholder data files
		try {
			ObjectOutputStream outputStream = new ObjectOutputStream(
					new FileOutputStream("src\\Passengers.dat"));
			outputStream.writeUTF("1000");
			outputStream.writeUTF("Ug Lee");
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
