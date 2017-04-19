package hotel;
import java.util.Map;
import java.util.HashMap;
import java.util.Observable;
import java.awt.event.*;
import java.time.LocalDate;

public class ReservationManager implements ActionListener {
	HotelManager hotelManager;
	int tempint = 0;
	private Hotel hotel;
	private LocalDate startDate, endDate;
	private int numberOfGuests;
	private Reservation newReservation;
	private ReservationStorage allReservations = new ReservationStorage();
	
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		System.out.println("Caught actionperformed");
		//System.out.println (e.getActionCommand());
	}
	


	public void addHotelManager(HotelManager hm)
	{
		this.hotelManager = hm;
	}
	
	public int addReservation(Hotel hotel, LocalDate startDate, LocalDate endDate, int numberOfGuests)
	{
		int reservationID = -1;
		this.hotel = hotel;
		this.startDate = startDate;
		this.endDate = endDate;
		this.numberOfGuests = numberOfGuests;
		
		newReservation = new Reservation(this.hotel, this.startDate, this.endDate, this.numberOfGuests);
		reservationID = allReservations.insert(newReservation);
		hotelManager.reserveRoomsForConfirmedReservation(hotel, startDate, endDate, numberOfGuests);
		
		return reservationID;
	}
	
	
	public int writeTempint() {
		return tempint;
	}
}
