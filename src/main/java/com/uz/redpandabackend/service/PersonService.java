package com.uz.redpandabackend.service;

import com.uz.redpandabackend.model.Event;
import com.uz.redpandabackend.model.Person;
import com.uz.redpandabackend.repository.PersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;

    public void updateOrInsertPerson(String email, String imie, String nazwisko, List<Event> events) {

        personRepository.deleteByEmail(email);

        Person newPerson = new Person(null, email, imie, nazwisko, events);
        personRepository.insert(newPerson);

    }
}
