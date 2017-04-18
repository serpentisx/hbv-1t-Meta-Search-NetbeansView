package hotel;

import java.time.*;
import java.util.Comparator;

public class RoomsPerDay {
	private int rpdID;
	private LocalDate day;
	private int availableRooms;

	public RoomsPerDay(){
		this.availableRooms = 0;
	}
	
	public RoomsPerDay(RoomsPerDay rpd){
		this.day = rpd.getDay();
		this.availableRooms = rpd.getAllAvailableRooms();
	}
	
	public RoomsPerDay(LocalDate day, int availableRooms){
		this.day = day;
		this.availableRooms = availableRooms;
	}

	public RoomsPerDay(int ID, LocalDate day, int availableRooms){
		this.rpdID = ID;
		this.day = day;
		this.availableRooms = availableRooms;
	}
	
	
	  /*Comparator for sorting the list by Date*/
   public static Comparator<RoomsPerDay> RoomDaycomparator = new Comparator<RoomsPerDay>() {

		public int compare(RoomsPerDay r1, RoomsPerDay r2) {
		   LocalDate d1 = r1.getDay();
		   LocalDate d2 = r2.getDay();
		   int c = 0;
		   if(d1.isBefore(d2))
		   	c = -1;
		   else if(d1.isAfter(d2))
		   	c = 1;
		   else if(d1.isEqual(d2))
		   	c = 0;
		   
		   return c;
	   }
		
	};

	
   public void decreaseAvailableRoomsBy(int amount)
   {
   	if( amount >= 0 )
   	availableRooms -= amount;
   }
   
   public void increaseAvailableRoomsBy(int amount)
   {
   	if( amount >= 0)
   		availableRooms += amount;
   }
   
	public LocalDate getDay() {
		return day;
	}

	public void setDay(LocalDate day) {
		this.day = day;
	}

	public int getAllAvailableRooms(){
		return availableRooms;
	}
	
	public int getAllAvailableRooms(int roomType){
		return availableRooms;
		}

	public void setAvailableRooms(int roomType, int number){
		//Ignoring roomType for now
		this.availableRooms = number;
	}

	public int getRpdID() {
		return rpdID;
	}

	public void setRpdID(int rpdID) {
		this.rpdID = rpdID;
	}
}
