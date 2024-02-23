package com.example.acterioexercise.Controller;

import com.example.acterioexercise.Domain.AuthenticationRequest;
import com.example.acterioexercise.Domain.RequestUpdateEmail;
import com.example.acterioexercise.Domain.User;
import com.example.acterioexercise.SecurityConfig;
import com.example.acterioexercise.Service.UserAlreadyExistsException;
import com.example.acterioexercise.Service.UserService;
import com.example.acterioexercise.Util.Utility;
import org.hibernate.mapping.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    SecurityConfig securityConfig;

    @PostMapping("/user")
    ResponseEntity<User> createUser(@RequestBody User user) throws UserAlreadyExistsException {
        return ResponseEntity.created(URI.create(ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString()))
                .body(userService.createUser(user));
    }

    @PostMapping("/authenticate")
    ResponseEntity<String> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {

        Optional<String> storedPassword = userService.findPasswordByEmail(authenticationRequest.getEmail());

        // Check if the stored password exists and matches the provided password
        if (storedPassword.isPresent() && securityConfig.getPasswordEncoder().matches(authenticationRequest.getPassword(), storedPassword.get())) {
            return ResponseEntity.ok("Authentication successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
        }
    }

    @GetMapping("/emails")
    ResponseEntity<Map<String, Long>> emails() {
        Optional<List<String>> optionalList = userService.findAllDomains();
        if (optionalList.isPresent()) {
            List<String> listOfDomains = optionalList.get();

            // Create a map to store domain counts
            Map<String, Long> domainCounts = new HashMap<>();
            for (String domain : listOfDomains) {
                domain = Utility.extractDomainFromEmail(domain);
                domainCounts.put(domain, domainCounts.getOrDefault(domain, 0L) + 1);
            }

            return ResponseEntity.ok(domainCounts);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/user/{id}")
    ResponseEntity<Object> deleteUserById(@PathVariable long id){
        Optional<User> user = userService.getUserById(id);

        if(user.isPresent()){
            userService.deleteUserById(id);
            return new ResponseEntity<>("Deleted user with id " + id, HttpStatus.OK);

        }
        return new ResponseEntity<>("Couldn't find user with id " + id, HttpStatus.NOT_FOUND);
    }

    @PutMapping("/user/{id}")
    ResponseEntity<Object> updateUser(@PathVariable long id,@RequestBody User user){
        Optional<User> currentUser = userService.getUserById(id);

        //Should do this probably in the service class
        if(currentUser.isPresent()){
            User updatedUser = currentUser.get();
            updatedUser.setFirstName(user.getFirstName());
            updatedUser.setLastName(user.getLastName());
            updatedUser.setEmail(user.getEmail());
            userService.saveUser(updatedUser);
            return ResponseEntity.ok(updatedUser);
        }
       return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with ID: " + id);
    }

    @PatchMapping("/user/{id}")
    ResponseEntity<Object> updateEmail(@RequestBody RequestUpdateEmail patchResource,@PathVariable long id){

        Optional<User> currentUser = userService.getUserById(id);
        if (currentUser.isEmpty()) {
            return ResponseEntity.notFound().build(); // Return 404 Not Found if user does not exist
        }

        // Update the email
        try {
            System.out.println(patchResource.getEmail());
            userService.updateEmail(id, patchResource);
            return ResponseEntity.ok("Email updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update email"); // Return 500 Internal Server Error if an error occurs
        }
    }
}
