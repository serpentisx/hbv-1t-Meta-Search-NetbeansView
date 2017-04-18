package flight;
public class FlightReservationMock implements FlightReservation {
	
	public void book(Flight f) { System.out.println("Flight: " + f.getFlightNumber() + " reserved."); }
}
