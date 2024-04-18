package com.uz.redpandabackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record Person(
        @JsonProperty("_id")
        String id,
        @JsonProperty("first_name")
        String firstName,
        @JsonProperty("last_name")
        String lastName,
        @JsonProperty("work_schedule")
        List<Event> workSchedule
) {
}
