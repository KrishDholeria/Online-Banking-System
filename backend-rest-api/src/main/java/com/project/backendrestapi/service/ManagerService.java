package com.project.backendrestapi.service;

import com.project.backendrestapi.model.Manager;
import com.project.backendrestapi.model.Person;
import com.project.backendrestapi.dto.ManagerDto;
import com.project.backendrestapi.model.Branch;
import com.project.backendrestapi.repository.ManagerRepository;

import lombok.AllArgsConstructor;

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
        Manager manager = managerRepository.findByUserName("keval545");
        System.out.println(username);
        System.out.println(manager);
        return manager;
    }

    public Manager createManager(ManagerDto managerDto) {

        Manager manager = Manager.builder()
            .userName(managerDto.getUserName())
            .password(managerDto.getPassword())
            .person(personService.createPerson(managerDto.getPerson()))
            .branch(branchService.getBranchById(managerDto.getBranchId()).get())
            .build();
        branchService.assignManagerToBranch(managerDto.getBranchId(), manager);
        return managerRepository.save(manager);
    }

    public Optional<Manager> updateManager(Long managerId, ManagerDto updatedManagerDto) {
        Optional<Manager> existingManager = managerRepository.findById(managerId);

        if (existingManager.isPresent()) {
            Manager existing = existingManager.get();
            existing.setManagerId(managerId);
            existing.setUserName(updatedManagerDto.getUserName());
            existing.setPassword(updatedManagerDto.getPassword());

            existing.setBranch(branchService.getBranchById(updatedManagerDto.getBranchId()).get());

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
        Manager manager = managerRepository.findByUserName(username);
        System.out.println(username);

        System.out.println(manager.getPerson());
        
        // If manager not found or password does not match, return null
        if (manager == null || password == manager.getPassword()){
            return null;
        }

        // Return the authenticated manager
        return manager;
    }

    public Manager abc(String username) {
        // Find the manager by username from the database
        Manager manager = managerRepository.findByUserName(username);
        System.out.println(username);

        System.out.println(manager.getPerson());
        

        // Return the authenticated manager
        return manager;
    }


//    public void assignPersonToManager(Long managerId, Person person) {
//        Optional<Manager> managerOptional = managerRepository.findById(managerId);
//        if (managerOptional.isPresent()) {
//            Manager manager = managerOptional.get();
//            manager.setPerson(person);
//            managerRepository.save(manager);
//        }
//    }
//
//    public void assignBranchToManager(Long managerId, Branch branch) {
//        Optional<Manager> managerOptional = managerRepository.findById(managerId);
//        if (managerOptional.isPresent()) {
//            Manager manager = managerOptional.get();
//            manager.setBranch(branch);
//            managerRepository.save(manager);
//        }
//    }
}
