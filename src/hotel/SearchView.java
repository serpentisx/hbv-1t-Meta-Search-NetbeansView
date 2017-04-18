package hotel;

import java.awt.EventQueue;

import javax.swing.JFrame;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.util.Date;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import javax.swing.border.CompoundBorder;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class SearchView extends Observable {

	private JFrame frame;
	private JPanel contentPane;
	private DefaultComboBoxModel<Integer> modelMonth;
	private JComboBox<Integer> comboStartDay;
	private JComboBox<Integer> comboStartMonth;
	private JLabel lblStartDate = new JLabel();
	private JComboBox<Integer> comboStartYear;
	private JPanel basicSearchPanel;
	private JComboBox<Integer> comboEndDay;
	private JComboBox<Integer> comboEndMonth;
	private JComboBox<Integer> comboEndYear;
	private JLabel lblEndDate;
	private JLabel lblHowMany;
	private JButton btnSearch;
	JCheckBox chckbxAdvancedOptions;
	JPanel advancedSearchPanel;

	private HotelManager hotelManager;
	private ResultsView resultsView;
	private ArrayList searchChoices = new ArrayList(16);
	private ArrayList<Hotel> searchResults = new ArrayList();
	private LocalDate startDate;
	private LocalDate endDate;
	private int numberOfGuests;
	private boolean[] priceRange = new boolean[5];
	private boolean[] openingMonths = new boolean[12];
	private boolean[] hotelRatings = new boolean[5];
	private boolean[] roomFacilities = new boolean[6];
	private boolean[] hotelFacilities = new boolean[6];
	private boolean[] hotelLocation = new boolean[6];
	private boolean[] airportLocation = new boolean[5];
	private boolean[] nearestCity = new boolean[5];
	private boolean[] sites = new boolean[8];
	private boolean[] dayTours = new boolean[6];
	
	private JPanel panelPrice;
	private JLabel lblPriceRange;
	private JCheckBox checkPrice0;
	private JCheckBox checkPrice1;
	private JCheckBox checkPrice2;
	private JCheckBox checkPrice3;
	private JCheckBox checkPrice4;
	private JPanel panelRating;
	private JLabel lblRating;
	private JCheckBox checkRating4;
	private JCheckBox checkRating3;
	private JCheckBox checkRating2;
	private JCheckBox checkRating1;
	private JCheckBox checkRating0;
	private JCheckBox chckbxSouthIceland;
	private JCheckBox chckbxEastIceland;
	private JCheckBox chckbxNorthIceland;
	private JCheckBox chckbxWestIceland;
	private JCheckBox chckbxWestfjords;
	private JPanel panelCities;
	private JLabel lblCities;
	private DefaultListModel<String> modelMajorCities = new DefaultListModel<String>();
	private DefaultListModel<String> modelHotelFacilities = new DefaultListModel<String>();
	private DefaultListModel<String> modelRoomFacilities = new DefaultListModel<String>();
	private DefaultListModel<String> modelSites = new DefaultListModel<String>();
	private DefaultListModel<String> modelDayTours = new DefaultListModel<String>();
	private JList<String> listCities;
	private JPanel panelRegions;
	private JLabel lblGeographicalRegions;
	private JCheckBox chckbxCapitalArea;
	private JScrollPane scrollPaneCities;
	private JPanel panelNearestAirport;
	private JLabel lblNearestAirport;
	private JCheckBox checkRKV;
	private JCheckBox checkKEF;
	private JCheckBox checkAEY;
	private JCheckBox checkEGS;
	private JCheckBox checkIFJ;
	private JPanel panelSites;
	private JLabel lblNearbySitesOf;
	private JScrollPane scrollPaneSites;
	private JList listSites;
	private JPanel panelHotelFacilities;
	private JLabel lblHotelFacilities;
	private JScrollPane scrollPaneHotelFacilities;
	private JList listHotelFacilities;
	private JPanel panelRoomFacilities;
	private JLabel lblRoomFacilities;
	private JScrollPane scrollPaneRoomFacilities;
	private JList listRoomFacilities;
	private JPanel panelDayTours;
	private JLabel lblNearbyDayTours;
	private JScrollPane scrollPaneDayTours;
	private JList listDayTours;
	private JButton btnResetSelection;
	private JLabel lblError;
	private JSpinner spinnerGuests;
	private JFormattedTextField tf;
	private JPanel panelName;
	private JLabel lblName;
	private JTextField textFieldName;
	private JPanel panelAddress;
	private JLabel lblAddress;
	private JTextField textFieldAddress;


	

	public SearchView() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1028, 601);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(50, 50, 300, 100));
		frame.getContentPane().add(contentPane, BorderLayout.CENTER);
		contentPane.setLayout(null);
		basicSearchPanel = new JPanel();
		basicSearchPanel.setBounds(12, 13, 966, 94);
		contentPane.add(basicSearchPanel);
		basicSearchPanel.setLayout(null);
		
		LocalDate today = LocalDate.now();
	
		comboStartDay = new JComboBox<Integer>();
		comboStartDay.setBounds(12, 35, 41, 22);
		basicSearchPanel.add(comboStartDay);
		//modelMonth = daysInMonth(today.getMonthValue());
		//comboStartDay.setModel(modelMonth);
		//comboStartDay.setModel(daysInMonth(today.getMonthValue()));
		comboStartDay.setModel(new DefaultComboBoxModel<Integer>(new Integer[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31}));
		//System.out.println(today.getDayOfMonth()-1);
		//comboStartDay.setSelectedIndex(modelMonth.getIndexOf(today));
		//System.out.println(today.getDayOfMonth()-1);
		//System.out.println(comboStartDay.getSelectedIndex((today.getDayOfMonth()-1)));
		comboStartDay.setSelectedIndex((Integer)(today.getDayOfMonth()-1));
		comboStartDay.setMaximumRowCount(8);

		comboStartMonth = new JComboBox<Integer>();
		comboStartMonth.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
	//			comboStartDay.setModel(daysInMonth(comboStartMonth.getSelectedIndex()+1));
			}
		});
		comboStartMonth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			//	comboStartDay.setModel(daysInMonth(comboStartMonth.getSelectedIndex()+1));
				//comboStartDay.setSelectedIndex(today.getDayOfMonth()-1);
				//comboStartMonth.getSelectedIndex()+1
			/*	int month = comboStartMonth.getSelectedIndex()+1;
				int year = today.getYear();
				switch(month)
				{ 
				case 1:
				case 3:
				case 5:
				case 7:
				case 8:
				case 10:
				case 12:
					comboStartDay.setModel(new DefaultComboBoxModel<Integer>(new Integer[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31}));
					break;
				case 4:
				case 6:
				case 9:
				case 11:
					comboStartDay.setModel(new DefaultComboBoxModel<Integer>(new Integer[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30}));
					break;
				case 2:
					if (((year % 4 == 0) && !(year % 100 == 0)) || (year % 400 == 0))
						comboStartDay.setModel(new DefaultComboBoxModel<Integer>(new Integer[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29}));
					else
						comboStartDay.setModel(new DefaultComboBoxModel<Integer>(new Integer[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28}));
					break;
				default:
					comboStartDay.setModel(new DefaultComboBoxModel<Integer>(new Integer[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31}));
					break;
				}*/
			}
		});
		comboStartMonth.setBounds(65, 35, 41, 22);
		basicSearchPanel.add(comboStartMonth);
		comboStartMonth.setModel(new DefaultComboBoxModel<Integer>(new Integer[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}));
		comboStartMonth.setSelectedIndex(today.getMonthValue()-1);
		
		
		comboStartYear = new JComboBox<Integer>();
		comboStartYear.setBounds(118, 35, 55, 22);
		basicSearchPanel.add(comboStartYear);
		comboStartYear.setModel(new DefaultComboBoxModel<Integer>(new Integer[] {2017, 2018, 2019}));
		comboStartYear.setSelectedIndex(today.getYear()-2017); //Note: If this were going live, we would use more complicated code, probably involving a separate function and autofill of the ComboBox
		
		
		
		
		lblStartDate.setBounds(54, 13, 68, 16);
		basicSearchPanel.add(lblStartDate);
		lblStartDate.setText("Start Date");
		
		comboEndDay = new JComboBox<Integer>();
		comboEndDay.setBounds(229, 35, 50, 22);
		comboEndDay.setModel(new DefaultComboBoxModel<Integer>(new Integer[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31}));
		basicSearchPanel.add(comboEndDay);
		comboEndDay.setSelectedIndex(today.getDayOfMonth()-1);
		
		comboEndMonth = new JComboBox<Integer>();
		/*
		 * comboEndMonth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comboEndDay.setModel(daysInMonth(comboStartMonth.getSelectedIndex()+1));
				comboEndDay.setSelectedIndex(today.getDayOfMonth()-1);
				int month = comboEndMonth.getSelectedIndex()+1;
				int year = today.getYear();
				switch(month)
				{ 
				case 1:
				case 3:
				case 5:
				case 7:
				case 8:
				case 10:
				case 12:
					comboEndDay.setModel(new DefaultComboBoxModel<Integer>(new Integer[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31}));
					break;
				case 4:
				case 6:
				case 9:
				case 11:
					comboEndDay.setModel(new DefaultComboBoxModel<Integer>(new Integer[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30}));
					break;
				case 2:
					if (((year % 4 == 0) && !(year % 100 == 0)) || (year % 400 == 0))
						comboEndDay.setModel(new DefaultComboBoxModel<Integer>(new Integer[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29}));
					else
						comboEndDay.setModel(new DefaultComboBoxModel<Integer>(new Integer[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28}));
					break;
				}
				
			}
		
		});
		*/
		comboEndMonth.setBounds(306, 35, 50, 22);
		comboEndMonth.setModel(new DefaultComboBoxModel<Integer>(new Integer[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}));
		comboEndMonth.setSelectedIndex(today.getMonthValue()-1);
		basicSearchPanel.add(comboEndMonth);
		
		comboEndYear = new JComboBox<Integer>();
		comboEndYear.setBounds(389, 35, 75, 22);
		comboEndYear.setModel(new DefaultComboBoxModel<Integer>(new Integer[] {2017, 2018, 2019}));
		comboEndYear.setSelectedIndex(today.getYear()-2017);
		basicSearchPanel.add(comboEndYear);
		
		lblEndDate = new JLabel("End date");
		lblEndDate.setBounds(293, 13, 56, 16);
		basicSearchPanel.add(lblEndDate);
		
		lblHowMany = new JLabel("How many?");
		lblHowMany.setBounds(489, 13, 75, 16);
		basicSearchPanel.add(lblHowMany);
		
		chckbxAdvancedOptions = new JCheckBox("Advanced \r\noptions");
		chckbxAdvancedOptions.setBounds(567, 9, 130, 25);
		chckbxAdvancedOptions.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if(chckbxAdvancedOptions.isSelected())
					advancedSearchPanel.setVisible(true);
				else
					advancedSearchPanel.setVisible(false);
			}
		});
		basicSearchPanel.add(chckbxAdvancedOptions);
		
		btnSearch = new JButton("Search!");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setChanged();
				notifyObservers();
				searchChoices.clear();
				lblError.setText("");
				if( gatherSelectedOptions() )
				{
					searchResults = hotelManager.searchHotel(searchChoices);
					setVisible(false);
					resultsView.receiveSelection(searchResults, startDate, endDate, numberOfGuests);
					resultsView.setVisible(true);
				}
				
			}
		});
		btnSearch.setBounds(577, 34, 97, 25);
		basicSearchPanel.add(btnSearch);
		
		lblError = new JLabel("");
		lblError.setBounds(509, 69, 281, 14);
		basicSearchPanel.add(lblError);
		
		spinnerGuests = new JSpinner();
		spinnerGuests.setModel(new SpinnerNumberModel(1, 1, 50, 1));
		spinnerGuests.setBounds(483, 36, 55, 25);
		spinnerGuests.setValue(1);
		tf = ((JSpinner.DefaultEditor)spinnerGuests.getEditor()).getTextField();
		tf.setEditable(false);
		basicSearchPanel.add(spinnerGuests);
		
		btnResetSelection = new JButton("Reset");
		btnResetSelection.setBounds(709, 35, 120, 23);
		basicSearchPanel.add(btnResetSelection);
		btnResetSelection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				clearAllOptions();
			}
		});
		
		advancedSearchPanel = new JPanel();
		advancedSearchPanel.setBounds(22, 118, 956, 433);
		contentPane.add(advancedSearchPanel);
		advancedSearchPanel.setLayout(null);
		advancedSearchPanel.setVisible(false);
		
		panelPrice = new JPanel();
		panelPrice.setBounds(0, 0, 124, 156);
		advancedSearchPanel.add(panelPrice);
		panelPrice.setPreferredSize(new Dimension(150, 230));
		panelPrice.setLayout(null);
		
				
		lblPriceRange = new JLabel("Price per night");
		lblPriceRange.setBounds(10, 0, 85, 14);
		panelPrice.add(lblPriceRange);
		
		checkPrice0 = new JCheckBox("0-5000kr");
		checkPrice0.setBounds(0, 21, 100, 23);
		panelPrice.add(checkPrice0);
		
		checkPrice1 = new JCheckBox("5-10.000kr");
		checkPrice1.setBounds(0, 47, 100, 23);
		panelPrice.add(checkPrice1);
		
		checkPrice2 = new JCheckBox("10-20.000kr");
		checkPrice2.setBounds(0, 73, 100, 23);
		panelPrice.add(checkPrice2);
		
		checkPrice3 = new JCheckBox("20-30.000kr");
		checkPrice3.setBounds(0, 99, 100, 23);
		panelPrice.add(checkPrice3);
		
		checkPrice4 = new JCheckBox("30.000+ kr");
		checkPrice4.setBounds(0, 125, 100, 23);
		panelPrice.add(checkPrice4);
		
		panelRating = new JPanel();
		panelRating.setBounds(0, 162, 110, 156);
		advancedSearchPanel.add(panelRating);
		panelRating.setPreferredSize(new Dimension(300, 250));
		panelRating.setLayout(null);
		
		lblRating = new JLabel("Rating");
		lblRating.setBounds(13, 0, 50, 14);
		panelRating.add(lblRating);
		lblRating.setPreferredSize(new Dimension(40, 14));
		
		checkRating0 = new JCheckBox("1 star");
		checkRating0.setBounds(0, 21, 60, 23);
		panelRating.add(checkRating0);
		
		checkRating1 = new JCheckBox("2 stars");
		checkRating1.setBounds(0, 47, 65, 23);
		panelRating.add(checkRating1);
		
		checkRating2 = new JCheckBox("3 stars");
		checkRating2.setBounds(0, 73, 65, 23);
		panelRating.add(checkRating2);
		
		checkRating3 = new JCheckBox("4 stars");
		checkRating3.setBounds(0, 99, 65, 23);
		panelRating.add(checkRating3);
		
		checkRating4 = new JCheckBox("5 stars");
		checkRating4.setBounds(0, 125, 65, 23);
		panelRating.add(checkRating4);
		
		panelRegions = new JPanel();
		panelRegions.setBounds(296, 0, 150, 178);
		advancedSearchPanel.add(panelRegions);
		panelRegions.setLayout(null);
		
		lblGeographicalRegions = new JLabel("Geographical regions");
		lblGeographicalRegions.setBounds(0, 0, 130, 14);
		panelRegions.add(lblGeographicalRegions);
		
		chckbxCapitalArea = new JCheckBox("Capital area");
		chckbxCapitalArea.setBounds(3, 21, 100, 23);
		panelRegions.add(chckbxCapitalArea);
		
		chckbxSouthIceland = new JCheckBox("South Iceland");
		chckbxSouthIceland.setBounds(3, 47, 110, 23);
		panelRegions.add(chckbxSouthIceland);
		
		chckbxEastIceland = new JCheckBox("East Iceland");
		chckbxEastIceland.setBounds(3, 73, 110, 23);
		panelRegions.add(chckbxEastIceland);
		
		chckbxNorthIceland = new JCheckBox("North Iceland");
		chckbxNorthIceland.setBounds(3, 99, 110, 23);
		panelRegions.add(chckbxNorthIceland);
		
		chckbxWestIceland = new JCheckBox("West Iceland");
		chckbxWestIceland.setBounds(3, 125, 110, 23);
		panelRegions.add(chckbxWestIceland);
		
		chckbxWestfjords = new JCheckBox("Westfjords");
		chckbxWestfjords.setBounds(3, 151, 110, 23);
		panelRegions.add(chckbxWestfjords);
		
		panelCities = new JPanel();
		panelCities.setBounds(639, 11, 155, 129);
		advancedSearchPanel.add(panelCities);
		panelCities.setLayout(null);
		
		lblCities = new JLabel("Nearest city or town");
		lblCities.setBounds(24, 0, 115, 14);
		panelCities.add(lblCities);
		
		modelMajorCities.addElement("Reykjavík");
		modelMajorCities.addElement("Keflavík");
		modelMajorCities.addElement("Akureyri");
		modelMajorCities.addElement("Egilsstaðir");
		modelMajorCities.addElement("Ísafjörður");
		
		scrollPaneCities = new JScrollPane();
		scrollPaneCities.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPaneCities.setBounds(0, 25, 147, 93);
		panelCities.add(scrollPaneCities);
		listCities = new JList<String>(modelMajorCities);
		scrollPaneCities.setViewportView(listCities);
		listCities.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		panelNearestAirport = new JPanel();
		panelNearestAirport.setBounds(296, 178, 155, 150);
		advancedSearchPanel.add(panelNearestAirport);
		panelNearestAirport.setLayout(null);
		
		lblNearestAirport = new JLabel("Nearest airport");
		lblNearestAirport.setBounds(0, 0, 90, 14);
		panelNearestAirport.add(lblNearestAirport);
		
		checkRKV = new JCheckBox("Reykjav\u00EDk");
		checkRKV.setBounds(0, 40, 97, 23);
		panelNearestAirport.add(checkRKV);
		
		checkKEF = new JCheckBox("Keflav\u00EDk International");
		checkKEF.setBounds(0, 14, 150, 23);
		panelNearestAirport.add(checkKEF);
		
		checkAEY = new JCheckBox("Akureyri");
		checkAEY.setBounds(0, 66, 97, 23);
		panelNearestAirport.add(checkAEY);
		
		checkEGS = new JCheckBox("Egilssta\u00F0ir");
		checkEGS.setBounds(0, 92, 97, 23);
		panelNearestAirport.add(checkEGS);
		
		checkIFJ = new JCheckBox("\u00CDsafj\u00F6r\u00F0ur");
		checkIFJ.setBounds(0, 120, 97, 23);
		panelNearestAirport.add(checkIFJ);
		
		panelSites = new JPanel();
		panelSites.setBounds(461, 11, 168, 279);
		advancedSearchPanel.add(panelSites);
		panelSites.setLayout(null);
		
		lblNearbySitesOf = new JLabel("Nearby sites \r\nof interest");
		lblNearbySitesOf.setBounds(10, 0, 140, 22);
		panelSites.add(lblNearbySitesOf);
		
		scrollPaneSites = new JScrollPane();
		scrollPaneSites.setBounds(20, 33, 105, 234);
		panelSites.add(scrollPaneSites);
		
		modelSites.addElement("Gullfoss");
		modelSites.addElement("Geysir");
		modelSites.addElement("Blue Lagoon");
		modelSites.addElement("Þingvellir");
		modelSites.addElement("Jökulsárlón");
		modelSites.addElement("Dettifoss");
		modelSites.addElement("Vatnajökull");
		modelSites.addElement("Jólahúsið");
		listSites = new JList<String>(modelSites);
		scrollPaneSites.setViewportView(listSites);
		
		panelHotelFacilities = new JPanel();
		panelHotelFacilities.setBounds(132, 0, 154, 172);
		advancedSearchPanel.add(panelHotelFacilities);
		panelHotelFacilities.setLayout(null);
		
		lblHotelFacilities = new JLabel("Hotel facilities");
		lblHotelFacilities.setBounds(35, 0, 92, 21);
		panelHotelFacilities.add(lblHotelFacilities);
		
		scrollPaneHotelFacilities = new JScrollPane();
		scrollPaneHotelFacilities.setBounds(10, 23, 117, 138);
		panelHotelFacilities.add(scrollPaneHotelFacilities);
		
		modelHotelFacilities.addElement("Gym");
		modelHotelFacilities.addElement("Restaurant");
		modelHotelFacilities.addElement("Bike rent");
		modelHotelFacilities.addElement("Handicap friendly");
		modelHotelFacilities.addElement("Dry cleaning");
		modelHotelFacilities.addElement("Parking lot");
		listHotelFacilities = new JList<String>(modelHotelFacilities);
		scrollPaneHotelFacilities.setViewportView(listHotelFacilities);
		
		panelRoomFacilities = new JPanel();
		panelRoomFacilities.setBounds(130, 172, 150, 156);
		advancedSearchPanel.add(panelRoomFacilities);
		panelRoomFacilities.setLayout(null);
		
		lblRoomFacilities = new JLabel("Room facilities");
		lblRoomFacilities.setBounds(39, 0, 95, 14);
		panelRoomFacilities.add(lblRoomFacilities);
		
		scrollPaneRoomFacilities = new JScrollPane();
		scrollPaneRoomFacilities.setBounds(10, 17, 130, 128);
		panelRoomFacilities.add(scrollPaneRoomFacilities);
		listRoomFacilities = new JList<String>(modelRoomFacilities);
		scrollPaneRoomFacilities.setViewportView(listRoomFacilities);
		
		modelRoomFacilities.addElement("Wi-Fi");
		modelRoomFacilities.addElement("Smoking");
		modelRoomFacilities.addElement("Pet friendly");
		modelRoomFacilities.addElement("Refrigerator");
		modelRoomFacilities.addElement("Combination safe");
		modelRoomFacilities.addElement("24hr room service");
		
		panelDayTours = new JPanel();
		panelDayTours.setBounds(638, 151, 156, 156);
		advancedSearchPanel.add(panelDayTours);
		panelDayTours.setLayout(null);
		
		lblNearbyDayTours = new JLabel("Nearby day tours");
		lblNearbyDayTours.setBounds(29, 1, 115, 14);
		panelDayTours.add(lblNearbyDayTours);
		
		scrollPaneDayTours = new JScrollPane();
		scrollPaneDayTours.setBounds(10, 23, 136, 122);
		panelDayTours.add(scrollPaneDayTours);
		
		modelDayTours.addElement("Hestaferð");
		modelDayTours.addElement("Söguganga");
		modelDayTours.addElement("Fjallganga");
		modelDayTours.addElement("Skíðaferð");
		modelDayTours.addElement("Þingvallahringur");
		modelDayTours.addElement("Reiðhjólatúr");
		listDayTours = new JList<String>(modelDayTours);
		scrollPaneDayTours.setViewportView(listDayTours);
		
		panelName = new JPanel();
		panelName.setBounds(0, 329, 262, 47);
		advancedSearchPanel.add(panelName);
		panelName.setLayout(null);
		
		lblName = new JLabel("Name");
		lblName.setBounds(10, 11, 36, 14);
		panelName.add(lblName);
		
		textFieldName = new JTextField();
		textFieldName.setBounds(45, 8, 187, 20);
		panelName.add(textFieldName);
		textFieldName.setColumns(10);
		
		panelAddress = new JPanel();
		panelAddress.setBounds(276, 339, 360, 33);
		advancedSearchPanel.add(panelAddress);
		panelAddress.setLayout(null);
		
		lblAddress = new JLabel("Address");
		lblAddress.setBounds(10, 11, 50, 15);
		panelAddress.add(lblAddress);
		
		textFieldAddress = new JTextField();
		textFieldAddress.setBounds(72, 8, 253, 20);
		panelAddress.add(textFieldAddress);
		textFieldAddress.setColumns(10);
						
				
	}

	public void setVisible(boolean state)
	{
		frame.setVisible(state);
		contentPane.setVisible(state);
		contentPane.repaint();	
	}
	
	public void addHotelManager(HotelManager hm)
	{
		this.hotelManager = hm;
	}
	
	public void addResultsView(ResultsView rv)
	{
		this.resultsView = rv;
	}
	
	private boolean gatherSelectedOptions()
	{
		searchChoices.clear();
		LocalDate today = LocalDate.now();
		
		//Our 16 parameters, in ascending order
		
		/*0*/ 
		int day = (Integer)comboStartDay.getSelectedItem();
		int month = (Integer)comboStartMonth.getSelectedItem();
		int year = (Integer)comboStartYear.getSelectedItem();
		startDate = LocalDate.of(year, month, day);
		if(startDate.isBefore(today))
		{
			lblError.setText("Please select a valid starting date");
			return false;
		}
		searchChoices.add(startDate); //0
		
		/*1*/
		day = (Integer)comboEndDay.getSelectedItem();
		month = (Integer)comboEndMonth.getSelectedItem();
		year = (Integer)comboEndYear.getSelectedItem();
		endDate = LocalDate.of(year, month, day);
		if(endDate.isBefore(startDate))
		{
			lblError.setText("Please select valid Start and End dates");
			return false;
		}
		searchChoices.add(endDate); //1
		
		/*2*/
		numberOfGuests = (Integer) spinnerGuests.getValue();
		searchChoices.add(numberOfGuests); //2
		
		/*3*/
		if(textFieldName.getText().equals(""))
			searchChoices.add(null);
		else
		{
			String hotelName = textFieldName.getText();
			searchChoices.add(hotelName);
		} //3
		
		/*4*/
		Arrays.fill(priceRange, false);
		priceRange[0] = checkPrice0.isSelected();
		priceRange[1] = checkPrice1.isSelected();
		priceRange[2] = checkPrice2.isSelected();
		priceRange[3] = checkPrice3.isSelected();
		priceRange[4] = checkPrice4.isSelected();
		searchChoices.add(priceRange); //4
				
		/*5*/ 
		/* Unimplemented; left in design for v2.0. We concluded there was
		 * no valid reason to allow a search for opening months: A search
		 * isn't valuable unless it's certain you can stay there, and if
		 * you already know what time periods you'd want, you'll simply
		 * run searches choosing those particular dates. If they come
		 * up empty, then the hotel didn't have rooms available, no matter
		 * whether they were reserved or the hotel closed.
		 * */
		Arrays.fill(openingMonths, true); 
		searchChoices.add(openingMonths); //5
		
		/*6*/ 
		if(textFieldAddress.getText().equals(""))
			searchChoices.add(null);
		else
		{
			String hotelAddress = textFieldAddress.getText();
			searchChoices.add(hotelAddress); //6
		}
		
		/*7*/
		hotelRatings[0] = checkRating0.isSelected();
		hotelRatings[1] = checkRating1.isSelected();
		hotelRatings[2] = checkRating2.isSelected();
		hotelRatings[3] = checkRating3.isSelected();
		hotelRatings[4] = checkRating4.isSelected();
		searchChoices.add(hotelRatings); //7

		/*8*/
		Arrays.fill(roomFacilities, false);
		int[] roomSelections = listRoomFacilities.getSelectedIndices();
		if(roomSelections.length < 1) //Since nothing was selected, this option won't count in our search.
			Arrays.fill(roomFacilities, true);
		else //User selected something, so let's adhere to their choices.
		{
			for(int i : roomSelections)
				roomFacilities[i] = true;
		}
		searchChoices.add(roomFacilities); //8
		
		/*9*/ 
		/* Unimplemented; left in design for v2.0. 
		 * Since we've implemented hotel facilities, room facilities,
		 * hotel locations and nearby sites of interest, there is no need
		 * to narrow searches down by types: hotel, motel, bed&breakfast, etc.
		 * This feature might have been useful if we had decided to allow 
		 * for hotel chains, since users might want to look only for
		 * Hotel Eddas and so on; so we've left the framework for it here. 
		 */
		boolean[] hotelType = new boolean[5];
		Arrays.fill(hotelType, true);
		searchChoices.add(hotelType);	//9
		
		/*10*/ 
		Arrays.fill(hotelFacilities, false);
		int[] facilitySelections = listHotelFacilities.getSelectedIndices();
		if(facilitySelections.length < 1) //Since nothing was selected, this option won't count in our search.
			Arrays.fill(hotelFacilities, true);
		else //User selected something, so let's adhere to their choices.
		{
			for(int i : facilitySelections)
				hotelFacilities[i] = true;
		}
		searchChoices.add(hotelFacilities); //10
	
		
		/*11*/
		/* We're using boolean[] rather than int[] - but we've designed the 
		 * location system so that it can be extended to cover specific postal 
		 * area codes in v.2, rather than always having to adhere to a much larger
		 * arbitrary parting of Iceland's main regions. In our Control class, 
		 * we match these numbers to ranges of postal codes that correspond to 
		 * official N-S-E-W region divisions in Iceland.
		 */
		hotelLocation[0]=chckbxCapitalArea.isSelected();
		hotelLocation[1]=chckbxSouthIceland.isSelected();
		hotelLocation[2]=chckbxEastIceland.isSelected();
		hotelLocation[3]=chckbxNorthIceland.isSelected();
		hotelLocation[4]=chckbxWestIceland.isSelected();
		hotelLocation[5]=chckbxWestfjords.isSelected();
		searchChoices.add(hotelLocation); //11
	
		/*12*/
		Arrays.fill(nearestCity, false);
		int[] citySelections = listCities.getSelectedIndices();
		if(citySelections.length < 1) //Since nothing was selected, this option won't count in our search.
			Arrays.fill(nearestCity, true);
		else //User selected something, so let's adhere to their choices.
		{
			for(int i : citySelections)
				nearestCity[i] = true;
		}
		searchChoices.add(nearestCity); //10
		
		
		/*13*/
		airportLocation[0]=checkKEF.isSelected();
		airportLocation[1]=checkRKV.isSelected();
		airportLocation[2]=checkAEY.isSelected();
		airportLocation[3]=checkEGS.isSelected();
		airportLocation[4]=checkIFJ.isSelected();
		searchChoices.add(airportLocation); //13
		
		/*14*/
		Arrays.fill(sites, false);
		int[] siteSelections = listSites.getSelectedIndices();
		if(siteSelections.length < 1) //Since nothing was selected, this option won't count in our search.
			Arrays.fill(sites, true);
		else //User selected something, so let's adhere to their choices.
		{
			for(int i : siteSelections)
				sites[i] = true;
		}
		searchChoices.add(sites); //14
		
		
		/*15*/
		Arrays.fill(dayTours, false);
		int[] tourSelections = listDayTours.getSelectedIndices();
		if(tourSelections.length < 1) //Since nothing was selected, this option won't count in our search.
			Arrays.fill(dayTours, true);
		else //User selected something, so let's adhere to their choices.
		{
			for(int i : tourSelections)
				dayTours[i] = true;
		}
		searchChoices.add(dayTours); //15
						
		return true; //No conditions broken, so we can safely claim searchChoices is correctly filled

	}

	private void testSearchFunction()
	/*Proof-of-concept for other groups*/
	{
		ArrayList searchParameters = new ArrayList(16);

		
		//Our 16 parameters, in ascending order
		/*0*/ startDate = LocalDate.of(2017, 06, 02);
		/*1*/ endDate = LocalDate.of(2017, 06, 04);
		/*2*/ numberOfGuests = 5;
		/*3*/ String hotelName = null;
		/*4*/ boolean[] priceRange = new boolean[5]; //Beware: Size initialization also sets all entries to FALSE
					Arrays.fill(priceRange, true); 
					//priceRange[3]=true; //Testing for a search on only range 4
		/*5*/ boolean[] openingMonths = new boolean[12];
				Arrays.fill(openingMonths, true); //Replace this line with "a[b]=true" assignments if you want to test the parameter
		/*6*/ String hotelAddress = null;
		/*7*/ boolean[] hotelRatings = new boolean[5];
					Arrays.fill(hotelRatings, true);
		/*8*/ boolean[] roomFacilities = new boolean[6];
					Arrays.fill(roomFacilities, true);
		/*9*/ boolean[] hotelType = new boolean[5];
					Arrays.fill(hotelType, true);
		/*10*/ boolean[] hotelFacilities = new boolean[6];
					Arrays.fill(hotelFacilities, true);
		/*11*/ boolean[] hotelLocation = new boolean[6]; //Will later be matched to a hotel's postal area code
		/*12*/ boolean[] nearestCity = new boolean[5];
		/*13*/ boolean[] nearestAirport = new boolean[6];
		/*14*/ boolean[] nearestSite = new boolean[8];
		/*15*/ boolean[] nearestDayTour = new boolean[6];
		 
		searchParameters.add(startDate); //0
		searchParameters.add(endDate); //1
		searchParameters.add(numberOfGuests); //2
		searchParameters.add(hotelName); //3
		searchParameters.add(priceRange); //4
		searchParameters.add(openingMonths); //5
		searchParameters.add(hotelAddress); //6
		searchParameters.add(hotelRatings); //7
		searchParameters.add(roomFacilities); //8
		searchParameters.add(hotelType);	//9
		searchParameters.add(hotelFacilities); //10
		searchParameters.add(hotelLocation); //11
		searchParameters.add(nearestCity); //12
		searchParameters.add(nearestAirport); //13
		searchParameters.add(nearestSite); //14
		searchParameters.add(nearestDayTour); //15
		
		//Use this to "fill" the array if you don't add search parameters.
		//Otherwize you'll get an error when referencing values that are unfilled,
		//EVEN THOUGH you may have specified the array size.
		/*for(int i = 0; i<16; i++)
			searchParameters.add(null);*/
		
		/* Header: searchHotel(p) looks for all hotels matching p (which must be size & capacity 16).
		 *	The ArrayList called "parameters" should be of length 16. It contains all possible search conditions:
		 * (0) start date (LocalDate) 
		 * (1) end date (LocalDate)
		 * (2) number of guests (int)
		 * (3) hotel name (String)
		 * (4) price range (boolean[], length 5)
		 * (5) opening months (boolean[], length 12)
		 * (6) address (String) 
		 * (7) ratings (boolean[], length 5)
		 * (8) room facilities (boolean[], length 6)
		 * (9) hotel type (boolean[], length 5)
		 * (10) hotel facilities (boolean[], length 6)
		 * (11) hotel area location (boolean[], length 6 - is eventually matched to a hotel's 3-digit int postal code)   
		 * (12) nearest city (boolean[], length 5)
		 * (13) nearest airport (boolean[], length 6)
		 * (14) nearest sites (boolean[], length 8)
		 * (15) nearest day tours (boolean[], length 6)  
		 * 
		 * */

		
		
		//Let's search for...
		//Any hotel at all, for 10 people, on June 2-3rd.
		//searchResults = new ArrayList<Hotel>(hotelManager.searchHotel(startDate, endDate, guests));
		
		
		searchResults = new ArrayList<Hotel>(hotelManager.searchHotel(searchParameters));
		//searchResults = new ArrayList<Hotel>(hotelManager.searchHotel(startDate, endDate, guests));
		//System.out.println("Size of searchResults is "+searchResults.size());
		//for(Hotel h : searchResults)
			//System.out.println("Found hotel "+(String)(h.getName()));

		
		
		
		/*
		//Part of working code for our own UI, commenting it out
		//while we test code functionality for controller, model and storage layers
		gatherSelectedOptions();
		searchChoices.clear();
		searchChoices.add(comboEndYear.getSelectedItem());
		*/
		
	}

	private void clearAllOptions()
	{
		searchChoices.clear();
		LocalDate today = LocalDate.now();
		
		comboStartYear.setSelectedIndex(today.getYear()-2017); //Note: If this were going live, we would use more complicated code, probably involving a separate function and autofill of the ComboBox
		comboStartMonth.setSelectedIndex(today.getMonthValue()-1);
		comboStartDay.setSelectedIndex(today.getDayOfMonth()-1);
		
		comboEndYear.setSelectedIndex(today.getYear()-2017);
		comboEndMonth.setSelectedIndex(today.getMonthValue()-1);
		comboEndDay.setSelectedIndex(today.getDayOfMonth()-1);
		

		spinnerGuests.setValue(1);
		chckbxAdvancedOptions.setSelected(false);
		lblError.setText("");
		
		checkPrice0.setSelected(false);
		checkPrice1.setSelected(false);
		checkPrice2.setSelected(false);
		checkPrice3.setSelected(false);
		checkPrice4.setSelected(false);
				
		checkRating0.setSelected(false);
		checkRating1.setSelected(false);
		checkRating2.setSelected(false);
		checkRating3.setSelected(false);
		checkRating4.setSelected(false);
		
				
		chckbxCapitalArea.setSelected(false);
		chckbxSouthIceland.setSelected(false);
		chckbxEastIceland.setSelected(false);
		chckbxNorthIceland.setSelected(false);
		chckbxWestIceland.setSelected(false);
		chckbxWestfjords.setSelected(false);

		listCities.clearSelection();
				
		checkRKV.setSelected(false);				
		checkKEF.setSelected(false);				
		checkAEY.setSelected(false);				
		checkEGS.setSelected(false);
		checkIFJ.setSelected(false);
		
		listSites.clearSelection();
		
		listHotelFacilities.clearSelection();
		
		listRoomFacilities.clearSelection();
		
		listDayTours.clearSelection();
		
		textFieldName.setText("");
		
		textFieldAddress.setText("");
	}

	private DefaultComboBoxModel<Integer> daysInMonth(int m)
	{
		DefaultComboBoxModel<Integer> days;
		LocalDate today = LocalDate.now();
		
		//int month = comboStartMonth.getSelectedIndex()+1;
		int month = m;
		int year = today.getYear();
		switch(month)
		{ 
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			days = new DefaultComboBoxModel<Integer>(new Integer[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31});
			break;
		case 4:
		case 6:
		case 9:
		case 11:
			days = new DefaultComboBoxModel<Integer>(new Integer[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30});
			break;
		case 2:
			if (((year % 4 == 0) && !(year % 100 == 0)) || (year % 400 == 0))
				days = new DefaultComboBoxModel<Integer>(new Integer[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29});
			else
				days = new DefaultComboBoxModel<Integer>(new Integer[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28});
			break;
		default:
			days = new DefaultComboBoxModel<Integer>(new Integer[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31});
			break;
		}
		return days;
	}
	
	
}
