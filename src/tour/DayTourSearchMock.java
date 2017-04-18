package tour;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.princeton.cs.algs4.In;

public class DayTourSearchMock implements DayTourSearch {

	@Override
	public List<DayTour> search(Date arriving, Date returning, int num) {
		In in = new In("src/tours.txt");
		SimpleDateFormat format = new SimpleDateFormat("ddMMyyyy");
		String[] array = in.readAllLines();
		
		List<DayTour> tours = new ArrayList<>();
		
		for(String s : array) {
			String[] info = s.split(";");
			String name = info[0];
			int dur = Integer.parseInt(info[1]);
			String loc = info[2];
			String time = info[3];
			String date = info[4];
			int seats = Integer.parseInt(info[5]);
			int price = Integer.parseInt(info[6]);
			try {
				Date d = format.parse(date);
				if(d.before(returning) && d.after(arriving)) {
					DayTour tour = new DayTour(name, dur, loc, time, date, seats, price);
					tours.add(tour);
				}
			} catch (ParseException e1) {
				System.out.println("Parse exception");
				e1.printStackTrace();
			}
		} 		
		return tours;
	}
	
	public static void main(String[] args) {
		DayTourSearchMock mock = new DayTourSearchMock();
		List<DayTour> list = mock.search(new Date(), new Date(), 1);
		for(DayTour t : list) {
			System.out.println("Location: "+t.getDate());
		}
	}
	
}
