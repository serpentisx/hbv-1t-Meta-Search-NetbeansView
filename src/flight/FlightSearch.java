package flight;
import java.util.Date;
import java.util.List;

public interface FlightSearch {
	
	/* needs clarification */
	public List<Flight> search(Date departing, String origin, String destination, int passengers);
}
