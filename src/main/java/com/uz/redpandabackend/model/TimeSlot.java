package com.uz.redpandabackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalTime;

public record TimeSlot(
        @JsonProperty("start_time")
        LocalTime startTime,
        @JsonProperty("end_time")
        LocalTime endTime
) {
}
