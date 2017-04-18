package flight;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/* Mocks a flight-search that returns a nonempty list */
public class FlightSearchMock implements FlightSearch {

	@Override
	public List<Flight> search(Date departing, String origin, String destination, int passengers) {
		List<Flight> flights = new ArrayList<Flight>();
		try {
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			Date departureTime1 = format.parse("05/07/2017 22:00");
			Date arrivalTime1   = format.parse("05/07/2017 23:00");
			Date departureTime2 = format.parse("18/07/2017 19:00");
			Date arrivalTime2   = format.parse("18/07/2017 21:00");
			
			Flight flight1 = new Flight("FI200", "Bob Air", "JFK", "KEF", departureTime1, arrivalTime1, 99, 22000);
			Flight flight2 = new Flight("FI210", "Bob Air", "KEF", "JFK", departureTime2, arrivalTime2, 99, 21000);
			
			flights.add(flight1);
			flights.add(flight2);
			
		} catch (ParseException e) { 
			System.out.println("parse exception"); 
		}
		return flights;
	}
}