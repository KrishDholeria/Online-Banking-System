package com.project.backendrestapi.service;

import com.project.backendrestapi.model.Admin;
import com.project.backendrestapi.model.Customer;
import com.project.backendrestapi.model.Manager;
import com.project.backendrestapi.repository.AdminRepository;
import com.project.backendrestapi.repository.CustomerRepository;
import com.project.backendrestapi.repository.ManagerRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    AdminRepository adminRepository;

    @Autowired
    ManagerRepository managerRepository;

    @Autowired
    CustomerRepository customerRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Admin> admin = adminRepository.findByUserName(username);
        if(admin.isPresent()){
            return org.springframework.security.core.userdetails.User.withUsername(admin.get().getUserName())
                    .password(admin.get().getPassword())
                    .roles("ADMIN")
                    .build();
        }

        Optional<Manager> manager = managerRepository.findByUserName(username);
        if(manager.isPresent()){
            return org.springframework.security.core.userdetails.User.withUsername(manager.get().getUserName())
                    .password(manager.get().getPassword())
                    .roles("MANAGER")
                    .build();
        }

        Optional<Customer> customer = customerRepository.findByUserName(username);
        if(customer.isPresent()){
            return org.springframework.security.core.userdetails.User.withUsername(customer.get().getUserName())
                    .password(customer.get().getPassword())
                    .roles("CUSTOMER").build();
        }

        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}
