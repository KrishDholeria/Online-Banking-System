package com.project.backendrestapi.service;

import com.project.backendrestapi.model.Customer;
import com.project.backendrestapi.model.Person;
import com.project.backendrestapi.model.Account;
import com.project.backendrestapi.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomerById(Long customerId) {
        return customerRepository.findById(customerId);
    }

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Optional<Customer> updateCustomer(Long customerId, Customer updatedCustomer) {
        Optional<Customer> existingCustomer = customerRepository.findById(customerId);

        if (existingCustomer.isPresent()) {
            Customer existing = existingCustomer.get();
            existing.setCustomerId(customerId);
            existing.setUserName(updatedCustomer.getUserName());
            existing.setPassword(updatedCustomer.getPassword());
            existing.setPanNo(updatedCustomer.getPanNo());

            // Handle relationships
            existing.setPerson(updatedCustomer.getPerson());
            existing.setAccount(updatedCustomer.getAccount());

            customerRepository.save(existing);

            return Optional.of(existing);
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

    // Additional methods for handling relationships if needed

    // Example method for assigning a person to a customer
    public void assignPersonToCustomer(Long customerId, Person person) {
        Optional<Customer> customerOptional = customerRepository.findById(customerId);
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            customer.setPerson(person);
            customerRepository.save(customer);
        }
    }

    // Example method for assigning an account to a customer
    public void assignAccountToCustomer(Long customerId, Account account) {
        Optional<Customer> customerOptional = customerRepository.findById(customerId);
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            customer.setAccount(account);
            customerRepository.save(customer);
        }
    }
}
