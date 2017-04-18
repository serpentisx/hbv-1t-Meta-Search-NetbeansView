package hotel;


import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;

public class Hotel {
	private int hotelID = -1;
	private String name;
	private boolean[] priceRange;
	private boolean[] openingMonths;
	private String address;
	private ArrayList<RoomsPerDay> freeRoomsPerDate = new ArrayList<RoomsPerDay>();
	private ArrayList<Integer> roomIDs = new ArrayList<Integer>();
	private int rating;
	private boolean[] roomFacilities;
	private int hotelType;
	private boolean[] hotelFacilities;
	private int hotelLocation;
	private String nearestCity;
	private String nearestAirport;
	private String nearestSite;
	private String nearbyDayTour;
	
	public Hotel(int hotelID){
		this.hotelID = hotelID;
	}
	
	public Hotel(int hotelID, String name, boolean[] priceRange, boolean[] openingMonths, String address, 
			ArrayList<RoomsPerDay> freeRoomsPerDate, int rating, boolean[] roomFacilities, int hotelType,
			boolean[] hotelFacilities, int hotelLocation, String nearestCity, String nearestAirport,
			String nearestSite, String nearbyDayTour)
	{
		this.hotelID = hotelID;
		this.name = name;
		this.priceRange = priceRange;
		this.openingMonths = openingMonths;
		this.address = address; 
		this.setFreeRoomsPerDate(freeRoomsPerDate);
		this.rating = rating;
		this.roomFacilities = roomFacilities;
		this.hotelType = hotelType;
		this.hotelFacilities = hotelFacilities;
		this.hotelLocation = hotelLocation;
		this.nearestCity = nearestCity;
		this.nearestAirport = nearestAirport;
		this.setNearestSite(nearestSite);
		this.setNearbyDayTour(nearbyDayTour);
	}
	public Hotel(){}

	public int numberofRoomDays(){
		return freeRoomsPerDate.size();
	}
	
	public int checkAvailability(LocalDate startDate, LocalDate endDate){
		if(endDate.isBefore(startDate))
		{
			//Enddate was before startdate
			return 0;
		}		
		long numberOfDays = ChronoUnit.DAYS.between(startDate, endDate)+1;
		int maxPossibleRooms = 0;
		int currentPossibleRooms = 0;
		boolean foundFirstDay = false;
		int dayOfTrip = 0;
		Collections.sort(freeRoomsPerDate, RoomsPerDay.RoomDaycomparator);
		for(RoomsPerDay e : freeRoomsPerDate)
		{
			/*Let's iterate through the entire (sorted) list until we find
			 * the first day the user searched for. If we find it, we're going
			 * to break the for() loop at some point. If we didn't find it,
			 * then startDay didn't even exist in our list, and we can
			 * safely return 0 for max number of guests. */
			LocalDate thatDay = e.getDay();
			if(thatDay.isEqual(startDate))
			{
				/*Okay, our hotel at least has an entry for the very first day.
				 * Let's iterate through them and find the lowest possible number
				 * for any given day. REMEMBER that we cannot guarantee we'll have
				 * database entries for each day - this class isn't in charge of the
				 * raw data - so we have to compare days at every step. If the comparison
				 * ever fails then our table was missing a day, and that means
				 * we can return 0 for the whole trip.*/
				foundFirstDay = true;
				dayOfTrip = 0;
				maxPossibleRooms = e.getAllAvailableRooms();
				if(numberOfDays==1) return maxPossibleRooms; //One-day stay
			}
			if( (thatDay.isAfter(startDate)) && (thatDay.isBefore(endDate.plusDays(1))) )
				/*We're in the date range we wanted to check*/
			{
				if( !(foundFirstDay) )
					/* We've hit values in our sorted list that are later than our starting date,
					 * but we never found an entry for the starting date itself. This means our 
					 * hotel's list doesn't contain an entry for that day, so obviously it can't
					 * offer our guests anything for the time period they requested.*/
					return 0;

				dayOfTrip++;
				LocalDate tripDay = startDate.plusDays(dayOfTrip);
				if( !(tripDay.isEqual(thatDay)))
					/*There was a missing entry in the hotel's list of days, so again,
					 * we can't offer them a valid number for this time period*/
					return 0;
				
				currentPossibleRooms = e.getAllAvailableRooms();
				if( currentPossibleRooms<maxPossibleRooms )
					maxPossibleRooms = currentPossibleRooms;
			}
			if( (thatDay.isAfter(endDate)) )
				/* (We don't need this condition here, by the way, but it'll save us having to
				 * cyce through every single remaining entry in freeRoomsPerDate)
				 * We've found the first day, passed through a valid date range with no dates
				 * missing, and adjusted the maximum possible room number accordingly.
				 * Now we've passed the final date, so we don't need to look at any more
				 * entries, and can simply return maxPossibleRooms.
				 * Remember that we set it to 0 initially, so even if some weird system
				 * quirk forces us into this if loop without having met the proper conditions, 
				 * we'll still just be returning a perfectly valid 0.
				 * */
				return maxPossibleRooms;
	
		}
		return maxPossibleRooms; //And just for good measure
			
	}
	

