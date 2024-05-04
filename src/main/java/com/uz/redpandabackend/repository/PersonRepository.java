package com.uz.redpandabackend.repository;

import com.uz.redpandabackend.model.Person;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PersonRepository extends MongoRepository<Person, String> {
}
