package com.uz.redpandabackend.controller;

import com.uz.redpandabackend.model.Person;
import com.uz.redpandabackend.repository.PersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.List;

@Controller
@AllArgsConstructor
public class PersonController {

    private final PersonRepository personRepository;

    @QueryMapping
    public List<Person> allPersons() throws IOException {
        return personRepository.findAll();  // assuming dataLoader loads all persons
    }

    @QueryMapping
    public Person personById(@Argument String id) throws IOException {
        Person person = personRepository.findById(id).orElse(null);
        if (person == null) {
            System.out.println("No person found with id " + id);
        }
        System.out.println(person);
        return person;
    }
}
