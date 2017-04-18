package src;
import java.awt.Color;
import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Locale;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import tour.DayTour;

/**
 *
 * @author npquy
 */
public class TourResultRenderer extends TourResult implements ListCellRenderer{
    
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
        
        DayTour tour = (DayTour) value;
        
        getDay().setText(new SimpleDateFormat("dd. MMMM", Locale.ENGLISH).format(tour.getDate()));
        getPrice().setText(tour.getPrice() + " kr");
        getTourLocation().setText(tour.getLocation());
        getTourName().setText(tour.getNameOfTrip());
        
        return this;
    }
}
