package com.example.reactivespring.resource;

import com.example.reactivespring.model.Person;
import com.example.reactivespring.repository.PersonReactiveRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class PersonResource {
    static final String PEOPLE ="/people";

    private PersonReactiveRepository personReactiveRepository;

    public PersonResource(PersonReactiveRepository personReactiveRepository) {
        this.personReactiveRepository = personReactiveRepository;
    }

    @GetMapping(value = PEOPLE)
    Flux<Person> getAll(){
        return personReactiveRepository.findAll();
    }
}
