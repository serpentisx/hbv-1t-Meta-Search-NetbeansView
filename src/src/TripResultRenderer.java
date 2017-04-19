package src;
import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import tour.DayTour;


/**
 *
 * @author npquy
 */
public class TripResultRenderer extends TripResult implements ListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus) {
        if (value == null) {
            return new JLabel();
        }

        setOpaque(false);

        TravelPackage p = (TravelPackage) value;
       
        getCustomerAddress().setText(p.getCustomer().getAddress());
        getCustomerEmail().setText(p.getCustomer().getEmail());
        getCustomerName().setText(p.getCustomer().getName());
        getCustomerPhone().setText(p.getCustomer().getPhone());
        
        int travellers = p.getTravellers();
        int flightCost = (p.getInbound().getPrice() + p.getOutbound().getPrice())*travellers;

        getFlight_totalPrice().setText(flightCost + " ISK");
        getInbound_airline().setText(p.getInbound().getAirline());
        getInbound_departure().setText(p.getInbound().getOrigin());
        getInbound_departureTime().setText(new SimpleDateFormat("EEEE, dd. MMMM YYYY - HH:mm", Locale.ENGLISH).format(p.getInbound().getDepartureTime()));
        getInbound_arrival().setText(p.getInbound().getDestination());
        getInbound_arrivalTime().setText(new SimpleDateFormat("EEEE, dd. MMMM YYYY - HH:mm", Locale.ENGLISH).format(p.getInbound().getArrivalTime()));
        getInbound_flightNo().setText(p.getInbound().getFlightNumber());

        getOutbound_airline().setText(p.getOutbound().getAirline());
        getOubound_departure().setText(p.getOutbound().getOrigin());
        getOutbound_departureTime().setText(new SimpleDateFormat("EEEE, dd. MMMM YYYY - HH:mm", Locale.ENGLISH).format(p.getOutbound().getDepartureTime()));
        getOutbound_arrival().setText(p.getOutbound().getDestination());
        getOutbound_arrivalTime().setText(new SimpleDateFormat("EEEE, dd. MMMM YYYY - HH:mm", Locale.ENGLISH).format(p.getOutbound().getArrivalTime()));
        getOutbound_flightNo().setText(p.getOutbound().getFlightNumber());

        Long diff = p.getInbound().getArrivalTime().getTime() - p.getOutbound().getDepartureTime().getTime();
        getHotelAddress().setText(p.getHotel().getAddress());
        getHotelName().setText(p.getHotel().getName());
        getHotelRating().setIcon(p.getHotel().getRatingIcon());
        getHotelPrice().setText(p.getHotel().getPrice() + " ISK");
        getHotel_nights().setText(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + " night(s)");
        getHotel_totalPrice().setText(p.getHotel().getPrice() * TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + " ISK");

        getPackage_price().setText("Package price: " + p.calculatePrice() + " ISK");

        DefaultListModel dm = new DefaultListModel();
        for (DayTour tour : p.getDayTours()) {
            String name = tour.getNameOfTrip();
            String date = tour.getDate();
            String price = Integer.toString(tour.getPrice()*travellers);

            String result = "<html> - " + name + "<br>" + date + "<br>" + price + " ISK" + "</html>";
            dm.addElement(result);
        }
        getDaytour_list().setModel(dm);
       
        return this;
    }
}
