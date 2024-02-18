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
        System.out.println("hello");
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
            System.out.println(date);
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

    public static PersonDto personToPersonDto(Person person) {
        PersonDto personDto = new PersonDto();
        personDto.setLastName(person.getLastName());
        personDto.setFirstName(person.getFirstName());
        personDto.setDob(person.getDob().toString()); // Assuming dob is a String in the format "yyyy-MM-dd"
        personDto.setEmail(person.getEmail());
        personDto.setPhoneNo(person.getPhoneNo());
        personDto.setAddress(person.getAddress());
        return personDto;
    }

    public static Person personDtoToPerson(PersonDto personDto) {
        Person person = new Person();
        person.setLastName(personDto.getLastName());
        person.setFirstName(personDto.getFirstName());
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC")); // replace "UTC" with your desired timezone
        try{
            date = sdf.parse(personDto.getDob());
        }
        catch(ParseException e){
            e.printStackTrace();
        }
        person.setDob(date); // Assuming dob is a String in the format "yyyy-MM-dd"
        person.setEmail(personDto.getEmail());
        person.setPhoneNo(personDto.getPhoneNo());
        person.setAddress(personDto.getAddress());
        return person;
    }

}