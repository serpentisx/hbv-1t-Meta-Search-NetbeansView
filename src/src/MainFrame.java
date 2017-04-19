package src;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultFormatter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import flight.*;
import hotel.*;
import java.util.ArrayList;
import java.util.UUID;
import tour.*;

public class MainFrame extends javax.swing.JFrame {

    /**
     * Creates new form MainFrame
     */
    
    private SlidePanel sp = new SlidePanel();
    private JDatePickerImpl returnPicker;
    private JDatePickerImpl departPicker;
    private List<Integer> toursSelected;
    private int xMouse;
    private int yMouse;
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private String customerAddress;
    private ImageIcon [] hotelStars;
    
    private SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
    private Customer customer = new Customer();
    private PackageManager manager;
    
    public MainFrame() {
        initComponents();
        manager = new PackageManager(new FlightGenerator("src/airportCodes.txt"), new HotelManager(true), new DayTourSearchMock(), null, new ReservationManager(), null);
        toursSelected = new ArrayList<>();
	generateComboboxModel();  
        generateHotelStars();
        calendar();
        comboBox();
    }
    
    public void displayTripInfo() {
        DefaultListModel dm = new DefaultListModel();
        dm.addElement(manager.getPackage());
        trip_list.setModel(dm);
    }
	
    private void searchFlights() {
        String origin = "";
        String destination = "";
        try {
            origin = (String) jDepartureComboBox.getSelectedItem();
            destination = (String) jLocationComboBox.getSelectedItem();
            List<Flight> outbound = manager.searchFlights(getDepartDate(), origin, destination);
            List<Flight> inbound = manager.searchFlights(getReturnDate(), destination, origin);
            displayFlightResults(inbound, outbound);
            Flight in = scrollToDate(inbound_list, getReturnDate());
            Flight out = scrollToDate(outbound_list, getDepartDate());
            manager.getPackage().setInbound(in);
            manager.getPackage().setOutbound(out);
            jLabel3.setText("ISK " + Integer.toString(manager.getPackage().calculatePrice()));
            slideLeft();
        } catch (NullPointerException e) {
            e.printStackTrace();
            ImageIcon p = new ImageIcon(getClass().getResource("/icons/warning.png"));
            JOptionPane.showMessageDialog(null, "Fill in all inputs", "Warning:", JOptionPane.WARNING_MESSAGE, p);
        }
    }
    
    private void searchHotels() {
    	try {            
            List<Hotel> hotels = manager.searchHotels(getDepartDate(), getReturnDate());
            for (Hotel h : hotels) {
                h.setPrice((int)(Math.floor(h.getRating()*Math.random())*2500) + 4990);
                h.setRatingIcon(hotelStars[h.getRating()]);
            }
            displayHotelResults(hotels);
        } catch (NullPointerException e) {
        	e.printStackTrace();
            System.out.println("Fill in all inputs");
        }
    }
    
    private void generateHotelStars() {
        hotelStars = new ImageIcon[6];
        for (int i = 0; i <= 5; i++) {
            hotelStars[i] = new ImageIcon(getClass().getResource("/hotel_images/" + i + "_stars.png"));
        }
    }
    
    private void searchTours() {
        try {
            List<DayTour> tours = manager.searchDayTours(getDepartDate(), getReturnDate());
            displayTourResults(tours);
        } catch (NullPointerException e) {
        	e.printStackTrace();
            System.out.println("Fill in all inputs");
        }
    }    
    
    private void generateComboboxModel() {
        DefaultComboBoxModel dbm = new DefaultComboBoxModel();
        List<flight.Airport> airports = manager.getFlightGenerator().getAirports();
        Collections.sort(airports);
        for (flight.Airport ap : airports) {
            String name = ap.getName() + " (" + ap.getAirportCode() + "), " + ap.getCountry();
            dbm.addElement(name);
        }
        jDepartureComboBox.setModel(dbm);
    }
    
    private void displayFlightResults(List<Flight> inbound_flights, List<Flight> outbound_flights) {
        DefaultListModel inbound = new DefaultListModel();
        DefaultListModel outbound = new DefaultListModel();
        addToListModel(inbound_flights, inbound);
        addToListModel(outbound_flights, outbound);
        setFlightLabel();
        inbound_list.setModel(inbound);
        outbound_list.setModel(outbound);
    }
    
    private void displayHotelResults(List<Hotel> hotels) {
    	DefaultListModel hotelModel = new DefaultListModel();
        addToListModel(hotels, hotelModel);
        hotel_list.setModel(hotelModel); 
    }
    
    private void displayTourResults(List<DayTour> tours) {
    	DefaultListModel tourModel = new DefaultListModel();
        addToListModel(tours, tourModel);
        tourList.setModel(tourModel);
    }
    
    private void setFlightLabel() {
        String  t = "<font color='rgb(153,51,0'>  to  </font>";
        String from = (String) jDepartureComboBox.getSelectedItem();
        String to = (String) jLocationComboBox.getSelectedItem();
        
        outbound_flight.setText("<html>" + from + t + to + "</html>");
        inbound_flight.setText("<html>" + to + t + from + "</html>");
    }
    
    public Flight scrollToDate(JList list, Date date) {
        Flight flight = null;
        int index = getFlightIndexAt(date, (DefaultListModel) list.getModel());
        if (index != -1) {
            flight = (Flight) list.getModel().getElementAt(index);
        }
        list.setSelectedIndex(index);
        list.ensureIndexIsVisible(index);
        return flight;
    }
    
    public int getFlightIndexAt(Date d, DefaultListModel dl) {
        for (int i = 0; i < dl.size(); i++) {
            if (fmt.format(((Flight) dl.get(i)).getDepartureTime()).equals(fmt.format(d))) {
                return i;
            }
        }
        return -1;
    }
    
    private void addToListModel(List<?> item, DefaultListModel  dm) {
        for (int i = 0; i < item.size(); i++) {
            dm.addElement(item.get(i));
        }
    }
    
