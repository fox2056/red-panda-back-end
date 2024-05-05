package com.uz.redpandabackend.controller;

import com.uz.redpandabackend.model.Event;
import com.uz.redpandabackend.model.Person;
import com.uz.redpandabackend.model.TimeSlot;
import com.uz.redpandabackend.repository.PersonRepository;
import com.uz.redpandabackend.service.ScheduleService;
import lombok.AllArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class PersonController {

    private final PersonRepository personRepository;
    private final ScheduleService scheduleService;

    @QueryMapping
    public List<Person> allPersons() {
        return personRepository.findAll();  // assuming dataLoader loads all persons
    }

    @QueryMapping
    public Person personById(@Argument String id) {
        Person person = personRepository.findById(id).orElse(null);
        if (person == null) {
            System.out.println("No person found with id " + id);
        }
        return person;
    }

    @QueryMapping
    public List<TimeSlot> findCommonFreeTimeSlots(@Argument String date, @Argument String person1Id, @Argument String person2Id) {
        Person person1 = personRepository.findById(person1Id)
                .orElseThrow(() -> new RuntimeException("Person not found with id: " + person1Id));
        Person person2 = personRepository.findById(person2Id)
                .orElseThrow(() -> new RuntimeException("Person not found with id: " + person2Id));

        List<Event> schedule1 = filterEventsByDate(person1.workSchedule(), date);
        List<Event> schedule2 = filterEventsByDate(person2.workSchedule(), date);
        return scheduleService.findCommonFreeTimeSlots(schedule1, schedule2);
    }

    private List<Event> filterEventsByDate(List<Event> events, String date) {
        return events.stream()
                .filter(event -> event.date().equals(date))
                .collect(Collectors.toList());
    }


}
