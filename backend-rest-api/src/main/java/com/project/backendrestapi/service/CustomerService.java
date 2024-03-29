package com.project.backendrestapi.service;

import com.project.backendrestapi.dto.AccountDto;
import com.project.backendrestapi.dto.CustomerDto;
import com.project.backendrestapi.dto.PersonDto;
import com.project.backendrestapi.model.Account;
import com.project.backendrestapi.model.Customer;
import com.project.backendrestapi.model.Person;
import com.project.backendrestapi.repository.AccountRepository;
import com.project.backendrestapi.repository.CustomerRepository;
import com.project.backendrestapi.repository.PersonRepository;

import lombok.AllArgsConstructor;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CustomerService {

    @Autowired
    private final CustomerRepository customerRepository;

    @Autowired
    private final PersonService personService;

    @Autowired
    private final AccountService accountService;

    public List<Customer> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers;
    }

    public Optional<Customer> getCustomerById(Long customerId) {
        Optional<Customer> customerOptional = customerRepository.findById(customerId);
        return customerOptional;
    }

    public Customer createCustomer(CustomerDto customerDto) {
        Customer customer = convertToEntity(customerDto);
        customerRepository.save(customer);
        return customer;
    }

    public Optional<Customer> updateCustomer(Long customerId, CustomerDto updatedCustomerDto) {
        Optional<Customer> existingCustomerOptional = customerRepository.findById(customerId);

        if (existingCustomerOptional.isPresent()) {
            Customer existingCustomer = existingCustomerOptional.get();
            BeanUtils.copyProperties(updatedCustomerDto, existingCustomer, "customerId");
            customerRepository.save(existingCustomer);
            return existingCustomerOptional;
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteCustomer(Long customerId) {
        if (customerRepository.existsById(customerId)) {
            customerRepository.deleteById(customerId);
            return true;
        } else {
            return false;
        }
    }

    private Customer convertToEntity(CustomerDto customerDto) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDto, customer);
        customer.setPerson(personService.createPerson(customerDto.getPerson()));
        customer.setAccount(accountService.createAccount(customerDto.getAccount()));
        return customer;
    }

}
