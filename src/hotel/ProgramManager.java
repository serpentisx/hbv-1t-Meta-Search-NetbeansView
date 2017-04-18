package hotel;
import java.util.Map;
import java.util.HashMap;
import java.awt.EventQueue;

public class ProgramManager {

	public static void main(String[] args) {

		SearchView searchView = new SearchView();
		ResultsView resultsView = new ResultsView();
		ReservationView reservationView = new ReservationView();
		
		HotelManager hotelManager = new HotelManager(true);
		ReservationManager reservationManager = new ReservationManager();

		reservationView.setVisible(false);
		searchView.setVisible(true);
		
		reservationManager.addReservationView(reservationView);
		reservationView.addReservationManager(reservationManager);
		
		hotelManager.addResultsView(resultsView);
		resultsView.addHotelManager(hotelManager);
		
		hotelManager.addSearchView(searchView);
		searchView.addHotelManager(hotelManager);
		
		searchView.addResultsView(resultsView);
		resultsView.addSearchView(searchView);
		
		resultsView.addReservationView(reservationView);
		reservationView.addResultsView(resultsView);
		
		hotelManager.addReservationManager(reservationManager);
		reservationManager.addHotelManager(hotelManager);
		

	}

	
}
