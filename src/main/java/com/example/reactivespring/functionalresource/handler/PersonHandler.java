package com.example.reactivespring.functionalresource.handler;

import com.example.reactivespring.model.Person;
import com.example.reactivespring.repository.PersonReactiveRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;


@Component
public class PersonHandler {

    PersonReactiveRepository personReactiveRepository;

    public PersonHandler(PersonReactiveRepository personReactiveRepository) {
        this.personReactiveRepository = personReactiveRepository;
    }

    public Mono<ServerResponse> getAllItems(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(personReactiveRepository.findAll(), Person.class);
    }

    public Mono<ServerResponse> getOneItem(ServerRequest serverRequest) {
        Long id = Long.parseLong(serverRequest.pathVariable("id"));
        Mono<Person> personMono = personReactiveRepository.findById(id);
        return personMono.
                // return if found
                        flatMap(
                        person -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(fromValue(person)))
                // return if not found
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> create(ServerRequest serverRequest) {
        Mono<Person> personToBeInserted = serverRequest.bodyToMono(Person.class);
        return personToBeInserted
                .flatMap(
                        person -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(personReactiveRepository.save(person), Person.class)
                );
    }
}
