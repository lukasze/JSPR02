package com.example.reactivespring.config;


import com.example.reactivespring.model.Person;
import com.example.reactivespring.repository.PersonReactiveRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;

@Component
@Log4j2
@Profile("!test")
public class InitializeDbRunner implements CommandLineRunner {

    private final PersonReactiveRepository personReactiveRepository;

    public InitializeDbRunner(PersonReactiveRepository personReactiveRepository) {
        this.personReactiveRepository = personReactiveRepository;
    }

    @Override
    public void run(String... args) {

        var listToBeSaved = List.of(
                new Person(1L, "Jimi", "Hendrix"),
                new Person(2L, "Pat", "Metheny"),
                new Person(3L, "Jaco", "Pastorius"),
                new Person(4L, "Paco", "De Lucia")
        );


        personReactiveRepository.deleteAll()
                .thenMany(Flux.fromIterable(listToBeSaved))
                .flatMap(personReactiveRepository::save)
                .thenMany(personReactiveRepository.findAll())
                .subscribe(person -> log.info("Person inserted from Command Line Runner: " + person));
    }
}