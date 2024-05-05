package com.uz.redpandabackend.utils;

public record EventInput(
        String date,
        String day,
        String startTime,
        String endTime,
        String subject,
        String classType,
        String room
) {
}
