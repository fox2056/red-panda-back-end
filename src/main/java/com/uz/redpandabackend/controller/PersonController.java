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
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.ArrayList;
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
    public List<TimeSlot> findCommonFreeTimeSlots(@Argument String date,
                                                  @Argument String hourFrom,
                                                  @Argument String hourTo,
                                                  @Argument List<String> personIds) {
        List<List<Event>> schedules = new ArrayList<>();

        for (String personId : personIds) {
            Person person = personRepository.findById(personId)
                    .orElseThrow(() -> new RuntimeException("Person not found with id: " + personId));
            List<Event> filteredEvents = filterEventsByDate(person.workSchedule(), date);
            schedules.add(filteredEvents);
        }

        return scheduleService.findCommonFreeTimeSlots(hourFrom, hourTo, schedules);
    }

    private List<Event> filterEventsByDate(List<Event> events, String date) {
        return events.stream()
                .filter(event -> event.date().equals(date))
                .collect(Collectors.toList());
    }


}
