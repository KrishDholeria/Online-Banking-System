package com.project.backendrestapi.controller;

import com.project.backendrestapi.dto.CustomerDto;
import com.project.backendrestapi.dto.LoginResponse;
import com.project.backendrestapi.dto.ManagerDto;
import com.project.backendrestapi.dto.PersonDto;
import com.project.backendrestapi.model.Branch;
import com.project.backendrestapi.model.Customer;
import com.project.backendrestapi.model.Manager;
import com.project.backendrestapi.model.Person;
import com.project.backendrestapi.repository.PersonRepository;
import com.project.backendrestapi.service.CustomerService;
import com.project.backendrestapi.service.ManagerService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

<<<<<<< HEAD
<<<<<<< HEAD
import java.text.SimpleDateFormat;
import java.util.ArrayList;
=======
>>>>>>> parent of abcbe0f (manager update half)
=======
>>>>>>> parent of abcbe0f (manager update half)
import java.util.Date;
import java.util.List;

@RestController
@AllArgsConstructor
public class ManagerController {

    @Autowired
    private final ManagerService managerService;

    @Autowired
    private final CustomerService customerService;

    @Autowired
    private final PersonRepository personRepository;

    @PostMapping("/add/customer")
    public ResponseEntity<String> addCustomer(@RequestBody CustomerDto customerDto) {
        try {
            Customer customer = customerService.createCustomer(customerDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Customer added successfully with ID: " + customer.getCustomerId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to add customer: " + e.getMessage());
        }
    }

    @GetMapping("all/customers")
    public List<CustomerDto> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        List<CustomerDto> customerDtos = new ArrayList<>();

        for (Customer customer : customers) {
            CustomerDto customerDto = customerService.convertToCustomerDto(customer);
            customerDtos.add(customerDto);
        }

        return customerDtos;
    }

    @PostMapping("/manager/details")
    public ResponseEntity<ManagerDto> getManagerDetails(@RequestBody String username) {
    try {
        Manager manager = managerService.getManagerByUserName(username);
        if (manager != null) {
            // Map the manager entity to ManagerDto
            ManagerDto managerDto = new ManagerDto();
            managerDto.setUserName(manager.getUserName());
            managerDto.setPassword(manager.getPassword());
            // managerDto.setbranchId(manager.getbranchId());
            // Map the Person entity to PersonDto
            Person person = manager.getPerson();
            if (person != null) {
                PersonDto personDto = new PersonDto();
                personDto.setFirstName(person.getFirstName());
                personDto.setLastName(person.getLastName());
                personDto.setAddress(person.getAddress());
                // personDto.setDob(person.getDob());
                personDto.setEmail(person.getEmail());
                personDto.setPhoneNo(person.getPhoneNo());
                // Set other properties as needed
                managerDto.setPerson(personDto);
            }
            return ResponseEntity.ok().body(managerDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}


    @PostMapping("/manager/login")
    public ResponseEntity<?> managerLogin(@RequestBody ManagerDto managerDto) {
        // Extract username and password from the request body
        String username = managerDto.getUserName();
        String password = managerDto.getPassword();
        // Authenticate manager
        Manager manager = managerService.authenticateManager(username, password);

        if (manager != null) {
            // If authentication is successful, generate JWT token
            String token = generateToken(username);

            // Return the token in the response body
            return ResponseEntity.ok(new LoginResponse("Login successful", token));
        } else {
            // If authentication fails, return an unauthorized response
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse("Invalid username or password"));
        }
    }

    // Generate JWT token
    private String generateToken(String username) {
        long expirationTime = 1000 * 60 * 60 * 10; // Token expiration time (10 hours)

        String secretKey = "YcoufefeffrsdsgerererergrrgegerSefffbfbrrrrcoufefeffrsdsgerererergrrgegerSefffbfbrrrrcoufefeffrsdsgerererergrrgegerSefffbfbrrrrcoufefeffrsdsgerererergrrgegerSefffbfbrrrrcoufefeffrsdsgerererergrrgegerSefffbfbrrrrcoufefeffrsdsgerererergrrgegerSefffbfbrrrrcoufefeffrsdsgerererergrrgegerSefffbfbrrrrcretKdvvvvfvfereydwdeffwewwff"; // Change
                                                                                                                                                                                                                                                                                                                                                                   // this
                                                                                                                                                                                                                                                                                                                                                                   // to
                                                                                                                                                                                                                                                                                                                                                                   // your
                                                                                                                                                                                                                                                                                                                                                                   // secret
                                                                                                                                                                                                                                                                                                                                                                   // key
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }
}
