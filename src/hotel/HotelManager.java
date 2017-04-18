package hotel;
import java.time.LocalDate;
import java.util.ArrayList;
public class HotelManager
{
	@SuppressWarnings("unused")
	private SearchView sv;
	@SuppressWarnings("unused")
	private ResultsView rv;
	private ArrayList<Hotel> hotels;
	private ArrayList<Hotel> searchedHotels = new ArrayList<Hotel>();
	private ArrayList<Integer> rpdIDs = new ArrayList<Integer>();
	private HotelStorage allHotels;
	private ReservationManager reservationManager;
	
	public HotelManager()
	{	}
	
	/* Header: This HotelManager(b) constructor runs loadAllHotels() if b==true
	 * We needed this extra constructor because sometimes we want to create a new,
	 * temporary HotelManager without loading all the data (f.ex. in ReservationView).
	 * If we load the data every single time we initialize the constructor, we start
	 * running into trouble: The ResultSet contains double entries, some of them are
	 * null values, etc.
	 * So we call "new HotelManager(true)" if and only if we want it to load
	 * all the date from our database (basically, in the main function in
	 * ProgramManager). Anywhere else, such as in our views, we simply call
	 * "new HotelManager()" or "HotelManager m;"
	 */	
	public HotelManager(boolean initialize){
		if(initialize){
			allHotels = new HotelStorage();
			loadAllHotels();
		}
}
	

	public void addSearchView(SearchView sv)
	{
		this.sv = sv;
	}
	public void addResultsView(ResultsView rv)
	{
		this.rv = rv;
	}
	public void addReservationManager(ReservationManager rm)
	{
		this.reservationManager = rm;
	}
	
	private void toBool(ArrayList o, boolean[] b)
	{
		for(int i = 0; i<o.size(); i++)
			b[i] = Boolean.valueOf((o.get(i)).toString());
	}
	