	public boolean isEqual(Hotel hotel)
	{
		/*HotelID is unique, so that's the only value we need to match.*/
		if(this.hotelID == hotel.getHotelID())
			return true;
		else
			return false;
	}

	public ArrayList<Integer> decreaseAvailability(LocalDate startDate, LocalDate endDate, int numberOfGuests)
	{
		roomIDs.clear();		
		Collections.sort(freeRoomsPerDate, RoomsPerDay.RoomDaycomparator);
		/*Note: since we've implemented a comparator, we _could_ add extra code so that
		 * we only cycle through the dates between start and end, similar to how it's 
		 * done in checkAvailability(), rather than cycling through every single
		 * freeRoomsPerDate object. Since we don't have a ton of entries, it's fine
		 * this way, but if we ever add a bunch of new dates, we might want to
		 * implement that extra code so that we decrease the runtime of this function.*/
		LocalDate dayBeforeStart = startDate.minusDays(1);
		LocalDate dayAfterEnd = endDate.plusDays(1);
		for(RoomsPerDay e : freeRoomsPerDate)
		{
			LocalDate checkingDay = e.getDay();
			if( (checkingDay.isAfter(dayBeforeStart)) && (checkingDay.isBefore(dayAfterEnd)) )
			{
				e.decreaseAvailableRoomsBy(numberOfGuests);
				roomIDs.add(e.getRpdID());
			}
		}
		
		return roomIDs; //We return these to the Control layer so it can update the corresponding DB entries
	}
	
	public void decreaseAvailability(LocalDate thisDate, int numberOfGuests)
	{
		//UNIMPLEMENTED. Including it mainly because it was part of the original design
	}
	
	
	private RoomsPerDay findRoomDay(LocalDate date){
		for(RoomsPerDay e : freeRoomsPerDate)
		{
			System.out.println("e.getDay(): "+e.getDay());
			System.out.println("LocalDate date: "+date);
			if(e.getDay() == date)
				return e;
		}
		return null;
	}
	
	public int getHotelID() {
		return hotelID;
	}
	public void setHotelID(int hotelID) {
		this.hotelID = hotelID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean[] getPriceRange() {
		return priceRange;
	}
	public void setPriceRange(boolean[] priceRange) {
		this.priceRange = priceRange;
	}
	public boolean[] getOpeningMonths() {
		return openingMonths;
	}
	public void setOpeningMonths(boolean[] openingMonths) {
		this.openingMonths = openingMonths;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public boolean[] getRoomFacilities() {
		return roomFacilities;
	}
	public void setRoomFacilities(boolean[] roomFacilities) {
		this.roomFacilities = roomFacilities;
	}
	public int getHotelType() {
		return hotelType;
	}
	public void setHotelType(int hotelType) {
		this.hotelType = hotelType;
	}
	public boolean[] getHotelFacilities() {
		return hotelFacilities;
	}
	public void setHotelFacilities(boolean[] hotelFacilities) {
		this.hotelFacilities = hotelFacilities;
	}
	public String getNearestCity() {
		return nearestCity;
	}
	public void setNearestCity(String nearestCity) {
		this.nearestCity = nearestCity;
	}
	public String getNearestAirport() {
		return nearestAirport;
	}
	public void setNearestAirport(String nearestAirport) {
		this.nearestAirport = nearestAirport;
	}
	public int getHotelLocation() {
		return hotelLocation;
	}
	public void setHotelLocation(int hotelLocation) {
		this.hotelLocation = hotelLocation;
	}
	public String getNearestSite() {
		return nearestSite;
	}
	public void setNearestSite(String nearestSite) {
		this.nearestSite = nearestSite;
	}
	public String getNearbyDayTour() {
		return nearbyDayTour;
	}
	public void setNearbyDayTour(String nearbyDayTour) {
		this.nearbyDayTour = nearbyDayTour;
	}

	public ArrayList<RoomsPerDay> getFreeRoomsPerDate() {
		return freeRoomsPerDate;
	}
	public void setFreeRoomsPerDate(ArrayList<RoomsPerDay> fr) {
		//System.out.println("Got into setFreeRoomsPerDate for "+this.getName()+" and am about to add "+fr.size()+" rooms");
		freeRoomsPerDate = new ArrayList<RoomsPerDay>(fr);
		//System.out.println("freeRoomsPerDate for "+this.getName()+" now contains "+freeRoomsPerDate.size()+" elements.");
		//this.freeRoomsPerDate = freeRoomsPerDate;
	}

	

}
