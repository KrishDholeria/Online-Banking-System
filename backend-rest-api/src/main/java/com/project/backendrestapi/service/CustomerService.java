package com.project.backendrestapi.service;

import com.project.backendrestapi.dto.AccountDto;
import com.project.backendrestapi.dto.BeneficiaryDto;
import com.project.backendrestapi.dto.CustomerDto;
import com.project.backendrestapi.dto.PersonDto;
import com.project.backendrestapi.model.Account;
import com.project.backendrestapi.model.Beneficiary;
import com.project.backendrestapi.model.Customer;
import com.project.backendrestapi.model.Person;
import com.project.backendrestapi.model.Transaction;
import com.project.backendrestapi.repository.AccountRepository;
import com.project.backendrestapi.repository.CustomerRepository;
import com.project.backendrestapi.repository.PersonRepository;

import lombok.AllArgsConstructor;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.beans.PropertyDescriptor;
import java.util.*;
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

    @Autowired
    private final BeneficiaryService beneficiaryService;

    @Autowired
    private final TransactionService transactionService;

    public List<Customer> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers;
    }

    public Boolean customerExistByUserName(String userName) {
        return customerRepository.existsByUserName(userName);
    }

    public Optional<Customer> getCustomerById(Long customerId) {
        Optional<Customer> customerOptional = customerRepository.findById(customerId);
        return customerOptional;
    }

    public Optional<Customer> getCustomerByUserName(String userName) {
        return customerRepository.findByUserName(userName);
    }

    public Customer createCustomer(CustomerDto customerDto) {
        Customer customer = convertToEntity(customerDto);
        customer = customerRepository.save(customer);
        return customer;
    }

    // to get array of null properties
    private static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null)
                emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public Optional<Customer> updateCustomer(String userName, CustomerDto updatedCustomerDto) {
        Optional<Customer> existingCustomerOptional = customerRepository.findByUserName(userName);

        if (existingCustomerOptional.isPresent()) {
            Customer existingCustomer = existingCustomerOptional.get();
            BeanUtils.copyProperties(updatedCustomerDto, existingCustomer, getNullPropertyNames(updatedCustomerDto));
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
        BCryptPasswordEncoder b = new BCryptPasswordEncoder();
        BeanUtils.copyProperties(customerDto, customer);
        if(customerDto.getPassword() != null){
            customer.setPassword(b.encode(customerDto.getPassword()));
        }
        customer.setPerson(personService.createPerson(customerDto.getPerson()));
        customer.setAccount(accountService.createAccount(customerDto.getAccount()));
        return customer;
    }

    public CustomerDto convertToCustomerDto(Customer customer) {
        PersonDto personDto = personService.personToPersonDto(customer.getPerson());
        AccountDto accountDto = accountService.fromEntity(customer.getAccount());
        CustomerDto customerDto = new CustomerDto();
        customerDto.setPanNo(customer.getPanNo());
        customerDto.setPerson(personDto);
        customerDto.setAccount(accountDto);
        List<BeneficiaryDto> beneficiaryDtos = new ArrayList<>();
        for (Beneficiary b : customer.getBeneficiaries()) {
            beneficiaryDtos.add(beneficiaryService.entityToDto(b));
        }
        customerDto.setBeneficiaries(beneficiaryDtos);

        return customerDto;
    }
}
