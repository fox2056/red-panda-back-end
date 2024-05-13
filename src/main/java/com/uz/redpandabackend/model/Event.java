package com.uz.redpandabackend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

public record Event(

        String date,
        String day,

        @Field("start_time")
        String startTime,

        @Field("end_time")
        String endTime,

        String subject,

        @Field("class_type")
        String classType,

        String room
) {
}
