package com.example.demo.ride;

import com.example.demo.map.HeremapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rides")
public class RideController {

    @Autowired
    private RideRepository rideRepository;
    @Autowired
    private HeremapService heremapService;


    @PostMapping
    public ResponseEntity<?> createRide(@RequestBody Ride ride) {
        Ride savedRide= rideRepository.save(ride);
        String origin= ride.getOrigin();
        String destination= ride.getDestination();
        try{
            String directions= heremapService.getDirections(origin,destination);
            savedRide.setDirections(directions);
            return ResponseEntity.ok(rideRepository.save(savedRide));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Not Found");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ride> getRideById(@PathVariable Long id) {
        Optional<Ride> ride = rideRepository.findById(id);
        return ride.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRide(@PathVariable Long id, @RequestBody Ride updatedRide) {
        Optional<Ride> existingRideOptional = rideRepository.findById(id);
        if (existingRideOptional.isPresent()) {
            Ride existingRide = existingRideOptional.get();
            String origin= existingRide.getOrigin();
            String destination= existingRide.getDestination();
            try{
                existingRide.setDriver(updatedRide.getDriver());
                existingRide.setCustomer(updatedRide.getCustomer());
                existingRide.setOrigin(updatedRide.getOrigin());
                existingRide.setDestination(updatedRide.getDestination());
                String directions = heremapService.getDirections(origin, destination);
                existingRide.setDirections(directions);
                existingRide.setFare(updatedRide.getFare());
                Ride savedRide = rideRepository.save(existingRide);
                return ResponseEntity.ok(savedRide);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.badRequest().body("Not Found");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRide(@PathVariable Long id) {
        Optional<Ride> ride = rideRepository.findById(id);
        if (ride.isPresent()) {
            rideRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public List<Ride> getAllRides() {
        return rideRepository.findAll();
    }
}
