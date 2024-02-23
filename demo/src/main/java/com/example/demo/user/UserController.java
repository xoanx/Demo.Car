package com.example.demo.user;

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
@RequestMapping("/api/users")

public class UserController {
        @Autowired
        private UserRepository userRepository;
        @PostMapping
        public ResponseEntity<Object> createUser(@RequestBody User user, Authentication authentication) {
            String roles = getRoles(authentication);
            if(roles.equals("ADMIN")){
                return ResponseEntity.ok(userRepository.save(user));
            }
            else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
        @GetMapping("/{id}")
        public ResponseEntity<Optional<User>> getUserById(@PathVariable Long id, Authentication authentication) {
            Optional<User> user = userRepository.findById(id);
            if(user.isPresent()){
                User userExisting = user.get();
                String roles= getRoles(authentication);
                if(roles.equals("ADMIN")){
                    return ResponseEntity.ok(user);
                }
                else{
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                }
            }
            else {
                return ResponseEntity.notFound().build();
            }
        }
        @PutMapping("/{id}")
        public ResponseEntity<User> updateUser(@PathVariable Long id, Authentication authentication, @RequestBody User updatedUser) {
            Optional<User> existingUserOptional = userRepository.findById(id);
            if (existingUserOptional.isPresent()) {
                User existingUser = existingUserOptional.get();
                String roles= getRoles(authentication);
                if(roles.equals("ADMIN")){
                    existingUser.setId(updatedUser.getId());
                    existingUser.setUserName(updatedUser.getUserName());
                    existingUser.setPassWord(updatedUser.getPassWord());
                    existingUser.setRoles(updatedUser.getRoles());
                    User updated = userRepository.save(existingUser);
                    return ResponseEntity.ok(updated);
                }
                else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                } 
            } else {
                return ResponseEntity.notFound().build();
            }
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteUser(@PathVariable Long id,  Authentication authentication) {
            Optional<User> user = userRepository.findById(id);
            if (user.isPresent()) {
                User userExisting = user.get();
                String roles= getRoles(authentication);
                if(roles.equals("ADMIN")){
                    userRepository.deleteById(id);
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
            if (authority.getAuthority().equals("ADMIN")) {
                return authority.getAuthority();
            }
        }
        return null;
    }
}


