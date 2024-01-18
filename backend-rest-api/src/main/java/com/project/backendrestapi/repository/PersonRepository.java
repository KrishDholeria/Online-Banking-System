package com.project.backendrestapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.backendrestapi.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {

}
