package com.uz.redpandabackend.graphql;

import com.uz.redpandabackend.model.Person;
import com.uz.redpandabackend.model.Event;
import com.uz.redpandabackend.model.TimeSlot;
import com.uz.redpandabackend.repository.PersonRepository;
import graphql.kickstart.tools.GraphQLQueryResolver;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class QueryResolver implements GraphQLQueryResolver {

    private PersonRepository personRepository;

    private LocalTime parseTime(String time) {
        return LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
    }

    public List<TimeSlot> commonFreeTimeSlots(String date, String person1Id, String person2Id) {
        Person person1 = personRepository.findById(person1Id)
                .orElseThrow(() -> new RuntimeException("Person not found with id: " + person1Id));
        Person person2 = personRepository.findById(person2Id)
                .orElseThrow(() -> new RuntimeException("Person not found with id: " + person2Id));

        List<Event> schedule1 = filterEventsByDate(person1.workSchedule(), date);
        List<Event> schedule2 = filterEventsByDate(person2.workSchedule(), date);

        return findCommonFreeTimeSlots(schedule1, schedule2);
    }

    private List<Event> filterEventsByDate(List<Event> events, String date) {
        return events.stream()
                .filter(event -> event.date().equals(date))
                .collect(Collectors.toList());
    }

    private List<TimeSlot> findCommonFreeTimeSlots(List<Event> schedule1, List<Event> schedule2) {
        List<TimeSlot> freeTimeSlots = new ArrayList<>();
        int i = 0, j = 0;
        LocalTime lastEndTime = LocalTime.MIN;

        while (i < schedule1.size() && j < schedule2.size()) {
            Event e1 = schedule1.get(i);
            Event e2 = schedule2.get(j);

            LocalTime startTime1 = parseTime(e1.startTime());
            LocalTime endTime1 = parseTime(e1.endTime());
            LocalTime startTime2 = parseTime(e2.startTime());
            LocalTime endTime2 = parseTime(e2.endTime());

            LocalTime end = endTime1.isAfter(endTime2) ? endTime2 : endTime1;

            if (startTime1.isAfter(endTime2)) {
                j++;
            } else if (startTime2.isAfter(endTime1)) {
                i++;
            } else {
                if (lastEndTime.isBefore(end)) {
                    lastEndTime = end;
                }
                i++;
                j++;
            }

            LocalTime nextStartTime = LocalTime.MAX;
            if (i < schedule1.size()) nextStartTime = nextStartTime.isAfter(parseTime(schedule1.get(i).startTime())) ? parseTime(schedule1.get(i).startTime()) : nextStartTime;
            if (j < schedule2.size()) nextStartTime = nextStartTime.isAfter(parseTime(schedule2.get(j).startTime())) ? parseTime(schedule2.get(j).startTime()) : nextStartTime;

            if (lastEndTime.isBefore(nextStartTime)) {
                freeTimeSlots.add(new TimeSlot(lastEndTime.toString(), nextStartTime.toString()));
                lastEndTime = nextStartTime;
            }
        }

        return freeTimeSlots;
    }
}
