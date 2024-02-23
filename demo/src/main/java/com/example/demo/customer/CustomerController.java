package com.example.demo.customer;

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
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer, Authentication authentication) {
        String roles = getRoles(authentication);
        if(roles.equals("ManagerCar")){
            return ResponseEntity.ok(customerRepository.save(customer));
        }
        else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Customer>> getCustomerById(@PathVariable Long id, Authentication authentication) {
        Optional<Customer> customer = customerRepository.findById(id);
        if (customer.isPresent()) {
            Customer customerExisting = customer.get();
            String roles = getRoles(authentication);
            if (roles.equals("ManagerCustomer")) {
                return ResponseEntity.ok(customer);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer updatedCustomer, Authentication authentication) {
        Optional<Customer> existingCustomerOptional = customerRepository.findById(id);
        if (existingCustomerOptional.isPresent()) {
            Customer existingCustomer = existingCustomerOptional.get();
            String roles = getRoles(authentication);
            if (roles.equals("ManagerCar")) {
                existingCustomer.setId(updatedCustomer.getId());
                existingCustomer.setName(updatedCustomer.getName());
                existingCustomer.setPhoneNumber(updatedCustomer.getPhoneNumber());
                existingCustomer.setLoyaltyPoints(updatedCustomer.getLoyaltyPoints());
                return ResponseEntity.ok(updatedCustomer);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id, Authentication authentication) {
        Optional<Customer> customer = customerRepository.findById(id);
        if (customer.isPresent()) {
            Customer customerExisting = customer.get();
            String roles = getRoles(authentication);
            if(roles.equals("ManagerCustomer")){
                customerRepository.deleteById(id);
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
            if (authority.getAuthority().equals("ManagerCustomer")) {
                return authority.getAuthority();
            }
        }
        return null;
    }
}

