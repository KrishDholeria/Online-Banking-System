package com.project.backendrestapi.service;

import com.project.backendrestapi.model.Manager;
import com.project.backendrestapi.model.Person;
import com.project.backendrestapi.model.Branch;
import com.project.backendrestapi.repository.ManagerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ManagerService {

    @Autowired
    private final ManagerRepository managerRepository;

    public ManagerService(ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    public List<Manager> getAllManagers() {
        return managerRepository.findAll();
    }

    public Optional<Manager> getManagerById(Long managerId) {
        return managerRepository.findById(managerId);
    }

    public Manager createManager(Manager manager) {
        return managerRepository.save(manager);
    }

    public Optional<Manager> updateManager(Long managerId, Manager updatedManager) {
        Optional<Manager> existingManager = managerRepository.findById(managerId);

        if (existingManager.isPresent()) {
            Manager existing = existingManager.get();
            existing.setManagerId(managerId);
            existing.setUserName(updatedManager.getUserName());
            existing.setPassword(updatedManager.getPassword());

            // Handle relationships
            existing.setPerson(updatedManager.getPerson());
            existing.setBranch(updatedManager.getBranch());

            managerRepository.save(existing);

            return Optional.of(existing);
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteManager(Long managerId) {
        if (managerRepository.existsById(managerId)) {
            managerRepository.deleteById(managerId);
            return true;
        } else {
            return false;
        }
    }

    // assigning a person to a manager
    public void assignPersonToManager(Long managerId, Person person) {
        Optional<Manager> managerOptional = managerRepository.findById(managerId);
        if (managerOptional.isPresent()) {
            Manager manager = managerOptional.get();
            manager.setPerson(person);
            managerRepository.save(manager);
        }
    }

    // assigning a branch to a manager
    public void assignBranchToManager(Long managerId, Branch branch) {
        Optional<Manager> managerOptional = managerRepository.findById(managerId);
        if (managerOptional.isPresent()) {
            Manager manager = managerOptional.get();
            manager.setBranch(branch);
            managerRepository.save(manager);
        }
    }
}
