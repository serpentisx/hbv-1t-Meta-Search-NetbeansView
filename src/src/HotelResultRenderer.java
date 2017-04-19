package src;

import java.awt.Color;
import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Locale;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import hotel.Hotel;

public class HotelResultRenderer extends HotelResult implements ListCellRenderer {
    
 
    @Override
    public Component getListCellRendererComponent (JList list, Object value, int index, boolean isSelected, boolean hasFocus) {
        if (value == null) return new JLabel();
        
        setOpaque(false);
             
        if (isSelected) {
            setOpaque(true);
            setBackground(new Color(180, 200, 190));
            setForeground(list.getForeground());
        }
        
        Hotel hotel = (Hotel) value;
        getHotelName().setText(hotel.getName());
        getAddress().setText(hotel.getAddress());
        getPrice().setText(hotel.getPrice() + " ISK");
        getRating().setIcon(hotel.getRatingIcon());
        
        return this;
    }
}