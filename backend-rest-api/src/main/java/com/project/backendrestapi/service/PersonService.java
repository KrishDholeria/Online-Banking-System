package com.project.backendrestapi.service;

import com.project.backendrestapi.model.Person;
import com.project.backendrestapi.repository.PersonRepository;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    @Autowired
    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    public Optional<Person> getPersonById(String personId) {
        return personRepository.findById(personId);
    }

    public Person createPerson(Person person) {
        return personRepository.save(person);
    }

    public Optional<Person> updatePerson(String personId, Person updatedPerson) {
        Optional<Person> existingPerson = personRepository.findById(personId);

        if (existingPerson.isPresent()) {
            Person existing = existingPerson.get();
            existing.setPersonId(personId);
            existing.setFirstName(updatedPerson.getFirstName());
            existing.setLastName(updatedPerson.getLastName());
            existing.setDob(updatedPerson.getDob());
            existing.setEmail(updatedPerson.getEmail());
            existing.setPhoneNo(updatedPerson.getPhoneNo());
            existing.setAddress(updatedPerson.getAddress());

            personRepository.save(existing);

            return Optional.of(existing);
        } else {
            return Optional.empty();
        }
    }

    public boolean deletePerson(String personId) {
        if (personRepository.existsById(personId)) {
            personRepository.deleteById(personId);
            return true;
        } else {
            return false;
        }
    }
}