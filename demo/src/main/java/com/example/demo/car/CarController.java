package com.example.demo.car;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/api/cars")
public class CarController {
    @Autowired
    private CarRepository carRepository;
    @PostMapping
    public ResponseEntity<Car> createCar(@RequestBody Car car, Authentication authentication) {
        String roles = getRoles(authentication);
        if(roles.equals("ManagerCar")){
            return ResponseEntity.ok(carRepository.save(car));
        }
        else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Car>> getCarById(@PathVariable Long id, Authentication authentication) {
        Optional<Car> car = carRepository.findById(id);
        if (car.isPresent()) {
            Car carExisting = car.get();
            String roles = getRoles(authentication);
            if (roles.equals("ManagerCar")) {
                return ResponseEntity.ok(car);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<Car> updateCar(@PathVariable Long id, @RequestBody Car updatedCar, Authentication authentication) {
        Optional<Car> existingCarOptional = carRepository.findById(id);
        if (existingCarOptional.isPresent()) {
            Car existingCar = existingCarOptional.get();
            String roles = getRoles(authentication);
            if (roles.equals("ManagerCar")) {
                existingCar.setLicensePlate(updatedCar.getLicensePlate());
                existingCar.setDriver(updatedCar.getDriver());
                existingCar.setHasPassenger(updatedCar.isHasPassenger());
                existingCar.setLocation(updatedCar.getLocation());
                Car updated = carRepository.save(existingCar);
                return ResponseEntity.ok(updated);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id, Authentication authentication) {
        Optional<Car> car = carRepository.findById(id);
        if (car.isPresent()) {
            Car carExisting = car.get();
            String roles = getRoles(authentication);
            if(roles.equals("ManagerCar")){
                carRepository.deleteById(id);
                return ResponseEntity.noContent().build();
            }
            else{
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @Nullable
    private String getRoles(@NotNull Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("ManagerCar")) {
                return authority.getAuthority();
            }
        }
        return null;
    }
}
