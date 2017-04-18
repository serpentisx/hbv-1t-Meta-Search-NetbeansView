package test;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import src.*;

import org.junit.*;

public class PackageManagerTest {
	
	private PackageManager manager;
	private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
	
	@Before
	public void setUp() { }
	
	@After
	public void tearDown() {
		manager = null;
	}
	
	// Tests for searching flights
	
	@Test // Normal search resulting in a nonempty list of flights
	public void testSearchFlights() throws IllegalArgumentException {
		this.manager = new PackageManager(new FlightSearchMock(), new HotelSearchMock(), new DayTourSearchMock(), 
				new FlightReservationMock(), new HotelReservationMock(), new DayTourReservationMock());
		Date date;
		try {
			date = format.parse("15/04/2017");
			String code = "JFK"; 
			List<Flight> list = manager.searchFlights(date, code, true);
			assertNotNull(list); 
		} catch (ParseException e) {
			System.out.println("Parse exception");
		}
	}
	
	@Test // Search resulting in an empty list of flights
	public void testSearchFlightsNoResults() throws IllegalArgumentException {
		this.manager = new PackageManager(new FlightSearchEmptyMock(), new HotelSearchMock(), new DayTourSearchMock(), 
				new FlightReservationMock(), new HotelReservationMock(), new DayTourReservationMock());
		Date date;
		try {
			date = format.parse("15/04/2017");
			String code = "JFK"; 
			List<Flight> list = manager.searchFlights(date, code, true);
			assertNotNull(list); 
		} catch (ParseException e) {
			System.out.println("Parse exception");
		}
	}
	
	// Search resulting in a null reference
	@Test(expected=NullPointerException.class)
	public void testSearchFlightsNullResult() throws IllegalArgumentException {
		this.manager = new PackageManager(new FlightSearchNullMock(), new HotelSearchMock(), new DayTourSearchMock(), 
				new FlightReservationMock(), new HotelReservationMock(), new DayTourReservationMock());
		Date date;
		try {
			date = format.parse("15/04/2017");
			String code = "JFK"; 
			List<Flight> list = manager.searchFlights(date, code, true);
			list.size();
		} catch (ParseException e) {
			System.out.println("Parse exception");
		}
	}
	
	// Search with date as a null reference should produce an exception
	@Test(expected=IllegalArgumentException.class)
	public void testSearchFlightsNoDepartingDate() {
		
		this.manager = new PackageManager(new FlightSearchMock(), new HotelSearchMock(), new DayTourSearchMock(), 
				new FlightReservationMock(), new HotelReservationMock(), new DayTourReservationMock());
		
		Date date = null;
		this.manager.searchFlights(date, "JFK", true);
	}
	
	// Search with airport code as a null reference should produce an exception
	@Test(expected=IllegalArgumentException.class)
	public void testSearchFlightsWithNoCode() {
		
		this.manager = new PackageManager(new FlightSearchMock(), new HotelSearchMock(), new DayTourSearchMock(), 
				new FlightReservationMock(), new HotelReservationMock(), new DayTourReservationMock());
		
		Date departing;
		try {
			departing = format.parse("15/04/2017");
			this.manager.searchFlights(departing, "", true);
		} catch (ParseException e) {
			// Can't happen here...
			System.out.println("Parse exception");
		}
	}
	
	// Search with a non-existing airport code should produce an exception	
	@Test(expected=IllegalArgumentException.class)
	public void testSearchOutboundFlightsIllegalAirport() {
		
		this.manager = new PackageManager(new FlightSearchMock(), new HotelSearchMock(), new DayTourSearchMock(), 
				new FlightReservationMock(), new HotelReservationMock(), new DayTourReservationMock());
		
		Date departing;
		try {
			departing = format.parse("15/04/2017");
			// valid airport codes consist of 3 characters
			this.manager.searchFlights(departing, "LA", true);
		} catch (ParseException e) {
			// Can't happen here ...
			System.out.println("Parse exception");
		}
	}
	
	// Tests for searching hotels (identical tests for searching daytours not shown here)
	
	@Test // Normal search resulting in a nonempty list of hotels
	public void testSearchHotels() throws IllegalArgumentException {
		
		this.manager = new PackageManager(new FlightSearchMock(), new HotelSearchMock(), new DayTourSearchMock(), 
				new FlightReservationMock(), new HotelReservationMock(), new DayTourReservationMock());
		
		Date arrival, returning;
		try {
			arrival = format.parse("15/04/2017");
			returning = format.parse("15/05/2017");
			List<Hotel> list = manager.searchHotels(arrival, returning);
			assertNotNull(list);
		} catch (ParseException e) {
			System.out.println("Parse exception");
		}
	}
	
	// Search with at least one date as a null reference should produce an exception	
	@Test(expected=IllegalArgumentException.class)
	public void testSearchHotelsMissingDate() {
		
		this.manager = new PackageManager(new FlightSearchMock(), new HotelSearchMock(), new DayTourSearchMock(), 
				new FlightReservationMock(), new HotelReservationMock(), new DayTourReservationMock());
		
		Date arrival, returning;
		try {
			arrival = null;
			returning = format.parse("15/05/2017");
			this.manager.searchHotels(arrival, returning);
		} catch (ParseException e) {
			System.out.println("Parse exception");
		}
	}
	
	// Search with returning date that comes before the arrival date should produce an exception	
	@Test(expected=IllegalArgumentException.class)
	public void testSearchHotelsWrongOrderOfDates() {
		
		this.manager = new PackageManager(new FlightSearchMock(), new HotelSearchMock(), new DayTourSearchMock(), 
				new FlightReservationMock(), new HotelReservationMock(), new DayTourReservationMock());
		
		Date arrival, returning;
		try {
			arrival = format.parse("15/05/2017");
			returning = format.parse("15/04/2017");
			this.manager.searchHotels(arrival, returning);
		} catch (ParseException e) {
			System.out.println("Parse exception");
		}
	}
}
