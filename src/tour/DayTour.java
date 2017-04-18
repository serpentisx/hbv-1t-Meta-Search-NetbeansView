package tour;
import java.text.ParseException;

public class DayTour {
	
	/* Reminder: Make sure the following 
	 * corresponds to the Day-tour-team's implementation
	 */	
	
	private String name;
	private int length;
	private String location;
	private int time;
	private int date;
	private int availableSeats;
	private int price;
	
	/* Constructor */
	public DayTour(String name, int duration, String location, String time, String date, int availableSeats, 
			int price) throws ParseException {
		this.name = name;
		this.length = duration;
		this.location = location;
		this.availableSeats = availableSeats;
		this.price = price;
		// SimpleDateFormat format = new SimpleDateFormat("ddMMyyyyHHmm");
		// this.time = format.parse(date + time);
		this.time = Integer.parseInt(time);
		this.date = Integer.parseInt(date);
		
	}
	/* Instance methods */
	public String getNameOfTrip() {
		return name;
	}

	public int getLengthOfTrip() {
		return length;
	}

	public String getLocation() {
		return location;
	}
	
	public int getTime() {
		return time;
	}
	
	public int getDate() {
		return date;
	}

	public int getAvailableSeats() {
		return availableSeats;
	}

	public int getPrice() {
		return price;
	}
	

	
	
}
