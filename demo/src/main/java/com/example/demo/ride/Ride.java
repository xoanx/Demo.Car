package com.example.demo.ride;
import com.example.demo.customer.Customer;
import com.example.demo.driver.Driver;
import jakarta.persistence.*;
@Entity
public class Ride {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @Column(name = "origin")
    private String origin;
    @Column(name = "destination")
    private String destination;
    @Column(name = "directions")
    private String directions;
    @Column(name = "fare")
    private double fare;
    public Ride() {
    }
    public Ride(Long id, Driver driver, Customer customer, String origin, String destination, String directions, double fare) {
        this.id = id;
        this.driver = driver;
        this.customer = customer;
        this.origin = origin;
        this.destination = destination;
        this.directions=directions;
        this.fare = fare;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Driver getDriver() {
        return driver;
    }
    public void setDriver(Driver driver) {
        this.driver = driver;
    }
    public Customer getCustomer() {
        return customer;
    }
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    public String getOrigin() {
        return origin;
    }
    public void setOrigin(String source) {
        this.origin = source;
    }
    public String getDestination() {
        return destination;
    }
    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDirections() {
        return directions;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }

    public double getFare() {
        return fare;
    }
    public void setFare(double fare) {
        this.fare = fare;
    }
}

