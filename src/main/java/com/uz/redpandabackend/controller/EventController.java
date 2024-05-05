package com.uz.redpandabackend.controller;

import com.uz.redpandabackend.model.Event;
import com.uz.redpandabackend.model.Person;
import com.uz.redpandabackend.repository.PersonRepository;
import com.uz.redpandabackend.utils.EventInput;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@AllArgsConstructor
public class EventController {
    private final PersonRepository personRepository;

    @MutationMapping
    public Event createEvent(@Argument String personId, @Argument EventInput eventInput) {
        return personRepository.findById(personId).map(person -> {
            Event newEvent = new Event(
                    ObjectId.get().toString(), // MongoDB's Object ID
                    eventInput.date(),
                    eventInput.day(),
                    eventInput.startTime(),
                    eventInput.endTime(),
                    eventInput.subject(),
                    eventInput.classType(),
                    eventInput.room()
            );

            person.workSchedule().add(newEvent);
            personRepository.save(person);
            return newEvent;
        }).orElseThrow(() -> new RuntimeException("Person not found with id: " + personId));
    }

    @MutationMapping
    public boolean deleteEvent(@Argument String personId, @Argument String eventId) {
        Person person = personRepository.findById(personId).orElse(null);
        if (person != null) {
            boolean removed = person.workSchedule().removeIf(event -> event.id().equals(eventId));
            if (removed) {
                personRepository.save(person);
                return true;
            }
        }
        return false;
    }
}
