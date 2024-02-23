package com.example.demo.car;
import com.example.demo.driver.Driver;
import jakarta.persistence.*;
import org.springframework.data.geo.Point;
@Entity
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String licensePlate;
    @OneToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;
    @Column(name = "hasPassenger")
    private boolean hasPassenger;
    @Column(name = "location")
    private Point location;
    public Car() {
    }
    public Car(String licensePlate, Driver driver, boolean hasPassenger, Point location) {
        this.licensePlate = licensePlate;
        this.driver = driver;
        this.hasPassenger = hasPassenger;
        this.location = location;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public boolean isHasPassenger() {
        return hasPassenger;
    }

    public void setHasPassenger(boolean hasPassenger) {
        this.hasPassenger = hasPassenger;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }
}

