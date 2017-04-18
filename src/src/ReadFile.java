package src;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ReadFile {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(new File("airportCodes.txt")))) {
			
			String line;
		    while ((line = br.readLine()) != null) {
		       String[] s = line.split(",");
		       System.out.println(s[4]);
		    }
		}
	}
}