package src;

import java.awt.Color;
import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Locale;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import flight.Flight;

public class FlightResultRenderer extends FlightResult implements ListCellRenderer {
    
 
    @Override
    public Component getListCellRendererComponent (JList list, Object value, int index, boolean isSelected, boolean hasFocus) {
        if (value == null) return new JLabel();
        
        if (index % 2 == 0) {
            setBackground(new Color(230, 230, 230));
        }
        else {
            setBackground(new Color(220, 220, 220));
        }
        
        if (isSelected) {
            setBackground(new Color(180, 200, 190));
            setForeground(list.getForeground());
        }
        
        Flight flight = (Flight) value;
        String departureTime = new SimpleDateFormat("HH:mm").format(flight.getDepartureTime());
        String arrivalTime = new SimpleDateFormat("HH:mm").format(flight.getArrivalTime());
        
        getWeekday().setText(new SimpleDateFormat("EEE", Locale.ENGLISH).format(flight.getDepartureTime()));
        getDay().setText(new SimpleDateFormat("dd. MMMM", Locale.ENGLISH).format(flight.getDepartureTime()));
        getPrice().setText("ISK " + flight.getPrice());
        getTime().setText(departureTime + " - " + arrivalTime);
        
        return this;
    }
}