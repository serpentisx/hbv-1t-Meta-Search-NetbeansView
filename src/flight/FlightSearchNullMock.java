package flight;

import java.util.Date;
import java.util.List;

/* Mocks a flight-search that returns a null reference */
public class FlightSearchNullMock implements FlightSearch {
	@Override
	public List<Flight> search(Date departing, String origin, String destination, int passengers) {
		return null;
	}
}
