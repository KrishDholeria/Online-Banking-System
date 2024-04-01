package com.project.backendrestapi.service;

import com.project.backendrestapi.model.Manager;
import com.project.backendrestapi.model.Person;
import com.project.backendrestapi.dto.ManagerDto;
import com.project.backendrestapi.model.Branch;
import com.project.backendrestapi.repository.ManagerRepository;

import lombok.AllArgsConstructor;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ManagerService {

    @Autowired
    private final ManagerRepository managerRepository;

    @Autowired
    private final PersonService personService;

    @Autowired
    private final BranchService branchService;

    public List<Manager> getAllManagers() {
        return managerRepository.findAll();
    }

    public Optional<Manager> getManagerById(Long managerId) {
        return managerRepository.findById(managerId);
    }

    public Manager getManagerByUserName(String username){
        Manager manager = managerRepository.findByUserName(username).get();
        // System.out.println(username);
        // System.out.println(manager);
        return manager;
    }

    public Manager createManager(ManagerDto managerDto) {
        BCryptPasswordEncoder b = new BCryptPasswordEncoder();

        Manager manager = Manager.builder()
                .userName(managerDto.getUserName())
                .password(b.encode(managerDto.getPassword()))
                .password(b.encode(managerDto.getPassword()))
                .person(personService.createPerson(managerDto.getPerson()))
                .branch(branchService.getBranchByBranchCode(managerDto.getBranch().getBranchCode()).get())
                .build();
        branchService.assignManagerToBranch(manager.getBranch().getBranchId(), manager);
        
        return managerRepository.save(manager);
    }

    public Optional<Manager> updateManager(Long managerId, ManagerDto updatedManagerDto) {
        Optional<Manager> existingManager = managerRepository.findById(managerId);
        BCryptPasswordEncoder b = new BCryptPasswordEncoder();

        if (existingManager.isPresent()) {
            BCryptPasswordEncoder b = new BCryptPasswordEncoder();
            Manager existing = existingManager.get();
            existing.setManagerId(managerId);
            existing.setUserName(updatedManagerDto.getUserName());
            existing.setPassword(b.encode(updatedManagerDto.getPassword()));
            Person person = existing.getPerson();
            personService.updatePerson(person.getPersonId(), personService.personToPersonDto(person));
            existing.setBranch(branchService.getBranchByBranchCode(updatedManagerDto.getBranch().getBranchCode()).get());

            
            managerRepository.save(existing);

            return Optional.of(existing);
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteManager(Long managerId) {
        if (managerRepository.existsById(managerId)) {
            Manager manager = managerRepository.findById(managerId).get();
            branchService.getBranchById(manager.getBranch().getBranchId()).get().getManagers().remove(manager);
            managerRepository.deleteById(managerId);
            return true;
        } else {
            return false;
        }
    }

    public Manager authenticateManager(String username, String password) {
        // Find the manager by username from the database
        Manager manager = managerRepository.findByUserName(username).get();
        
        String storedEncodedPassword = manager.getPassword();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        // If manager not found or password does not match, return null

        if (manager == null) {
            return null;
        }
    
        if (bCryptPasswordEncoder.matches(password, storedEncodedPassword)){
            return manager;
        }

        // Return the authenticated manager
        return null;
    }

    public boolean validatePassword(String username, String password) {

        Manager manager = managerRepository.findByUserName(username).get();
        String storedEncodedPassword = manager.getPassword();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        
        // System.out.println(manager);
        if (manager != null) {
            
            if(bCryptPasswordEncoder.matches(password, storedEncodedPassword)){
                // System.out.println("hello");
                return true;
            }
        }
        return false;
    }

    public void changePassword(String username, String newPassword) {
        Manager manager = managerRepository.findByUserName(username).get();
        BCryptPasswordEncoder b = new BCryptPasswordEncoder();
        if (manager != null) {
            manager.setPassword(b.encode(newPassword));
            managerRepository.save(manager);
        }
    }

    public ManagerDto managerToManagerDto(Manager manager) {
        BCryptPasswordEncoder b = new BCryptPasswordEncoder();
        ManagerDto managerDto = new ManagerDto();
        managerDto.setUserName(manager.getUserName());
        managerDto.setPassword(b.encode(manager.getPassword()));
        managerDto.setBranch(this.branchService.entityToDto(manager.getBranch())); // Assuming branchId is stored in Manager object
        managerDto.setPerson(PersonService.personToPersonDto(manager.getPerson())); // Assuming you have a method to map Person to PersonDto
        return managerDto;
    }


    public ManagerDto entityToDto(Manager manager){
        return ManagerDto.builder()
                .userName(manager.getUserName())
                .manageId(manager.getManagerId())
                // .password(manager.getPassword())
                .branch(branchService.entityToDto(manager.getBranch()))
                .person(personService.entityToDto(manager.getPerson()))
                .build();
    }

    public Optional<Manager> getManagerByuserName(String userName) {
        Optional<Manager> manager = managerRepository.getManagerByuserName(userName);
        // TODO Auto-generated method stub
        return manager;
    }

    // public boolean deletePerson(Long personId) {
    //     if (personRepository.existsById(personId)) {
    //         personRepository.deleteById(personId);
    //         return true;
    //     } else {
    //         return false;
    //     }
    // }

    

    public void deleteManagerByUsername(String username) {
        // TODO Auto-generated method stub
        Optional<Manager> manager = managerRepository.getManagerByuserName(username);
        managerRepository.deleteById(manager.get().getManagerId());
        System.out.println(username);
    }

    // public void assignPersonToManager(Long managerId, Person person) {
    // Optional<Manager> managerOptional = managerRepository.findById(managerId);
    // if (managerOptional.isPresent()) {
    // Manager manager = managerOptional.get();
    // manager.setPerson(person);
    // managerRepository.save(manager);
    // }
    // }
    //
    // public void assignBranchToManager(Long managerId, Branch branch) {
    // Optional<Manager> managerOptional = managerRepository.findById(managerId);
    // if (managerOptional.isPresent()) {
    // Manager manager = managerOptional.get();
    // manager.setBranch(branch);
    // managerRepository.save(manager);
    // }
    // }
}