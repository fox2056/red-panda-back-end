package com.uz.redpandabackend.service;

import com.uz.redpandabackend.model.Event;
import com.uz.redpandabackend.model.TimeSlot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScheduleService {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleService.class);


    public LocalTime parseTime(String time) {
        return LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
    }

    public List<TimeSlot> findCommonFreeTimeSlots(List<Event> eventsPerson1, List<Event> eventsPerson2) {

        logger.debug("Finding free time slots for events person 1: {}", eventsPerson1);

        logger.debug("Finding free time slots for events person 2: {}", eventsPerson2);

        List<TimeSlot> freeTimeSlots1 = findFreeTimeSlots(eventsPerson1);
        List<TimeSlot> freeTimeSlots2 = findFreeTimeSlots(eventsPerson2);

        return findCommonTimeSlots(freeTimeSlots1, freeTimeSlots2);
    }

    private List<TimeSlot> findFreeTimeSlots(List<Event> events) {
        LocalTime startOfDay = LocalTime.MIN;
        LocalTime endOfDay = LocalTime.of(23, 59);
        List<TimeSlot> freeTimeSlots = new ArrayList<>();
        freeTimeSlots.add(new TimeSlot(startOfDay, endOfDay));

        for (Event event : events) {
            LocalTime startTime = parseTime(event.startTime());
            LocalTime endTime = parseTime(event.endTime());
            List<TimeSlot> updatedFreeTimeSlots = new ArrayList<>();

            for (TimeSlot slot : freeTimeSlots) {
                if (startTime.isAfter(slot.endTime()) || endTime.isBefore(slot.startTime())) {
                    updatedFreeTimeSlots.add(slot);
                } else {
                    if (slot.startTime().isBefore(startTime)) {
                        updatedFreeTimeSlots.add(new TimeSlot(slot.startTime(), startTime));
                    }
                    if (slot.endTime().isAfter(endTime)) {
                        updatedFreeTimeSlots.add(new TimeSlot(endTime, slot.endTime()));
                    }
                }
            }
            freeTimeSlots = updatedFreeTimeSlots;
        }

        return freeTimeSlots;
    }

    private List<TimeSlot> findCommonTimeSlots(List<TimeSlot> slots1, List<TimeSlot> slots2) {
        List<TimeSlot> commonSlots = new ArrayList<>();

        for (TimeSlot slot1 : slots1) {
            for (TimeSlot slot2 : slots2) {
                LocalTime latestStart = slot1.startTime().isAfter(slot2.startTime()) ? slot1.startTime() : slot2.startTime();
                LocalTime earliestEnd = slot1.endTime().isBefore(slot2.endTime()) ? slot1.endTime() : slot2.endTime();
                if (latestStart.isBefore(earliestEnd)) {
                    commonSlots.add(new TimeSlot(latestStart, earliestEnd));
                }
            }
        }

        return commonSlots;
    }
}

