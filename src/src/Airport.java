


package src;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ingohuyy
 */
public class Airport implements Comparable<Airport>{
    
    private String name;
    private String city;
    private String country;
    private String airportCode;
    
    public Airport(String name, String city, String country, String airportCode) {
        this.name = name;
        this.city = city;
        this.country = country;
        this.airportCode = airportCode;
    }
    
    @Override
    public int compareTo(Airport o) {
        return this.name.compareTo(o.getName());
    }
    
    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getAirportCode() {
        return airportCode;
    }
}
