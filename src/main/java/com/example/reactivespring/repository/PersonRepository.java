package com.example.reactivespring.repository;

import com.example.reactivespring.model.Person;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PersonRepository extends MongoRepository<Person, Long> {
}