	/* Header: searchHotel(p) looks for all hotels matching p (which must be size & capacity 16).
	 *	The ArrayList called "parameters" should be of length 16. It contains all possible search conditions:
	 * (0) start date (LocalDate) 
	 * (1) end date (LocalDate)
	 * (2) number of guests (int)
	 * (3) hotel name (String)
	 * (4) price range (boolean[], length 5)
	 * (5) opening months (boolean[], length 12)
	 * (6) address (String) 
	 * (7) ratings (boolean[], length 5)
	 * (8) room facilities (boolean[], length 6)
	 * (9) hotel type (boolean[], length 5)
	 * (10) hotel facilities (boolean[], length 6)
	 * (11) hotel area location (boolean[], length 6 - is eventually matched to a hotel's 3-digit int postal code)   
	 * (12) nearest city (boolean[], length 5)
	 * (13) nearest airport (boolean[], length 6)
	 * (14) nearest sites (boolean[], length 8)
	 * (15) nearest day tours (boolean[], length 6) 
	 * 
	 * If a search condition was not specified by the user (meaning the user doesn't care about
	 * that particular condition - location, pricing, rating, etc.) then its corresponding position
	 * in our ArrayList will hold a NULL value, and will be ignored in our search.
	 *    The only exceptions to this are the start date, end date and number of guests: 
	 *    If any of these are NULL, our search will simply return no results 
	 *    (rather than throw exceptions and destabilize the entire program).  
	 * 
	 * Also, since we've already loaded all our hotels into the "hotels" ArrayList, we can keep all
	 * the code here - rather than having to descend into HotelStorage and perform a bunch of
	 * horrible, liable-to-break SQL conversions there.
	 */
	public ArrayList<Hotel> searchHotel(ArrayList parameters)
	{
		boolean match = false;
		searchedHotels.clear();
		
		/* First, let's make sure we got the number of parameters we need.
		 * If not, we return an empty search array right away.*/
		if(parameters.size() < 16)
			return searchedHotels;
		
		for(Hotel h : hotels){
			/*Let's assume this hotel is a match until we find out otherwise*/
			match = true;
			
			/*Also, we _could_ run all these checks with lines like
			 * 	if( (!(parameters.get(0) == null)) && 	(!(parameters.get(0).equals(h.getName()))) )
			 * but that kind of code is really cryptic. So let's use temporary variables
			 * instead, for each of the parameters.*/

			//Parameters 0, 1 and 2
			//startDate, endDate, and number of guests
			LocalDate startDate = (LocalDate)parameters.get(0);
			LocalDate endDate = (LocalDate)parameters.get(1);
			int numberOfGuests = (int)parameters.get(2);
			if( (startDate == null)	|| (endDate == null) || (numberOfGuests < 1) )
				match = false;
			if(numberOfGuests > h.checkAvailability(startDate, endDate))
				match = false;

			//Parameter 3: name
			/* Yes, we're using "!=" in a string comparison, instead of the ".equals()" method. 
			 * That's because we're first checking whether the string's _pointer_ is set to null,
			 * because if so, the second condition of our "if" clause - the one that might be
			 * calling a method on an object set to null - could cause a NullPointerException. 
			 */
			String name = (String)parameters.get(3);
			if( (name != null) && !(name.equals(h.getName())) )
					match = false;
			
			//Parameter 4: pricing options
			boolean[] hotelPrices = h.getPriceRange();
			boolean[] searchPrices = (boolean[])(parameters.get(4));
			if (!findMatch(hotelPrices, searchPrices))
				match = false;
						
			//Parameter 5: opening months
			boolean[] hotelOpeningMonths = h.getOpeningMonths();
			boolean[] searchOpeningMonths = (boolean[])(parameters.get(5));
			//if ((searchOpeningMonths != null) && (Arrays.asList(hotelOpeningMonths).contains(true)))
			if (!findMatch(hotelOpeningMonths, searchOpeningMonths))
				match = false;
			
			//Parameter 6: address
			String address = (String)parameters.get(6);
			if( (address != null) && (!address.equals("")) && (!address.equals(h.getAddress())) )
					match = false;
			
			//Eight parameter: rating
			/* Let's be a little careful here. A hotel's rating is a single int value. But the
			 * user is allowed to search for more than one rating option. So what we get is a
			 * boolean array of possible ratings, and we're going to match our hotel's rating
			 * to the value in the array's corresponding _position_. For example, if our hotel
			 * is rated 5 stars out of 5, we check whether array[4] is '1' (it's [4] rather than
			 * [5] because arrays are zero-indexed).  
			 *   
			 * This way, we also get around the parameter complication that int values can't 
			 * be NULL; it doesn't affect us, because boolean arrays can.
			 */
			boolean[] hotelRatings = new boolean[5];
			hotelRatings[h.getRating()-1] = true;
			boolean[] searchRatings = (boolean[])(parameters.get(7));
			if (!findMatch(hotelRatings, searchRatings))
				match = false;
			
			//Ninth parameter: room facilities
			boolean[] hotelRoomFacilities = h.getRoomFacilities();
			boolean[] searchRoomFacilities = (boolean[])(parameters.get(8));
			if (!findMatch(hotelRoomFacilities, searchRoomFacilities))
					match = false;
			
			//Tenth parameter: hotel type
			boolean[] hotelTypes = new boolean[5];
			hotelTypes[h.getHotelType()-1] = true;
			boolean[] searchTypes = (boolean[])(parameters.get(9));
			if(!findMatch(hotelTypes, searchTypes))
				match = false;
		
			//Eleventh parameter: hotel facilities
			boolean[] hotelFacilities = h.getHotelFacilities();
			boolean[] searchFacilities = (boolean[])(parameters.get(10));
			if (!findMatch(hotelFacilities, searchFacilities))
				match = false;
			
			//Twelth parameter: hotel area location
			int hotelLocation = h.getHotelLocation();
			boolean[] searchLocations = (boolean[])(parameters.get(11));
			if(!allZeroes(searchLocations))
			{
				boolean foundLocation = false;
				
				/* This is a little cryptic, but it means we could later create
				 * a far more granular search for individual postal codes.
				 * A hotel's location is an int representing a postal code. 
				 * Our search, however, only covers binary selections of
				 * particular regions. So we use official region divisions 
				 * from the Icelandic Postal Authority to check if this 
				 * particular hotel's postal code falls within the range 
				 * of at least one of the user's selections.
				 * 
				 * Also, it would be wonderful if a switch statement could be
				 * made to work with more than one condition.
				 */

				if( (searchLocations[0]) && (hotelLocation>=101) && (hotelLocation<=276) )
					foundLocation=true;
				
				else if( (searchLocations[1]) && (800<=hotelLocation) && (hotelLocation<=902))
					foundLocation=true;
			
				else if( (searchLocations[2]) && (700<=hotelLocation) && (hotelLocation<=785))
					foundLocation=true;
			
				else if( (searchLocations[3]) && (500<=hotelLocation) && (hotelLocation<=690))
					foundLocation=true;
			
				else if( (searchLocations[4]) && (300<=hotelLocation) && (hotelLocation<=380))
					foundLocation=true;
			
				else if( (searchLocations[5]) && (400<=hotelLocation) && (hotelLocation<=471))
					foundLocation=true;
			
				if(!foundLocation)
					match = false;
			}
			
			
			//Thirteenth parameter: nearest city
			String hotelCity = h.getNearestCity();
			boolean[] cityLocations = (boolean[])(parameters.get(12));
			if(!allZeroes(cityLocations))
			{
				boolean foundCity = false;
				if( (cityLocations[0]) && (hotelCity.equals("Reykjavík")) )
					foundCity=true;
				
				if( (cityLocations[1]) && (hotelCity.equals("Keflavík")) )
					foundCity=true;
			
				if( (cityLocations[2]) && (hotelCity.equals("Akureyri")) )
					foundCity=true;
			
				if( (cityLocations[3]) && (hotelCity.equals("Egilsstaðir")) )
					foundCity=true;
			
				if( (cityLocations[4]) && (hotelCity.equals("Ísafjörður")) )
					foundCity=true;
			
				if(!foundCity)
					match = false;
			}	
			
			//Fourteenth parameter: nearest airport
			String hotelAirport = h.getNearestAirport();
			boolean[] airportLocations = (boolean[])(parameters.get(13));
			if(!allZeroes(airportLocations))
			{
				boolean foundAirport = false;
				
				if( (airportLocations[0]==true) && (hotelAirport.equals("KEF")) )
					foundAirport=true;
			
				if( (airportLocations[1]==true) && (hotelAirport.equals("RKV")) )
					foundAirport=true;
			
				if( (airportLocations[2]==true) && (hotelAirport.equals("AEY")) )
					foundAirport=true;
			
				if( (airportLocations[3]==true) && (hotelAirport.equals("EGS")) )
					foundAirport=true;
			
				if( (airportLocations[4]==true) && (hotelAirport.equals("IFJ")) )
					foundAirport=true;
			
				if(!foundAirport)
					match = false;
			
			}	
			
			//Fifteenth parameter: nearest site of interest
			String hotelSite = h.getNearestSite();
			boolean[] siteLocations = (boolean[])(parameters.get(14));
			if(!allZeroes(siteLocations))
			{
				boolean foundSite = false;

				if( (siteLocations[0]==true) && (hotelSite.equals("Gullfoss")) )
					foundSite=true;
				
				if( (siteLocations[1]==true) && (hotelSite.equals("Geysir")) )
					foundSite=true;
				
				if( (siteLocations[2]==true) && (hotelSite.equals("Blue Lagoon")) )
					foundSite=true;
				
				if( (siteLocations[3]==true) && (hotelSite.equals("Þingvellir")) )
					foundSite=true;
				
				if( (siteLocations[4]==true) && (hotelSite.equals("Jökulsárlón")) )
					foundSite=true;
				
				if( (siteLocations[5]==true) && (hotelSite.equals("Dettifoss")) )
					foundSite=true;
				
				if( (siteLocations[6]==true) && (hotelSite.equals("Vatnajökull")) )
					foundSite=true;
				
				if( (siteLocations[7]==true) && (hotelSite.equals("Jólahúsið")) )
					foundSite=true;
				
				if(!foundSite)
					match = false;
			
			}	
			
	
			//Sixteenth parameter: nearest day tour
			String tour = h.getNearbyDayTour();
			boolean[] tourLocations = (boolean[])(parameters.get(15));
			if(!allZeroes(tourLocations))
			{
				boolean foundTour = false;
				
				if( (tourLocations[0]==true) && (tour.equals("Hestaferð")) )
					foundTour=true;
				
				if( (tourLocations[1]==true) && (tour.equals("Söguganga")) )
					foundTour=true;
				
				if( (tourLocations[2]==true) && (tour.equals("Fjallganga")) )
					foundTour=true;
				
				if( (tourLocations[3]==true) && (tour.equals("Skíðaferð")) )
					foundTour=true;
				
				if( (tourLocations[4]==true) && (tour.equals("Þingvallahringur")) )
					foundTour=true;
				
				if( (tourLocations[5]==true) && (tour.equals("Reiðhjólatúr")) )
					foundTour=true;
				
				if(!foundTour)
					match = false;
			
			}	
			
			if(match)
				searchedHotels.add(h);
		}
		
		return searchedHotels;
	}

