package com.uz.redpandabackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "schedule_app")
public record Person(
        @Id
        @Field("_id")
        String id,

        @Field("first_name")
        String firstName,

        @Field("last_name")
        String lastName,

        @Field("work_schedule")
        List<Event> workSchedule
) {}
