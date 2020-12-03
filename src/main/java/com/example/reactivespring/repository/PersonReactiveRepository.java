package com.example.reactivespring.repository;

import com.example.reactivespring.model.Person;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface PersonReactiveRepository extends ReactiveMongoRepository<Person, Long> {
}
