package hotel;
import java.time.LocalDate;

public class Reservation {


	private int reservationID;
	private Hotel hotel;
	private User user;
	private LocalDate startDate;
	private LocalDate endDate;
	private int numberOfPeople;
	private int[] numberOfRoomsByType;
	private boolean hasBeenPaid;
	
	
	public Reservation()
	{
		
	}
	
	public Reservation(Hotel hotel, LocalDate startDate, LocalDate endDate, int numberOfGuests)
	{
		this.hotel = hotel;
		this.startDate = startDate;
		this.endDate = endDate;
		this.numberOfPeople = numberOfGuests;
	}
	
	
	
	public int getReservationID() {
		return reservationID;
	}
	public void setReservationID(int reservationID) {
		this.reservationID = reservationID;
	}
	public Hotel getHotel() {
		return hotel;
	}
	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	public LocalDate getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	public int getNumberOfPeople() {
		return numberOfPeople;
	}
	public void setNumberOfPeople(int numberOfPeople) {
		this.numberOfPeople = numberOfPeople;
	}
	public int[] getNumberOfRoomsByType() {
		return numberOfRoomsByType;
	}
	public void setNumberOfRoomsByType(int[] numberOfRoomsByType) {
		this.numberOfRoomsByType = numberOfRoomsByType;
	}
	public boolean isHasBeenPaid() {
		return hasBeenPaid;
	}
	public void setHasBeenPaid(boolean hasBeenPaid) {
		this.hasBeenPaid = hasBeenPaid;
	}
	
	
	
}
