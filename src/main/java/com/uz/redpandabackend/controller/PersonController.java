package com.uz.redpandabackend.controller;

import com.uz.redpandabackend.DataLoader;
import com.uz.redpandabackend.model.Person;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.List;

@Controller
public class PersonController {
    private final DataLoader dataLoader;

    public PersonController(DataLoader dataLoader) {
        this.dataLoader = dataLoader;
    }

    @QueryMapping
    public List<Person> allPersons() throws IOException {
        return dataLoader.loadJsonData();  // assuming dataLoader loads all persons
    }

    @QueryMapping
    public Person personById(@Argument String id) throws IOException {
        Person person = dataLoader.loadJsonData().stream()
                .filter(p -> p.id().equals(id))
                .findFirst()
                .orElse(null);
        if (person == null) {
            System.out.println("No person found with id " + id);
        }
        System.out.println(person);
        return person;
    }
}
