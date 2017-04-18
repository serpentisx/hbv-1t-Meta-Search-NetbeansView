
package flight;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author ingohuyy
 */
public class FlightGenerator {
    private String filename;
    private List<Airport> airports;
    private List<Flight> inboundFlights;
    private List<Flight> outboundFlights;
    private SimpleDateFormat fmt;
    
    public FlightGenerator(String filename) {
        this.filename = filename;
        this.airports = generateAirports();
        this.inboundFlights = generateInboundFlights();
        this.outboundFlights = generateOutboundFlights();
        fmt = new SimpleDateFormat("yyyyMMdd");
    }
    
    public List<Flight> search(Date departing, String origin, String destination, int passengers) {
        if (origin.contains("Iceland")) {
            return searchInboundFlights(departing, destination, passengers);
        }
        return searchOutboundFlights(departing, destination, passengers);
    }
    
    public List<Flight> searchInboundFlights(Date departing, String destination, int passengers) {
        List<Flight> match = new ArrayList<>();
        LocalDate date = departing.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Date date1 = Date.from(date.minusDays(10).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date date2 = Date.from(date.plusDays(10).atStartOfDay(ZoneId.systemDefault()).toInstant());
        for (Flight f : inboundFlights) {
            if (f.getDepartureTime().after(date1) && f.getDepartureTime().before(date2) && destination.equals(f.getDestination()) && f.getSeatsAvailable() >= passengers) {
                match.add(f);
            }
        }
        return match;
    }

    public List<Flight> searchOutboundFlights(Date departing, String origin, int passengers) {
        List<Flight> match = new ArrayList<>();
        LocalDate date = departing.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Date date1 = Date.from(date.minusDays(10).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date date2 = Date.from(date.plusDays(10).atStartOfDay(ZoneId.systemDefault()).toInstant());
        for (Flight f : outboundFlights) {

            if (f.getDepartureTime().after(date1) && f.getDepartureTime().before(date2) && origin.equals(f.getOrigin()) && f.getSeatsAvailable() >= passengers) {
                match.add(f);
            }
        }
        return match;
    }

    private List<Airport> generateAirports() {
        List<Airport> airport = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(new File(filename)))) {
            String line;
            String[] s;
            while ((line = br.readLine()) != null) {
                s = line.split(",");
                String airportName = s[1].substring(1, s[1].length() - 1);
                String city = s[2].substring(1, s[2].length() - 1);
                String country = s[3].substring(1, s[3].length() - 1);
                String airportcode = s[4].substring(1, s[4].length() - 1);
                
                Airport ap = new Airport(airportName, city, country, airportcode);
                airport.add(ap);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File containing airport codes was not found");
        } catch (IOException e) {
            System.out.println("Reading airport codes resulted in an IOException");
        }
        return airport;
    }
    
    // mess code
    private List<Flight> generateOutboundFlights() {
        List<Flight> flights = new ArrayList<>();
        
        for (Airport ap : airports) {
            LocalDate start = LocalDate.now();
            LocalDate end = start.plusDays(100);
            for (LocalDate date = start; date.isBefore(end); date = date.plusDays(1)) {
                long randomTime = ((long) (Math.random() * 43200000)) + 14400000;
                int numberOfSeats = (int) (Math.random()*201 + 100);
                long today = date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                long flightTime = (long) (Math.random() * 86400000);
                Date departure = new Date(today + flightTime);
                Date arrival = new Date(today + flightTime + randomTime);
                
                int booked = (int) (Math.random()*numberOfSeats) + 1;
                int available = numberOfSeats - booked; 
                int price = (int) (Math.random()*1001); 
                
                String from = ap.getName() + " (" + ap.getAirportCode() + "), " + ap.getCountry();
                String to = "Keflavik International Airport (KEF), Iceland";
                
                Flight flight = new Flight("FI-200", "Icelandair", from, to, departure, arrival, available, price);
                flights.add(flight);    
            }
        }
        return flights;
    }
    
    private List<Flight> generateInboundFlights() {
        List<Airport>  airports = generateAirports();
        
        List<Flight> flights = new ArrayList<>();
        
        for (Airport ap : airports) {
            LocalDate start = LocalDate.now();
            LocalDate end = start.plusDays(100);
            for (LocalDate date = start; date.isBefore(end); date = date.plusDays(1)) {
                long randomTime = ((long) (Math.random() * 43200000)) + 14400000;
                int numberOfSeats = (int) (Math.random()*201 + 100);
                long today = date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                long flightTime = (long) (Math.random() * 86400000);
                Date departure = new Date(today + flightTime);
                Date arrival = new Date(today + flightTime + randomTime);
                
                int booked = (int) (Math.random()*numberOfSeats) + 1;
                int available = numberOfSeats - booked; 
                int price = (int) (Math.random()*1001); 
                
                String from = ap.getName() + " (" + ap.getAirportCode() + "), " + ap.getCountry();
                String to = "Keflavik International Airport (KEF), Iceland";
                
                Flight flight = new Flight("FI-200", "Icelandair",to, from, departure, arrival, available, price);
                flights.add(flight);    
            }
        }
        return flights;
    }
    
    public static void main(String args[]) {
        FlightGenerator generator = new FlightGenerator("airportCodes.txt");
        
     /*   List<Flight> flights = generator.generateOutboundFlights();
        System.out.println("Outbound flights size : " + flights.size());
        for (Flight f : flights) {
            System.out.println("From : " + f.getOrigin());
            System.out.println("To : " + f.getDestination());
            System.out.println("Depart time : " + f.getDepartureTime());
            System.out.println("Arrival time : " + f.getArrivalTime());
            System.out.println("Price : " + f.getPrice());
            System.out.println("--------------\n");
        }*/
        
        for (Flight f : generator.searchOutboundFlights(new Date(System.currentTimeMillis() + 259200000), "Tan Son Nhat International Airport (SGN), Vietnam", 0)) {
            System.out.println("From : " + f.getOrigin());
            System.out.println("To : " + f.getDestination());
            System.out.println("Depart time : " + f.getDepartureTime());
            System.out.println("Arrival time : " + f.getArrivalTime());
            System.out.println("Price : " + f.getPrice());
            System.out.println("--------------\n");
        }
    }

    public List<Flight> getInboundFlights() {
        return inboundFlights;
    }

    public List<Flight> getOutboundFlights() {
        return outboundFlights;
    }

    public List<Airport> getAirports() {
        return airports;
    }
}
