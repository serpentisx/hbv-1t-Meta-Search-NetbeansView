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
		// SimpleDateFormat format = new SimpleDateFormat("ddMMyyyy");
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
	
	public String getDate() {
		String day = Integer.toString(this.date);
		String zero = "0";
		if(day.length() == 7) {
			day = zero.concat(day);
			return day.substring(1, 2) + " " + getMonth(day) + " " + day.substring(4, day.length());
		}
		return day.substring(0, 2) + " " + getMonth(day) + " " + day.substring(4, day.length());
	}
	
	private String getMonth(String day) {
		int month = Integer.parseInt(day.substring(2, 4));
		switch (month) {
                    case 1:
                        return "January";
                    case 2:
                        return "February";
                    case 3:
                        return "March";
                    case 4:
                        return "April";
                    case 5:
                        return "May";
                    case 6:
                        return "June";
                    case 7:
                        return "July";
                    case 8:
                        return "August";
                    case 9:
                        return "September";
                    case 10:
                        return "October";
                    case 11:
                        return "November";
                    case 12:
                        return "December";
                    default:
                        throw new AssertionError("Month should be of one of the twelve in the Gregorian calendar");
                }
	}

	public int getAvailableSeats() {
		return availableSeats;
	}

	public int getPrice() {
		return price;
	}	
	
}
