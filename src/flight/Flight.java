package flight;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Flight {
	
	/* Reminder: Make sure the following 
	 * corresponds to the Flight-team's implementation
	 */	
	
	private String flightNumber;
	private String airline;
	private String origin;
	private String destination;
	private Date departureTime;
	private Date arrivalTime;
	private int seatsAvailable;
	private int price;
	private int duration;
	
	/* Constructor */
	public Flight(String flightNumber, String airline, String origin, String destination, Date departureTime,
			Date arrivalTime, int seatsAvailable, int price) {
		this.flightNumber = flightNumber;
		this.airline = airline;
		this.origin = origin;
		this.destination = destination;
		this.departureTime = departureTime;
		this.arrivalTime = arrivalTime;
		this.seatsAvailable = seatsAvailable;
		this.price = price;
		
		long diff = arrivalTime.getTime() - departureTime.getTime();
		this.duration = (int)TimeUnit.MILLISECONDS.toMinutes(diff); 
	}
	
	/* Instance methods */	
	public String getFlightNumber() {
		return flightNumber;
	}
	
	public String getAirline() {
		return airline;
	}
	
	public String getOrigin() {
		return origin;
	}
	
	public String getDestination() {
		return destination;
	}
	
	public Date getDepartureTime() {
		return departureTime;
	}
	
	public Date getArrivalTime() {
		return arrivalTime;
	}
	
	public int getSeatsAvailable() {
		return seatsAvailable;
	}
	
	public int getPrice() {
		return price;
	}
	
	public int getDuration() {
		return duration;
	}
}
