package hotel;

import java.util.Observable;

import javax.swing.DefaultListModel;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JFrame;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.jdatepicker.impl.*;
import java.util.Date;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.Date;
import javax.swing.border.CompoundBorder;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;


public class ResultsView extends Observable {

	private HotelManager hm;
	private SearchView searchView;
	private ReservationView reservationView;
	private ArrayList<Hotel> searchResults = new ArrayList();
	private Hotel selectedHotel;
	private LocalDate startDate, endDate;
	private int numberOfGuests;
	
	private JFrame frame;
	private JPanel contentPane;
	private JPanel panel;
	private DefaultListModel model = new DefaultListModel();
	private JList list;
	private JLabel lblStartingDate;
	private JLabel lblEndingDate;
	private JLabel lblGuests;
	private JButton btnReserveARoom;
	


	/**
	 * Create the frame.
	 */
	public ResultsView() {
		frame = new JFrame();
		frame.setBounds(100, 100, 619, 455);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		frame.setContentPane(contentPane);
		contentPane.setLayout(null);
		
		panel = new JPanel();
		panel.setBounds(10, 11, 583, 254);
		contentPane.add(panel);
		panel.setLayout(null);
		
		btnReserveARoom = new JButton("Reserve a Room");
		btnReserveARoom.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				int size = model.getSize();
				int selection = list.getSelectedIndex();
				if((size>0) && (selection>=0))
				{
					selectedHotel = (Hotel)searchResults.get(selection);
					setVisible(false);
					reservationView.receiveSelection(selectedHotel, startDate, endDate, numberOfGuests);
					reservationView.setVisible(true);
				}
			}
		});
		btnReserveARoom.setBounds(460, 218, 111, 23);
		panel.add(btnReserveARoom);
		
		lblStartingDate = new JLabel("Starting date: ");
		lblStartingDate.setBounds(31, 10, 180, 16);
		panel.add(lblStartingDate);
		
		lblEndingDate = new JLabel("Ending date:");
		lblEndingDate.setBounds(238, 10, 180, 16);
		panel.add(lblEndingDate);
		
		lblGuests = new JLabel("Guests: ");
		lblGuests.setBounds(460, 10, 85, 16);
		panel.add(lblGuests);
		
		list = new JList(model);
		list.setBounds(31, 53, 500, 118);
		panel.add(list);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				searchView.setVisible(true);
			}
		});
		btnBack.setBounds(10, 218, 89, 23);
		panel.add(btnBack);
	}

	public void addHotelManager(HotelManager hm)
	{
		this.hm = hm;
	}
	
	public void addSearchView(SearchView sv)
	{
		this.searchView = sv;
	}

	public void addReservationView(ReservationView rv)
	{
		this.reservationView = rv;
	}

	public void setVisible(boolean state){
		frame.setVisible(state);
		contentPane.setVisible(state);
		contentPane.repaint();	
	}
	
	public void receiveSelection(ArrayList<Hotel> selection, LocalDate startDate, LocalDate endDate, int numberOfGuests)
	{
		this.startDate = startDate;
		this.endDate = endDate;
		this.numberOfGuests = numberOfGuests;
		searchResults.clear();
		for(Hotel h : selection)
			searchResults.add(h);
		lblStartingDate.setText("Starting date: "+startDate.toString());;
		lblEndingDate.setText("Ending date: "+endDate.toString());;
		lblGuests.setText("Guests: "+numberOfGuests);

		displayOptions();
	}
	
	private void displayOptions()
	{
		model.clear();
		int i = 0;
		for(Hotel h : searchResults)
		{
			model.add(i, h.getName());
			i++;
		}

	}
}
