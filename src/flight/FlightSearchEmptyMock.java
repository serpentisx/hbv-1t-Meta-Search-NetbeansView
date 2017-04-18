package flight;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/* Mocks a flight-search that returns an empty list */
public class FlightSearchEmptyMock implements FlightSearch {
	@Override
	public List<Flight> search(Date departing, String origin, String destination, int passengers) {
		return new ArrayList<Flight>();
	}
}
