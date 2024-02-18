package com.project.backendrestapi.controller;


import com.project.backendrestapi.dto.AccountDto;
import com.project.backendrestapi.dto.CustomerDto;
import com.project.backendrestapi.dto.CustomerSignupDto;
import com.project.backendrestapi.dto.PersonDto;
import com.project.backendrestapi.model.Account;
import com.project.backendrestapi.model.Customer;
import com.project.backendrestapi.model.Person;
import com.project.backendrestapi.service.AccountService;
import com.project.backendrestapi.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private final CustomerService customerService;

    @Autowired
    private final AccountService accountService;

    @PostMapping("/setusername")
    public ResponseEntity<CustomerDto> setUsername(@RequestBody CustomerSignupDto customer){
        Account account = accountService.getAccountByAccountNo(customer.getAccountNo());
        if(!customerService.customerExistByUserName(customer.getUserName())){
            Optional<Customer> customer1 = customerService.updateCustomer(account.getCustomer().getCustomerId(), CustomerDto.builder().userName(customer.getUserName()).build());
            Person person = customer1.get().getPerson();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            CustomerDto customerDto = CustomerDto.builder()
                    .panNo(customer1.get().getPanNo())
                    .userName(customer.getUserName())
                    .account(AccountDto.builder()
                            .accountBalance(account.getAccountBalance())
                            .branchId(account.getBranch().getBranchId())
                            .cutomerId(customer1.get().getCustomerId())
                            .build())
                    .person(PersonDto.builder()
                            .dob(sdf.format(person.getDob()))
                            .email(person.getEmail())
                            .address(person.getAddress())
                            .phoneNo(person.getPhoneNo())
                            .firstName(person.getFirstName())
                            .lastName(person.getLastName())
                            .build())
                    .build();
            return new ResponseEntity<>(customerDto, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
        }
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
