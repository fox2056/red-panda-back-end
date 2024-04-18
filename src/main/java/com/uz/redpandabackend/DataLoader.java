package com.uz.redpandabackend;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uz.redpandabackend.model.Person;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class DataLoader {
    public List<Person> loadJsonData() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File("src/main/resources/persons.json"), new TypeReference<List<Person>>() {});
    }
}
