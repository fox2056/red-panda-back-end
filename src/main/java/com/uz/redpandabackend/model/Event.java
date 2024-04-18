package com.uz.redpandabackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Event(
        String date,
        String day,
        @JsonProperty("start_time")
        String startTime,
        @JsonProperty("end_time")
        String endTime,
        String subject,
        @JsonProperty("class_type")
        String classType,
        String room
) {
}
