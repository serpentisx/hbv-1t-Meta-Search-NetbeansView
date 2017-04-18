package src;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import flight.Flight;
import flight.FlightGenerator;
import flight.FlightReservation;
import hotel.*;
import tour.*;

public class PackageManager {
	
	private TravelPackage packageInMaking;
	// private Vector<String> codes; // all valid airport codes
	private FlightGenerator fsearch;
	private HotelManager hsearch;
	private DayTourSearch dtsearch;
	private FlightReservation fres;
	private ReservationManager hres; // hotel reservation manager
	private DayTourReservation dtres;
		
	public PackageManager(FlightGenerator fsearch, HotelManager hsearch, DayTourSearch dtsearch, 
			FlightReservation fres, ReservationManager hres, DayTourReservation dtres) {
		this.packageInMaking = new TravelPackage();
		this.fsearch = fsearch;
		this.hsearch = hsearch;
		this.dtsearch = dtsearch;
		this.fres = fres;
		this.hres = hres;
		this.dtres = dtres;
		// this.codes = generateCodes();
	}	
	
	public TravelPackage getPackage() {
		return packageInMaking;
	}
	
	public FlightGenerator getFlightGenerator() {
		return fsearch;
	}
	
	public void bookPackage() {
		fres.book(packageInMaking.getInbound());
		fres.book(packageInMaking.getOutbound());
		
		LocalDate arrival = packageInMaking.getOutbound().getArrivalTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate returning = packageInMaking.getInbound().getDepartureTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		int travellers = packageInMaking.getTravellers();		
		hres.addReservation(packageInMaking.getHotel(), arrival, returning, travellers);
		
		for(DayTour tour : packageInMaking.getDayTours()) {
			dtres.book(tour);
		}
	}
	
	/* 
	 * If @param inbound is true, this method searches for inbound flights from
	 * KEF airport to the destination specified by @param airportCode. 
	 * Otherwise the method searches for outbound flights from the origin specified
	 * by @param airportCode to KEF airport
	 */
	public List<Flight> searchFlights(Date departureDate, String origin, String destination) {
		int travellers = packageInMaking.getTravellers();
		if(departureDate == null) {
			//throw new IllegalArgumentException("Please select both the dates of arrival and return");
		}
		return fsearch.search(departureDate, origin, destination, travellers);
	}	
	
	public ArrayList<Hotel> searchHotels(Date arrivalDate, Date returningDate) {
		int travellers = packageInMaking.getTravellers();
		if(arrivalDate == null || returningDate == null) {
			throw new IllegalArgumentException("Please select both the dates of arrival and return");
		} else if(returningDate.before(arrivalDate)) {
			throw new IllegalArgumentException("Date of returning must follow date of arrival.");
		}
		LocalDate start = arrivalDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end = returningDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		return hsearch.searchHotel(start, end, travellers);
	}
	
	public List<DayTour> searchDayTours(Date arrivalDate, Date returningDate) {
		if(returningDate.before(arrivalDate)) {
			throw new IllegalArgumentException("Date of returning must follow date of arrival.");
		} else if(arrivalDate == null || returningDate == null) {
			throw new IllegalArgumentException("Please select both the dates of arrival and return");
		}
		return dtsearch.search(arrivalDate, returningDate, getPackage().getTravellers());		
	}
	
	/*
	private static Vector<String> generateCodes() {
		Vector<String> codes = new Vector<>();
		try (BufferedReader br = new BufferedReader(new FileReader(new File("airportCodes.txt")))) {
			String line; String[] s;
		    while ((line = br.readLine()) != null) {
		       s = line.split(",");		       
		       if(s[4].length() == 5) {
		    	   codes.add(s[4].substring(1, 4));
		       }
		    }
		} catch (FileNotFoundException e) {
			System.out.println("File containing airport codes was not found");
		} catch (IOException e) {
			System.out.println("Reading airport codes resulted in an IOException");
		}
	    return codes;
	}*/

}
