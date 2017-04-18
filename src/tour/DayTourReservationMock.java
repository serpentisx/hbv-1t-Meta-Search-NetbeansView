package tour;
public class DayTourReservationMock implements DayTourReservation {
	
	public void book(DayTour tour) { System.out.println("Tour: " + tour.getNameOfTrip() + " reserved." ); }
}