package com.project.backendrestapi.service;

import com.project.backendrestapi.dto.PersonDto;
import com.project.backendrestapi.model.Person;
import com.project.backendrestapi.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

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

    public Optional<Person> getPersonById(Long personId) {
        return personRepository.findById(personId);
    }

    public String dateToString(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }

    public Person createPerson(PersonDto personDto) {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC")); // replace "UTC" with your desired timezone
        try{
            date = sdf.parse(personDto.getDob());
        }
        catch(ParseException e){
            e.printStackTrace();
        }
        Person person = Person.builder()
        .firstName(personDto.getFirstName())
        .lastName(personDto.getLastName())
        .dob(date)
        .address(personDto.getAddress())
        .email(personDto.getEmail())
        .phoneNo(personDto.getPhoneNo())
        .build();
        return personRepository.save(person);
    }

    public Optional<Person> updatePerson(Long personId, PersonDto updatedPerson) {
        Optional<Person> existingPerson = personRepository.findById(personId);

        if (existingPerson.isPresent()) {
            Person existing = existingPerson.get();
            Date date = null;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC")); // replace "UTC" with your desired timezone
            try{
                date = sdf.parse(updatedPerson.getDob());
            }
            catch(ParseException e){
                e.printStackTrace();
            }
            existing.setPersonId(personId);
            existing.setFirstName(updatedPerson.getFirstName());
            existing.setLastName(updatedPerson.getLastName());
            existing.setDob(date);
            existing.setEmail(updatedPerson.getEmail());
            existing.setPhoneNo(updatedPerson.getPhoneNo());
            existing.setAddress(updatedPerson.getAddress());

            personRepository.save(existing);

            return Optional.of(existing);
        } else {
            return Optional.empty();
        }
    }

    public boolean deletePerson(Long personId) {
        if (personRepository.existsById(personId)) {
            personRepository.deleteById(personId);
            return true;
        } else {
            return false;
        }
    }

    public PersonDto entityToDto(Person person){
        return PersonDto.builder()
                .lastName(person.getLastName())
                .firstName(person.getFirstName())
                .phoneNo(person.getPhoneNo())
                .address(person.getAddress())
                .address(person.getAddress())
                .email(person.getEmail())
                .dob(dateToString(person.getDob()))
                .build();
    }
}