    private void adjustFrame(){
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
    }
    
    private void calendar(){
        UtilDateModel model = new UtilDateModel();
        UtilDateModel model2 = new UtilDateModel();

        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        departPicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        JDatePanelImpl datePanel2 = new JDatePanelImpl(model2, p);
        returnPicker = new JDatePickerImpl(datePanel2, new DateLabelFormatter());

        departPicker.setBounds(0,0,150,40);
        returnPicker.setBounds(0,0,150,40);
        jDepartingCalendar.add(departPicker);
        jReturningCalendar.add(returnPicker);
    }
    
    private void comboBox(){
        JTextField departing = (JTextField)jDepartureComboBox.getEditor().getEditorComponent();
        JTextField returning = (JTextField)jLocationComboBox.getEditor().getEditorComponent();
        departing.addKeyListener(new ComboKeyHandler(jDepartureComboBox));
        returning.addKeyListener(new ComboKeyHandler(jLocationComboBox));
        changeScrollBarDimension(jDepartureComboBox, 7);
        changeScrollBarDimension(jLocationComboBox, 7);
    }

    private void changeScrollBarDimension(JComboBox<String> cb, int width) {
        Object popup = cb.getUI().getAccessibleChild(cb, 0);
        Component c = ((Container) popup).getComponent(0);
        if (c instanceof JScrollPane) {
            JScrollPane scrollpane = (JScrollPane) c;
            JScrollBar scrollBar = scrollpane.getVerticalScrollBar();
            Dimension scrollBarDim = new Dimension(width, scrollBar.getPreferredSize().height);
            scrollBar.setPreferredSize(scrollBarDim);
        }
    }
    
