package com.uz.redpandabackend.repository;

import com.uz.redpandabackend.model.Person;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface PersonRepository extends MongoRepository<Person, String> {
    @Query("{ '_id': ?0 }")
    Optional<Person> findById(String id);

    Optional<Person> findByEmail(String email);
    void deleteByEmail(String email);
}
