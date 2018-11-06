package Interface;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Driver {
	public static void main(String[] args) {
		new UserScreen("1000", "Ug Lee");
		new AdminScreen();
		//save();
	}
	public static void save() {
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
