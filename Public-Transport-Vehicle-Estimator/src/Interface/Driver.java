package Interface;
import java.awt.Point;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import collections.GraphOfStations;
import planner.Route;
import planner.Station;

public class Driver {
	public static void main(String[] args) {
		/*
		GraphOfStations gos = new GraphOfStations();
		Station s1 = new Station("station1", new Point(1, 1));
		Station s2 = new Station("station2", new Point(2, 2));
		Station s3 = new Station("station3", new Point(3, 3));
		Station s4 = new Station("station4", new Point(1, 3));
		gos.addStation(s1);
		gos.addStation(s2);
		gos.addStation(s3);
		gos.addStation(s4);
		gos.addEdge(s1, s2);
		gos.addEdge(s2, s3);
		gos.addEdge(s3, s4);
		gos.addEdge(s1, s4);
		System.out.println(gos.getWeight(s1, s4));
		System.out.println(gos.getWeight(s3, s4));
		
		Route temp = gos.bestPath(s1.getStationId(), s4.getStationId());
		temp.tostring();*/
		
		
		//new UserScreen("1000", "Ug Lee");
		new AdminScreen();
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
