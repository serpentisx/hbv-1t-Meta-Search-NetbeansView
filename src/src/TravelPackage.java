package src;
import java.util.Vector;
import hotel.*;
import tour.DayTour;
import flight.*;

import flight.Flight;

public class TravelPackage {
	
	private int price;
	private int travellers;
	private Flight outbound;
	private Flight inbound;
	private Hotel hotel;
	private Vector<DayTour> tours;
	Customer customer;
	
	/* Default constructor */

	/* Instance methods */
	public int getPrice() {
		return price;
	}

	public int getTravellers() {
		return travellers;
	}

	public Flight getOutbound() {
		return outbound;
	}

	public Flight getInbound() {
		return inbound;
	}

	public Hotel getHotel() {
		return hotel;
	}

	public Vector<DayTour> getDayTours() {
		return tours;
	}

	public Customer getCustomer() {
		return customer;
	}
	
	public int calculatePrice() {
		
		price =  outbound.getPrice() + inbound.getPrice();
		// price += hotel.getPrice();
		
		for(int i = 0; i < tours.size(); i++) {
			price += tours.get(i).getPrice();
		}
		
		return price;		
	}

	public void setTravellers(int travellers) {
		this.travellers = travellers;
	}

	public void setOutbound(Flight outbound) {
		this.outbound = outbound;
	}

	public void setInbound(Flight inbound) {
		this.inbound = inbound;
	}

	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}

	public void addDayTour(DayTour tour) {
		if(tour != null) {
			tours.add(tour);
		}
	}
	
	public void removeDayTour(DayTour tour) {
		int index = tours.indexOf(tour);
		if(index != -1) {
			tours.remove(index);
		}
	}
	
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
}