	private boolean findMatch(boolean[] hotel, boolean[] searchCriteria)
	{
		boolean allZeroes = true;
		boolean foundMatch = false;
		for(int i=0; i<hotel.length; i++)
		{
			if(searchCriteria[i]) //Since this tests for true, it takes care of arrays that either are all false or all null
				allZeroes = false;
   		if( hotel[i] && searchCriteria[i] )
   			foundMatch = true;
		}
		if(allZeroes)
			return allZeroes;
		else
			return foundMatch;
	}
	
	private boolean allZeroes(boolean[] searchCriteria)
	{
		boolean empty = true;
		for(boolean b : searchCriteria)
		{
			if(b)
				empty = false;
		}
		return empty;
	}
	
	private void loadAllHotels()
	{
		this.hotels = (ArrayList<Hotel>)(allHotels.selectAll());
	}

	public int hotelCount(){
	return hotels.size();
	}

	public void reserveRoomsForConfirmedReservation(Hotel hotel, LocalDate startDate, LocalDate endDate, int numberOfGuests)
	/* We would make this function protected if it were only our program - it's usually called by
	 * ReservationManager, which is in the same Control package as HotelManager, and which only calls
	 * this function once it has made a valid reservation. But since this class need to provide
	 * outside access, we're making the function public. */
	{
		rpdIDs.clear();
		for(Hotel h : hotels)
		{
			if(h.isEqual(hotel)) //This will just be one hotel
			{
				ArrayList<Integer> tempRooms = h.decreaseAvailability(startDate, endDate, numberOfGuests);
				allHotels.updateRoomAvailability(h, tempRooms, numberOfGuests);
			}
		}
	}
	
	public ArrayList<Hotel> searchHotel(LocalDate startDate, LocalDate endDate, int guests)
	{
		searchedHotels.clear();
		for(Hotel h : hotels){
			if(guests <= h.checkAvailability(startDate, endDate))
			{
				searchedHotels.add(h);
			}
		}
		return searchedHotels;
	}
	
	
//Below is a searchHotel method that takes in Date objects, not LocalDate
//Shown for reference in case we need to implement the Date->Localdate
//conversion again, for example if anyone outside 1H needs it
/*
	public ArrayList<Hotel> searchHotel(Date startDate, Date endDate, int guests)
{
	searchedHotels.clear();
	LocalDate sD = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	LocalDate eD = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	for(Hotel h : hotels){
		if(guests <= h.checkAvailability(sD, eD))
			searchedHotels.add(h);
	}
	return searchedHotels;
}
*/

	}

