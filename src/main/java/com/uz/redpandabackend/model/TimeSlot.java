package com.uz.redpandabackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TimeSlot(
        @JsonProperty("start_time")
        String startTime,
        @JsonProperty("end_time")
        String endTime
) {
}
