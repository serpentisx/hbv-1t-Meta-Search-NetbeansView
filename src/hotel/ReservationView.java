package hotel;

import java.awt.EventQueue;

import javax.swing.JFrame;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.awt.event.*;
import javax.swing.JLabel;

public class ReservationView extends Observable {

	private JFrame frame;
	private JPanel contentPane;
	private JButton btnConfirm = new JButton();
	int counter = 0;
	private ReservationManager resm = new ReservationManager();
	
	private ResultsView resultsView;	
	private ArrayList<String> dates;
	
	private Hotel hotel;
	private LocalDate startDate, endDate;
	private int numberOfGuests;
	private int reservationID = -1;
	
	private String fullName;
	private String email;
	private int phoneNumber;
	private String address;
	private JLabel lblHotel;
	private JLabel lblStartingDate;
	private JLabel lblEndDate;
	private JLabel lblGuests;
	private JLabel lblReservationStatusAwaiting;
	JButton btnConfirm_1;
	
	

public ReservationView() {
	
	frame = new JFrame();
	frame.setBounds(100, 100, 748, 540);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	contentPane = new JPanel();
	contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	frame.setContentPane(contentPane);
	contentPane.setLayout(null);
	
	JPanel panel = new JPanel();
	panel.setBounds(10, 11, 674, 221);
	contentPane.add(panel);
	panel.setLayout(null);
	
	lblHotel = new JLabel("Hotel: ");
	lblHotel.setBounds(12, 13, 180, 16);
	panel.add(lblHotel);
	
	lblStartingDate = new JLabel("Starting date : ");
	lblStartingDate.setBounds(12, 42, 180, 16);
	panel.add(lblStartingDate);
	
	lblEndDate = new JLabel("End date: ");
	lblEndDate.setBounds(12, 79, 180, 16);
	panel.add(lblEndDate);
	
	lblGuests = new JLabel("Guests: ");
	lblGuests.setBounds(12, 108, 180, 16);
	panel.add(lblGuests);
	
	btnConfirm_1 = new JButton("Confirm");
	btnConfirm_1.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			reservationID = resm.addReservation(hotel, startDate, endDate, numberOfGuests);
			if( reservationID >= 0 )
				lblReservationStatusAwaiting.setText("Reservation confirmed! Your reservation number is: "+reservationID);
			else
				lblReservationStatusAwaiting.setText("Reservation failed! Attempts returned errorcode "+reservationID);
		}
	});
	btnConfirm_1.setBounds(12, 180, 97, 25);
	panel.add(btnConfirm_1);
	
	lblReservationStatusAwaiting = new JLabel("Reservation status: Awaiting confirmation");
	lblReservationStatusAwaiting.setBounds(180, 184, 350, 16);
	panel.add(lblReservationStatusAwaiting);
	}

 public void receiveSelection(Hotel chosenHotel, LocalDate startDate, LocalDate endDate, int numberOfGuests)
 {
	 this.hotel = chosenHotel;
	 this.startDate = startDate;
	 this.endDate = endDate;
	 this.numberOfGuests = numberOfGuests;
	 
	 lblHotel.setText("Hotel: "+chosenHotel.getName());
	 lblStartingDate.setText("Starting date: "+startDate.toString());;
	 lblEndDate.setText("Ending date: "+endDate.toString());;
	 lblGuests.setText("Guests: "+numberOfGuests);
 }
 

	public void displayConfirmation(String txt){
		btnConfirm.setText(txt);
	}
	
	public void addReservationManager(ReservationManager rm){
		resm = rm;
	}
	
	public void addResultsView(ResultsView v)
	{
		this.resultsView = v;
	}
	
	public void setVisible(boolean state)
	{
		frame.setVisible(state);
		contentPane.setVisible(state);
		contentPane.repaint();	
	}
	
	public ArrayList<String> getDates() {
		return dates;
	}

	public void setDates(ArrayList<String> dates) {
		this.dates = dates;
	}

	public int getNumberOfGuests() {
		return numberOfGuests;
	}

	public void setNumberOfGuests(int numberOfGuests) {
		this.numberOfGuests = numberOfGuests;
	}

	public int getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(int phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
