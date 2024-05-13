package com.uz.redpandabackend.service;

import com.uz.redpandabackend.model.Event;
import com.uz.redpandabackend.model.TimeSlot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    private LocalTime startOfDay;
    private LocalTime endOfDay;

    private static final Logger logger = LoggerFactory.getLogger(ScheduleService.class);


    public LocalTime parseTime(String time) {
        return LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
    }

    public List<TimeSlot> findCommonFreeTimeSlots(String hourFrom, String hourTo, List<List<Event>> events) {
        String[] hourFromTablicaString = hourFrom.split(":");
        if (hourFromTablicaString.length != 2) {
            hourFromTablicaString[0] = "0";
            hourFromTablicaString[1] = "0";
        }
        startOfDay = LocalTime.of(Integer.parseInt(hourFromTablicaString[0]), Integer.parseInt(hourFromTablicaString[1]));

        String[] hourToTablicaString = hourTo.split(":"); // Poprawiono na hourTo
        if (hourToTablicaString.length != 2) {
            hourToTablicaString[0] = "23";
            hourToTablicaString[1] = "59";
        }
        endOfDay = LocalTime.of(Integer.parseInt(hourToTablicaString[0]), Integer.parseInt(hourToTablicaString[1]));

        System.out.println(startOfDay);
        System.out.println(endOfDay);

        List<List<TimeSlot>> allFreeTimeSlots = events.stream()
                .map(this::findFreeTimeSlots)
                .collect(Collectors.toList());

        return findCommonTimeSlots(allFreeTimeSlots);
    }

    private List<TimeSlot> findFreeTimeSlots(List<Event> events) {

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

    private List<TimeSlot> findCommonTimeSlots(List<List<TimeSlot>> allFreeTimeSlots) {
        if (allFreeTimeSlots.isEmpty()) return Collections.emptyList();

        List<TimeSlot> commonSlots = new ArrayList<>(allFreeTimeSlots.get(0));

        for (int i = 1; i < allFreeTimeSlots.size(); i++) {
            List<TimeSlot> currentSlots = allFreeTimeSlots.get(i);
            List<TimeSlot> newCommonSlots = new ArrayList<>();

            for (TimeSlot commonSlot : commonSlots) {
                for (TimeSlot currentSlot : currentSlots) {
                    LocalTime latestStart = commonSlot.startTime().isAfter(currentSlot.startTime()) ? commonSlot.startTime() : currentSlot.startTime();
                    LocalTime earliestEnd = commonSlot.endTime().isBefore(currentSlot.endTime()) ? commonSlot.endTime() : currentSlot.endTime();
                    if (latestStart.isBefore(earliestEnd)) {
                        newCommonSlots.add(new TimeSlot(latestStart, earliestEnd));
                    }
                }
            }

            commonSlots = newCommonSlots;
            if (commonSlots.isEmpty()) {
                break;
            }
        }

        return commonSlots;
    }
}

