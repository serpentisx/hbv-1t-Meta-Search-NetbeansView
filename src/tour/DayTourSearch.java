package tour;
import java.util.Date;
import java.util.List;

public interface DayTourSearch {
	
	public List<DayTour> search(Date departing, Date returning, int num);
	
}