    private static void changeLF(String lf) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if (lf.equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("Error :" + e.getMessage());
        }
    }
    
    private void change_preference_tab(JPanel a){
        //Removing old panel
        result_container.removeAll();
        //Adding new one
        result_container.add(a);
        result_container.repaint();
        result_container.validate();
    }
    
    private void slideScrollBar(JScrollPane spane, int increment) {
        int current = spane.getHorizontalScrollBar().getValue();
        spane.getHorizontalScrollBar().setValue(current + increment);
    }
    
    private void slideLeft() {
        sp.slideLeft(result_panel.getX() - result_panel.getWidth(), 10, 20, result_panel);
        sp.slideLeft(preference_panel.getX() - preference_panel.getWidth(), 10, 20, preference_panel);
    }
    
    private void slideRight() {
        sp.slideRight(result_panel.getX() + result_panel.getWidth(), 10, 20, result_panel);
        sp.slideRight(preference_panel.getX() + preference_panel.getWidth(), 10, 20, preference_panel);
    }
    
    private Date getDepartDate() {
        return (Date) departPicker.getModel().getValue();
    }
    
    private Date getReturnDate() {
        return (Date) returnPicker.getModel().getValue();
    }
    
    private void setTravelersValue(int n) {
        manager.getPackage().setTravellers(n);
    }
    
    public List<Integer> getSelectedTours() {
        return toursSelected;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        container = new javax.swing.JPanel();
        result_panel = new javax.swing.JPanel();
        menuBar1 = new javax.swing.JPanel();
        jExit1 = new javax.swing.JLabel();
        flight_label1 = new javax.swing.JLabel();
        hotel_label1 = new javax.swing.JLabel();
        day_tour_label1 = new javax.swing.JLabel();
        back_label = new javax.swing.JLabel();
        priceLabel = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jcheckout = new javax.swing.JLabel();
        result_container = new javax.swing.JPanel();
        flight_result = new javax.swing.JPanel();
        outbound_flight = new javax.swing.JLabel();
        inbound_title = new javax.swing.JLabel();
        outbound_scrollpane = new javax.swing.JScrollPane();
        outbound_list = new javax.swing.JList<>();
        outbound_back = new javax.swing.JLabel();
        outbound_forward = new javax.swing.JLabel();
        outbound_title = new javax.swing.JLabel();
        inbound_scrollpane = new javax.swing.JScrollPane();
        inbound_list = new javax.swing.JList<>();
        inbound_back = new javax.swing.JLabel();
        inbound_flight = new javax.swing.JLabel();
        inbound_forward = new javax.swing.JLabel();
        hotel_result = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        hotel_list = new javax.swing.JList<>();
        tour_result = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tourList = new javax.swing.JList<>();
        customer_info = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        phone = new javax.swing.JTextField();
        name = new javax.swing.JTextField();
        email = new javax.swing.JTextField();
        address = new javax.swing.JTextField();
        jNext = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        confirm_info = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        trip_list = new javax.swing.JList<>();
        jLabel6 = new javax.swing.JLabel();
        preference_panel = new javax.swing.JPanel();
        menuBar = new javax.swing.JPanel();
        jExit = new javax.swing.JLabel();
        jInstruction = new javax.swing.JLabel();
        flight_preference = new javax.swing.JPanel();
        jTitle = new javax.swing.JLabel();
        jDepartureLabel = new javax.swing.JLabel();
        jLocationLabel = new javax.swing.JLabel();
        jTravelerLabel = new javax.swing.JLabel();
        changeLF("Windows");
        jTravelerSpinner = new javax.swing.JSpinner();
        jDepartingLabel = new javax.swing.JLabel();
        jReturningLabel = new javax.swing.JLabel();
        jDepartingCalendar = new javax.swing.JPanel();
        jReturningCalendar = new javax.swing.JPanel();
        changeLF("Windows");
        jLocationComboBox = new javax.swing.JComboBox<>();
        changeLF("Windows");
        jDepartureComboBox = new javax.swing.JComboBox<>();
        search_label = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        container.setBackground(new java.awt.Color(255, 255, 255));
        container.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(51, 51, 51)));
        container.setPreferredSize(new java.awt.Dimension(1215, 735));
        container.setLayout(null);

        result_panel.setBackground(new java.awt.Color(255, 255, 255));
        result_panel.setBorder(new javax.swing.border.MatteBorder(null));
        result_panel.setLayout(null);

        menuBar1.setBackground(new java.awt.Color(51, 51, 51));
        menuBar1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                menuBar1MouseDragged(evt);
            }
        });
        menuBar1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                menuBar1MousePressed(evt);
            }
        });

        jExit1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Multiply_64px_1.png"))); // NOI18N
        jExit1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jExit1MouseReleased(evt);
            }
        });

        flight_label1.setFont(new java.awt.Font("Segoe UI Light", 1, 20)); // NOI18N
        flight_label1.setForeground(new java.awt.Color(250, 250, 250));
        flight_label1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/flight-light-icon.png"))); // NOI18N
        flight_label1.setText("Flight");
        flight_label1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                flight_label1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                flight_label1MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                flight_label1MousePressed(evt);
            }
        });

        hotel_label1.setFont(new java.awt.Font("Segoe UI Light", 1, 20)); // NOI18N
        hotel_label1.setForeground(new java.awt.Color(250, 250, 250));
        hotel_label1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/hotel-light-icon.png"))); // NOI18N
        hotel_label1.setText("Hotel");
        hotel_label1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                hotel_label1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                hotel_label1MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                hotel_label1MousePressed(evt);
            }
        });

        day_tour_label1.setFont(new java.awt.Font("Segoe UI Light", 1, 20)); // NOI18N
        day_tour_label1.setForeground(new java.awt.Color(250, 250, 250));
        day_tour_label1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/day-tour-light-icon.png"))); // NOI18N
        day_tour_label1.setText("Day Tours");
        day_tour_label1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                day_tour_label1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                day_tour_label1MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                day_tour_label1MousePressed(evt);
            }
        });

        back_label.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/back-button-icon.png"))); // NOI18N
        back_label.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        back_label.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                back_labelMouseReleased(evt);
            }
        });

        priceLabel.setFont(new java.awt.Font("Segoe UI Light", 1, 24)); // NOI18N
        priceLabel.setForeground(new java.awt.Color(255, 255, 255));
        priceLabel.setText("Price:");

        jLabel3.setFont(new java.awt.Font("Segoe UI Light", 0, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("0");

        jcheckout.setFont(new java.awt.Font("Segoe UI Light", 1, 20)); // NOI18N
        jcheckout.setForeground(new java.awt.Color(255, 255, 255));
        jcheckout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Card in Use_64px.png"))); // NOI18N
        jcheckout.setText("Checkout");
        jcheckout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jcheckoutMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jcheckoutMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jcheckoutMousePressed(evt);
            }
        });

        javax.swing.GroupLayout menuBar1Layout = new javax.swing.GroupLayout(menuBar1);
        menuBar1.setLayout(menuBar1Layout);
        menuBar1Layout.setHorizontalGroup(
            menuBar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, menuBar1Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(back_label)
                .addGap(52, 52, 52)
                .addComponent(flight_label1, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51)
                .addComponent(hotel_label1, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addComponent(day_tour_label1)
                .addGap(56, 56, 56)
                .addComponent(jcheckout)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 86, Short.MAX_VALUE)
                .addComponent(priceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jExit1)
                .addGap(27, 27, 27))
        );
        menuBar1Layout.setVerticalGroup(
            menuBar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuBar1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(menuBar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(menuBar1Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(back_label))
                    .addGroup(menuBar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(flight_label1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(day_tour_label1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(hotel_label1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(priceLabel)
                        .addComponent(jLabel3)
                        .addComponent(jcheckout))
                    .addComponent(jExit1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        result_panel.add(menuBar1);
        menuBar1.setBounds(0, 0, 1220, 90);

        result_container.setBackground(new java.awt.Color(255, 255, 255));
        result_container.setLayout(new java.awt.CardLayout());

        flight_result.setBackground(new java.awt.Color(255, 255, 255));

        outbound_flight.setFont(new java.awt.Font("Segoe UI Light", 1, 18)); // NOI18N
        outbound_flight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/departure-13x13-icon.png"))); // NOI18N
        outbound_flight.setText("Keflavík (KEF)  to London (LHR)");

        inbound_title.setFont(new java.awt.Font("Segoe UI Light", 1, 18)); // NOI18N
        inbound_title.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/arrival-icon.png"))); // NOI18N
        inbound_title.setText("  Select inbound flight");

        outbound_list.setFixedCellWidth(160);
        outbound_list.setVisibleRowCount(1);
        outbound_list.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                outbound_listMousePressed(evt);
            }
        });
        outbound_scrollpane.setViewportView(outbound_list);
        outbound_list.setBorder(BorderFactory.createEmptyBorder());
        outbound_list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        outbound_list.setCellRenderer(new FlightResultRenderer());
        outbound_list.setVisibleRowCount(1);

        outbound_back.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/arrow-left-black.png"))); // NOI18N
        outbound_back.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        outbound_back.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                outbound_backMousePressed(evt);
            }
        });

        outbound_forward.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/arrow-right-black.png"))); // NOI18N
        outbound_forward.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        outbound_forward.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                outbound_forwardMousePressed(evt);
            }
        });

        outbound_title.setFont(new java.awt.Font("Segoe UI Light", 1, 18)); // NOI18N
        outbound_title.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/departure-icon.png"))); // NOI18N
        outbound_title.setText("  Select outbound flight");

        inbound_list.setFixedCellWidth(160);
        inbound_list.setVisibleRowCount(1);
        inbound_list.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                inbound_listMousePressed(evt);
            }
        });
        inbound_scrollpane.setViewportView(inbound_list);
        inbound_list.setBorder(BorderFactory.createEmptyBorder());
        inbound_list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        inbound_list.setCellRenderer(new FlightResultRenderer());
        inbound_list.setVisibleRowCount(1);

        inbound_back.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/arrow-left-black.png"))); // NOI18N
        inbound_back.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        inbound_back.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                inbound_backMousePressed(evt);
            }
        });

        inbound_flight.setFont(new java.awt.Font("Segoe UI Light", 1, 18)); // NOI18N
        inbound_flight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/departure-13x13-icon.png"))); // NOI18N
        inbound_flight.setText("Keflavík (KEF)  to London (LHR)");

        inbound_forward.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/arrow-right-black.png"))); // NOI18N
        inbound_forward.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        inbound_forward.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                inbound_forwardMousePressed(evt);
            }
        });

        javax.swing.GroupLayout flight_resultLayout = new javax.swing.GroupLayout(flight_result);
        flight_result.setLayout(flight_resultLayout);
        flight_resultLayout.setHorizontalGroup(
            flight_resultLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(flight_resultLayout.createSequentialGroup()
                .addGap(74, 74, 74)
                .addComponent(inbound_title, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, flight_resultLayout.createSequentialGroup()
                .addGap(0, 61, Short.MAX_VALUE)
                .addGroup(flight_resultLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(flight_resultLayout.createSequentialGroup()
                        .addComponent(inbound_back)
                        .addGap(42, 42, 42)
                        .addGroup(flight_resultLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(inbound_flight, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(inbound_scrollpane, javax.swing.GroupLayout.DEFAULT_SIZE, 971, Short.MAX_VALUE))
                        .addGap(33, 33, 33)
                        .addComponent(inbound_forward))
                    .addGroup(flight_resultLayout.createSequentialGroup()
                        .addComponent(outbound_back)
                        .addGap(42, 42, 42)
                        .addGroup(flight_resultLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(flight_resultLayout.createSequentialGroup()
                                .addComponent(outbound_scrollpane, javax.swing.GroupLayout.PREFERRED_SIZE, 971, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(33, 33, 33)
                                .addComponent(outbound_forward))
                            .addComponent(outbound_flight, javax.swing.GroupLayout.PREFERRED_SIZE, 971, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(78, 78, 78))
            .addGroup(flight_resultLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(flight_resultLayout.createSequentialGroup()
                    .addGap(84, 84, 84)
                    .addComponent(outbound_title, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(824, Short.MAX_VALUE)))
        );
        flight_resultLayout.setVerticalGroup(
            flight_resultLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, flight_resultLayout.createSequentialGroup()
                .addContainerGap(114, Short.MAX_VALUE)
                .addGroup(flight_resultLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, flight_resultLayout.createSequentialGroup()
                        .addGroup(flight_resultLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(outbound_back, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(outbound_forward, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(124, 124, 124))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, flight_resultLayout.createSequentialGroup()
                        .addComponent(outbound_flight, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(outbound_scrollpane, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(73, 73, 73)))
                .addComponent(inbound_title, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(flight_resultLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(flight_resultLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(inbound_flight, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)
                        .addComponent(inbound_scrollpane, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(flight_resultLayout.createSequentialGroup()
                        .addGap(121, 121, 121)
                        .addComponent(inbound_back))
                    .addGroup(flight_resultLayout.createSequentialGroup()
                        .addGap(122, 122, 122)
                        .addComponent(inbound_forward)))
                .addGap(53, 53, 53))
            .addGroup(flight_resultLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(flight_resultLayout.createSequentialGroup()
                    .addGap(48, 48, 48)
                    .addComponent(outbound_title, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(552, Short.MAX_VALUE)))
        );

        outbound_flight.setHorizontalAlignment(JLabel.CENTER);
        outbound_scrollpane.getViewport().setOpaque(false);
        outbound_scrollpane.setViewportBorder(null);
        outbound_scrollpane.getHorizontalScrollBar().setPreferredSize(new Dimension(0,0));
        inbound_scrollpane.getViewport().setOpaque(false);
        inbound_scrollpane.setViewportBorder(null);
        inbound_scrollpane.getHorizontalScrollBar().setPreferredSize(new Dimension(0,0));
        inbound_flight.setHorizontalAlignment(JLabel.CENTER);

        result_container.add(flight_result, "card2");

        hotel_result.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 22)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/hotel-dark-icon-mini.png"))); // NOI18N
        jLabel1.setText("Pick your hotel  ");

        hotel_list.setFixedCellHeight(190);
        hotel_list.setFixedCellWidth(360);
        hotel_list.setLayoutOrientation(javax.swing.JList.HORIZONTAL_WRAP);
        hotel_list.setVisibleRowCount(-1);
        hotel_list.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                hotel_listMousePressed(evt);
            }
        });
        jScrollPane2.setViewportView(hotel_list);
        hotel_list.setCellRenderer(new HotelResultRenderer());
        hotel_list.setBorder(null);
        hotel_list.setBorder(BorderFactory.createEmptyBorder());

        javax.swing.GroupLayout hotel_resultLayout = new javax.swing.GroupLayout(hotel_result);
        hotel_result.setLayout(hotel_resultLayout);
        hotel_resultLayout.setHorizontalGroup(
            hotel_resultLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(hotel_resultLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(hotel_resultLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1170, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        hotel_resultLayout.setVerticalGroup(
            hotel_resultLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(hotel_resultLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 571, Short.MAX_VALUE))
        );

        jLabel1.setHorizontalTextPosition(SwingConstants.LEFT);
        jScrollPane2.getViewport().setOpaque(false);
        jScrollPane2.setViewportBorder(null);
        jScrollPane2.getVerticalScrollBar().setPreferredSize(new Dimension(7,7));
        jScrollPane2.setBorder(BorderFactory.createEmptyBorder());

        result_container.add(hotel_result, "card3");

        tour_result.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane1.setMaximumSize(new java.awt.Dimension(880, 32767));

        tourList.setFont(new java.awt.Font("Segoe UI Light", 0, 24)); // NOI18N
        tourList.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tourList.setFixedCellWidth(220);
        tourList.setLayoutOrientation(javax.swing.JList.HORIZONTAL_WRAP);
        tourList.setVisibleRowCount(-1);
        tourList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tourListMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(tourList);
        tourList.setCellRenderer(new TourResultRenderer(this));

        javax.swing.GroupLayout tour_resultLayout = new javax.swing.GroupLayout(tour_result);
        tour_result.setLayout(tour_resultLayout);
        tour_resultLayout.setHorizontalGroup(
            tour_resultLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tour_resultLayout.createSequentialGroup()
                .addGap(184, 184, 184)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 901, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(130, Short.MAX_VALUE))
        );
        tour_resultLayout.setVerticalGroup(
            tour_resultLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tour_resultLayout.createSequentialGroup()
                .addContainerGap(96, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 506, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43))
        );

        result_container.add(tour_result, "card4");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Segoe UI Light", 1, 24)); // NOI18N
        jLabel2.setText("Name:");

        jLabel8.setFont(new java.awt.Font("Segoe UI Light", 1, 24)); // NOI18N
        jLabel8.setText("Phone:");

        jLabel9.setFont(new java.awt.Font("Segoe UI Light", 1, 24)); // NOI18N
        jLabel9.setText("Email:");

        jLabel4.setFont(new java.awt.Font("Segoe UI Light", 1, 24)); // NOI18N
        jLabel4.setText("Address:");

        phone.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        phone.setText("891 xxxx");
        phone.setBorder(null);
        phone.setBorder(BorderFactory.createEmptyBorder());
        phone.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                phoneMouseClicked(evt);
            }
        });
        phone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                phoneActionPerformed(evt);
            }
        });

        name.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        name.setText("John...");
        name.setBorder(null);
        name.setBorder(BorderFactory.createEmptyBorder());
        name.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                nameMouseClicked(evt);
            }
        });

        email.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        email.setText("jhon@xxxxxxx.com");
        email.setBorder(null);
        email.setBorder(BorderFactory.createEmptyBorder());
        email.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                emailMouseClicked(evt);
            }
        });

        address.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        address.setText("Your address");
        address.setBorder(null);
        address.setBorder(BorderFactory.createEmptyBorder());
        address.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addressMouseClicked(evt);
            }
        });

        jNext.setFont(new java.awt.Font("Segoe UI Light", 1, 24)); // NOI18N
        jNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/arrow-right-black.png"))); // NOI18N
        jNext.setText("Confirm  ");
        jNext.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jNext.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jNext.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jNextMousePressed(evt);
            }
        });

        jSeparator1.setBackground(new java.awt.Color(0, 0, 0));

        jSeparator2.setBackground(new java.awt.Color(0, 0, 0));

        jSeparator3.setBackground(new java.awt.Color(0, 0, 0));

        jSeparator4.setBackground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(467, 467, 467)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(email, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(name, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(phone, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel4)
                    .addComponent(address, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(421, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jNext)
                .addGap(80, 80, 80))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(85, 85, 85)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(jLabel8)
                .addGap(37, 37, 37)
                .addComponent(phone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50)
                        .addComponent(jLabel9)
                        .addGap(18, 18, 18)
                        .addComponent(email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(48, 48, 48)
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(address, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 67, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jNext)
                        .addGap(62, 62, 62))))
        );

        javax.swing.GroupLayout customer_infoLayout = new javax.swing.GroupLayout(customer_info);
        customer_info.setLayout(customer_infoLayout);
        customer_infoLayout.setHorizontalGroup(
            customer_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        customer_infoLayout.setVerticalGroup(
            customer_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        result_container.add(customer_info, "card5");

        confirm_info.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setFont(new java.awt.Font("Segoe UI Light", 1, 26)); // NOI18N
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/travel.png"))); // NOI18N
        jLabel5.setText("Confirm package  ");

        trip_list.setFixedCellHeight(1830);
        trip_list.setFixedCellWidth(1170);
        trip_list.setLayoutOrientation(javax.swing.JList.HORIZONTAL_WRAP);
        trip_list.setVisibleRowCount(-1);
        trip_list.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                trip_listMousePressed(evt);
            }
        });
        jScrollPane3.setViewportView(trip_list);
        trip_list.setCellRenderer(new TripResultRenderer());
        trip_list.setBorder(null);
        trip_list.setBorder(BorderFactory.createEmptyBorder());

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Book now");
        jLabel6.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(153, 153, 153)));
        jLabel6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel6.setOpaque(true);
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabel6MouseReleased(evt);
            }
        });

        javax.swing.GroupLayout confirm_infoLayout = new javax.swing.GroupLayout(confirm_info);
        confirm_info.setLayout(confirm_infoLayout);
        confirm_infoLayout.setHorizontalGroup(
            confirm_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(confirm_infoLayout.createSequentialGroup()
                .addGroup(confirm_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(confirm_infoLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(679, 679, 679)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1170, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 45, Short.MAX_VALUE))
        );
        confirm_infoLayout.setVerticalGroup(
            confirm_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(confirm_infoLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(confirm_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(confirm_infoLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(0, 11, Short.MAX_VALUE))
                    .addGroup(confirm_infoLayout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 555, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jLabel5.setHorizontalAlignment(JLabel.RIGHT);
        jScrollPane3.getViewport().setOpaque(false);
        jScrollPane3.setViewportBorder(null);
        jScrollPane3.getVerticalScrollBar().setPreferredSize(new Dimension(7,7));
        jScrollPane3.setBorder(BorderFactory.createEmptyBorder());

        result_container.add(confirm_info, "card6");

        result_panel.add(result_container);
        result_container.setBounds(0, 90, 1215, 645);

        container.add(result_panel);
        result_panel.setBounds(1215, 0, 1215, 735);

        preference_panel.setBackground(new java.awt.Color(255, 255, 255));
        preference_panel.setBorder(new javax.swing.border.MatteBorder(null));
        preference_panel.setLayout(null);

        menuBar.setBackground(new java.awt.Color(51, 51, 51));
        menuBar.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                menuBarMouseDragged(evt);
            }
        });
        menuBar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                menuBarMousePressed(evt);
            }
        });

        jExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Multiply_64px_1.png"))); // NOI18N
        jExit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jExitMouseReleased(evt);
            }
        });

        jInstruction.setBackground(new java.awt.Color(255, 255, 255));
        jInstruction.setFont(new java.awt.Font("Segoe UI Light", 1, 24)); // NOI18N
        jInstruction.setForeground(new java.awt.Color(255, 255, 255));
        jInstruction.setText("Instruction");
        jInstruction.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jInstructionMousePressed(evt);
            }
        });

        javax.swing.GroupLayout menuBarLayout = new javax.swing.GroupLayout(menuBar);
        menuBar.setLayout(menuBarLayout);
        menuBarLayout.setHorizontalGroup(
            menuBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, menuBarLayout.createSequentialGroup()
                .addGap(67, 67, 67)
                .addComponent(jInstruction, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 916, Short.MAX_VALUE)
                .addComponent(jExit)
                .addGap(27, 27, 27))
        );
        menuBarLayout.setVerticalGroup(
            menuBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuBarLayout.createSequentialGroup()
                .addGroup(menuBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(menuBarLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jExit))
                    .addGroup(menuBarLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jInstruction)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        preference_panel.add(menuBar);
        menuBar.setBounds(0, 0, 1220, 90);

        flight_preference.setBackground(new java.awt.Color(255, 255, 255));
        flight_preference.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTitle.setFont(new java.awt.Font("Segoe UI Semibold", 1, 35)); // NOI18N
        jTitle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Snowflake_48px.png"))); // NOI18N
        jTitle.setText("Book your trip  ");
        flight_preference.add(jTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 28, 320, 66));
        jTitle.setHorizontalTextPosition(SwingConstants.LEFT);

        jDepartureLabel.setFont(new java.awt.Font("Segoe UI Light", 1, 30)); // NOI18N
        jDepartureLabel.setText("From");
        flight_preference.add(jDepartureLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(91, 160, 102, -1));

        jLocationLabel.setFont(new java.awt.Font("Segoe UI Light", 1, 30)); // NOI18N
        jLocationLabel.setText("To");
        flight_preference.add(jLocationLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(91, 292, 102, -1));

        jTravelerLabel.setFont(new java.awt.Font("Segoe UI Light", 1, 30)); // NOI18N
        jTravelerLabel.setText("Travelers");
        flight_preference.add(jTravelerLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(878, 218, -1, -1));

        jTravelerSpinner.setFont(new java.awt.Font("Segoe UI Light", 0, 24)); // NOI18N
        jTravelerSpinner.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));
        changeLF("Nimbus");
        jTravelerSpinner.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTravelerSpinnerKeyPressed(evt);
            }
        });
        flight_preference.add(jTravelerSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(1026, 222, 74, -1));
        JComponent comp = jTravelerSpinner.getEditor();
        JFormattedTextField field = (JFormattedTextField) comp.getComponent(0);
        DefaultFormatter formatter = (DefaultFormatter) field.getFormatter();
        formatter.setCommitsOnValidEdit(true);
        jTravelerSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                setTravelersValue((int)jTravelerSpinner.getValue());
            }
        });

        jDepartingLabel.setFont(new java.awt.Font("Segoe UI Light", 0, 24)); // NOI18N
        jDepartingLabel.setText("Departing");
        flight_preference.add(jDepartingLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(91, 472, -1, 34));

        jReturningLabel.setFont(new java.awt.Font("Segoe UI Light", 0, 24)); // NOI18N
        jReturningLabel.setText("Returning");
        flight_preference.add(jReturningLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(331, 472, 110, 34));

        jDepartingCalendar.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jDepartingCalendarLayout = new javax.swing.GroupLayout(jDepartingCalendar);
        jDepartingCalendar.setLayout(jDepartingCalendarLayout);
        jDepartingCalendarLayout.setHorizontalGroup(
            jDepartingCalendarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 142, Short.MAX_VALUE)
        );
        jDepartingCalendarLayout.setVerticalGroup(
            jDepartingCalendarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 34, Short.MAX_VALUE)
        );

        flight_preference.add(jDepartingCalendar, new org.netbeans.lib.awtextra.AbsoluteConstraints(91, 524, -1, -1));

        jReturningCalendar.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jReturningCalendarLayout = new javax.swing.GroupLayout(jReturningCalendar);
        jReturningCalendar.setLayout(jReturningCalendarLayout);
        jReturningCalendarLayout.setHorizontalGroup(
            jReturningCalendarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 156, Short.MAX_VALUE)
        );
        jReturningCalendarLayout.setVerticalGroup(
            jReturningCalendarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 34, Short.MAX_VALUE)
        );

        flight_preference.add(jReturningCalendar, new org.netbeans.lib.awtextra.AbsoluteConstraints(331, 524, -1, -1));

        jLocationComboBox.setEditable(true);
        jLocationComboBox.setFont(new java.awt.Font("Segoe UI Light", 0, 24)); // NOI18N
        jLocationComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Keflavik International Airport (KEF), Iceland" }));
        flight_preference.add(jLocationComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(91, 354, 610, -1));
        changeLF("Nimbus");

        jDepartureComboBox.setEditable(true);
        jDepartureComboBox.setFont(new java.awt.Font("Segoe UI Light", 0, 24)); // NOI18N
        jDepartureComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Bahamas, The", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bhutan", "Bolivia", "Bosnia and Herzegovina", "Botswana", "Brazil", "Brunei", "Bulgaria", "Burkina Faso", "Burma", "Burundi", "Cambodia", "Cameroon", "Canada", "Cabo Verde", "Central African Republic", "Chad", "Chile", "China", "Colombia", "Comoros", "Congo, Democratic Republic of the", "Congo, Republic of the", "Costa Rica", "Cote d'Ivoire", "Croatia", "Cuba", "Curacao", "Cyprus", "Czechia" }));
        jDepartureComboBox.setSelectedIndex(-1);
        flight_preference.add(jDepartureComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(91, 222, 610, -1));
        changeLF("Nimbus");

        search_label.setBackground(new java.awt.Color(204, 204, 204));
        search_label.setFont(new java.awt.Font("Segoe UI Light", 1, 35)); // NOI18N
        search_label.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/search-icon.png"))); // NOI18N
        search_label.setText("Search  ");
        search_label.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        search_label.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        search_label.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                search_labelMouseReleased(evt);
            }
        });
        flight_preference.add(search_label, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 520, -1, 50));
        search_label.setHorizontalTextPosition(SwingConstants.LEFT);

        preference_panel.add(flight_preference);
        flight_preference.setBounds(0, 90, 1210, 640);

        container.add(preference_panel);
        preference_panel.setBounds(1, 1, 1213, 733);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(container, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(container, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(1215, 735));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void menuBarMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuBarMouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x-xMouse, y-yMouse);
    }//GEN-LAST:event_menuBarMouseDragged

    private void menuBarMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuBarMousePressed
        xMouse= evt.getX();
        yMouse= evt.getY();
    }//GEN-LAST:event_menuBarMousePressed

    private void menuBar1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuBar1MouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x-xMouse, y-yMouse);
    }//GEN-LAST:event_menuBar1MouseDragged

    private void menuBar1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuBar1MousePressed
        xMouse= evt.getX();
        yMouse= evt.getY();
    }//GEN-LAST:event_menuBar1MousePressed

    private void flight_label1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_flight_label1MouseEntered
        flight_label1.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(250,250,250)));
    }//GEN-LAST:event_flight_label1MouseEntered

    private void flight_label1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_flight_label1MouseExited
        flight_label1.setBorder(BorderFactory.createEmptyBorder());
    }//GEN-LAST:event_flight_label1MouseExited

    private void flight_label1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_flight_label1MousePressed
        change_preference_tab(flight_result);
    }//GEN-LAST:event_flight_label1MousePressed

    private void hotel_label1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hotel_label1MouseEntered
        hotel_label1.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(250,250,250)));
    }//GEN-LAST:event_hotel_label1MouseEntered

    private void hotel_label1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hotel_label1MouseExited
        hotel_label1.setBorder(BorderFactory.createEmptyBorder());
    }//GEN-LAST:event_hotel_label1MouseExited

    private void hotel_label1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hotel_label1MousePressed
        change_preference_tab(hotel_result);
    }//GEN-LAST:event_hotel_label1MousePressed

    private void day_tour_label1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_day_tour_label1MouseEntered
        day_tour_label1.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(250,250,250)));
    }//GEN-LAST:event_day_tour_label1MouseEntered

    private void day_tour_label1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_day_tour_label1MouseExited
        day_tour_label1.setBorder(BorderFactory.createEmptyBorder());
    }//GEN-LAST:event_day_tour_label1MouseExited

    private void day_tour_label1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_day_tour_label1MousePressed
        change_preference_tab(tour_result);
    }//GEN-LAST:event_day_tour_label1MousePressed

    private void search_labelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_search_labelMouseReleased
    	searchFlights();
        searchHotels();
        searchTours();
    }//GEN-LAST:event_search_labelMouseReleased

    private void back_labelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_back_labelMouseReleased
        slideRight();
    }//GEN-LAST:event_back_labelMouseReleased

    private void inbound_forwardMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_inbound_forwardMousePressed
        slideScrollBar(inbound_scrollpane, 120);
    }//GEN-LAST:event_inbound_forwardMousePressed

    private void inbound_backMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_inbound_backMousePressed
        slideScrollBar(inbound_scrollpane, -120);
    }//GEN-LAST:event_inbound_backMousePressed

    private void outbound_forwardMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_outbound_forwardMousePressed
        slideScrollBar(outbound_scrollpane, 120);
    }//GEN-LAST:event_outbound_forwardMousePressed

    private void outbound_backMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_outbound_backMousePressed
        slideScrollBar(outbound_scrollpane, -120);
    }//GEN-LAST:event_outbound_backMousePressed

    private void jTravelerSpinnerKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTravelerSpinnerKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTravelerSpinnerKeyPressed

    private void jExit1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jExit1MouseReleased
        System.exit(0);
    }//GEN-LAST:event_jExit1MouseReleased

    private void jExitMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jExitMouseReleased
        System.exit(0);
    }//GEN-LAST:event_jExitMouseReleased

    private void outbound_listMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_outbound_listMousePressed
        Flight selected = (Flight) ((DefaultListModel) outbound_list.getModel()).getElementAt(outbound_list.getSelectedIndex());
        manager.getPackage().setOutbound(selected);
        jLabel3.setText("ISK "+Integer.toString(manager.getPackage().calculatePrice()));
    }//GEN-LAST:event_outbound_listMousePressed

    private void inbound_listMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_inbound_listMousePressed
        Flight selected = (Flight) ((DefaultListModel) inbound_list.getModel()).getElementAt(inbound_list.getSelectedIndex());
        manager.getPackage().setInbound(selected);
        jLabel3.setText("ISK "+Integer.toString(manager.getPackage().calculatePrice()));
    }//GEN-LAST:event_inbound_listMousePressed

    private void hotel_listMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hotel_listMousePressed
        Hotel selected = (Hotel) ((DefaultListModel) hotel_list.getModel()).getElementAt(hotel_list.getSelectedIndex());
        manager.getPackage().setHotel(selected);
        jLabel3.setText("ISK "+Integer.toString(manager.getPackage().calculatePrice()));
    }//GEN-LAST:event_hotel_listMousePressed

    private void tourListMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tourListMousePressed
        Integer selectedIndex = tourList.getSelectedIndex();
        if(!toursSelected.contains(selectedIndex)) {
            toursSelected.add(selectedIndex);
        } else {
            toursSelected.remove(selectedIndex);
        }
        
        DefaultListModel model = (DefaultListModel) tourList.getModel();
        DayTour selected = (DayTour) model.getElementAt(tourList.getSelectedIndex());
        if (!manager.getPackage().getDayTours().contains(selected)) {
            manager.getPackage().addDayTour(selected);
        } else {
            manager.getPackage().removeDayTour(selected);
        }
        
        jLabel3.setText("ISK "+Integer.toString(manager.getPackage().calculatePrice()));
    }//GEN-LAST:event_tourListMousePressed

    private void jInstructionMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jInstructionMousePressed
        Instruction a = new Instruction(this, true);
        a.setVisible(true);
    }//GEN-LAST:event_jInstructionMousePressed

    private void jcheckoutMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jcheckoutMousePressed
        change_preference_tab(customer_info);
    }//GEN-LAST:event_jcheckoutMousePressed

    private void jcheckoutMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jcheckoutMouseEntered
        jcheckout.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(250,250,250)));
    }//GEN-LAST:event_jcheckoutMouseEntered

    private void jcheckoutMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jcheckoutMouseExited
        jcheckout.setBorder(BorderFactory.createEmptyBorder());
    }//GEN-LAST:event_jcheckoutMouseExited

    private void jNextMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jNextMousePressed
        customer.setName(name.getText());
        customer.setPhone(phone.getText());
        customer.setEmail(email.getText());
        customer.setAddress(address.getText());
        manager.getPackage().setCustomer(customer);
        TravelPackage p = manager.getPackage();
        if (p.getInbound() == null || p.getOutbound() == null || p.getDayTours() == null || p.getHotel() == null) {
            ImageIcon d = new ImageIcon(getClass().getResource("/icons/warning.png"));
            JOptionPane.showMessageDialog(null, "Select flights, hotel AND day tours.", "Warning:", JOptionPane.WARNING_MESSAGE, d);
        } else {
            displayTripInfo();
            change_preference_tab(confirm_info);
        }
    }//GEN-LAST:event_jNextMousePressed

    private void phoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_phoneActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_phoneActionPerformed

    private void nameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nameMouseClicked
        name.setText("");
    }//GEN-LAST:event_nameMouseClicked

    private void phoneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_phoneMouseClicked
        phone.setText("");
    }//GEN-LAST:event_phoneMouseClicked

    private void emailMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_emailMouseClicked
        email.setText("");        // TODO add your handling code here:
    }//GEN-LAST:event_emailMouseClicked

    private void addressMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addressMouseClicked
        address.setText("");        // TODO add your handling code here:
    }//GEN-LAST:event_addressMouseClicked

    private void trip_listMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_trip_listMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_trip_listMousePressed

    private void jLabel6MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseReleased
        String id = UUID.randomUUID().toString().substring(0, 12);
        ImageIcon d = new ImageIcon(getClass().getResource("/icons/success-icon.png"));
        JOptionPane.showMessageDialog(null, "Your package has been successfully booked. Your booking id is: "  + id, "Booked!", JOptionPane.WARNING_MESSAGE, d);
    }//GEN-LAST:event_jLabel6MouseReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField address;
    private javax.swing.JLabel back_label;
    private javax.swing.JPanel confirm_info;
    private javax.swing.JPanel container;
    private javax.swing.JPanel customer_info;
    private javax.swing.JLabel day_tour_label1;
    private javax.swing.JTextField email;
    private javax.swing.JLabel flight_label1;
    private javax.swing.JPanel flight_preference;
    private javax.swing.JPanel flight_result;
    private javax.swing.JLabel hotel_label1;
    private javax.swing.JList<String> hotel_list;
    private javax.swing.JPanel hotel_result;
    private javax.swing.JLabel inbound_back;
    private javax.swing.JLabel inbound_flight;
    private javax.swing.JLabel inbound_forward;
    private javax.swing.JList<String> inbound_list;
    private javax.swing.JScrollPane inbound_scrollpane;
    private javax.swing.JLabel inbound_title;
    private javax.swing.JPanel jDepartingCalendar;
    private javax.swing.JLabel jDepartingLabel;
    private javax.swing.JComboBox<String> jDepartureComboBox;
    private javax.swing.JLabel jDepartureLabel;
    private javax.swing.JLabel jExit;
    private javax.swing.JLabel jExit1;
    private javax.swing.JLabel jInstruction;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JComboBox<String> jLocationComboBox;
    private javax.swing.JLabel jLocationLabel;
    private javax.swing.JLabel jNext;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jReturningCalendar;
    private javax.swing.JLabel jReturningLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JLabel jTitle;
    private javax.swing.JLabel jTravelerLabel;
    private javax.swing.JSpinner jTravelerSpinner;
    private javax.swing.JLabel jcheckout;
    private javax.swing.JPanel menuBar;
    private javax.swing.JPanel menuBar1;
    private javax.swing.JTextField name;
    private javax.swing.JLabel outbound_back;
    private javax.swing.JLabel outbound_flight;
    private javax.swing.JLabel outbound_forward;
    private javax.swing.JList<String> outbound_list;
    private javax.swing.JScrollPane outbound_scrollpane;
    private javax.swing.JLabel outbound_title;
    private javax.swing.JTextField phone;
    private javax.swing.JPanel preference_panel;
    private javax.swing.JLabel priceLabel;
    private javax.swing.JPanel result_container;
    private javax.swing.JPanel result_panel;
    private javax.swing.JLabel search_label;
    private javax.swing.JList<String> tourList;
    private javax.swing.JPanel tour_result;
    private javax.swing.JList<String> trip_list;
    // End of variables declaration//GEN-END:variables
}