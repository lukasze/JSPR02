package com.example.reactivespring.functionalresource.handler;

import com.example.reactivespring.model.Person;
import com.example.reactivespring.repository.PersonReactiveRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.example.reactivespring.config.PersonConstans.FUNCTIONAL_PEOPLE;


@SpringBootTest
@DirtiesContext
@AutoConfigureWebTestClient
@Log4j2
@ActiveProfiles("test")
class PersonHandlerTest {

    private final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private PersonReactiveRepository personReactiveRepository;

    @BeforeEach
    void setupDB() {
        var data = List.of(
                new Person(1L, "Jimi", "Hendrix"),
                new Person(2L, "Pat", "Metheny"),
                new Person(3L, "Jaco", "Pastorius")
        );
        personReactiveRepository
                .deleteAll()
                .thenMany(Flux.fromIterable(data))
                .flatMap(personReactiveRepository::save)
                .doOnNext(person -> log.info("Inserted person:" + person))
                .blockLast();
    }

    @Test
    void givenInitializedBase_functionalPeopleResource_shouldReturn3Elements() {
        webTestClient.
                get().uri(FUNCTIONAL_PEOPLE)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Person.class)
                .hasSize(3);

    }

    @Test
    void givenInitializedDatabase_functionalPeopleIdResource_shouldReturnOneElementForId1() {
        webTestClient.
                get().uri(FUNCTIONAL_PEOPLE.concat("/{id}"), 1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.name", "Jimi");

    }

    @Test
    void givenInitializedDatabase_functionalPeopleIdResource_shouldReturnNotFoundForId10() {
        webTestClient.
                get().uri(FUNCTIONAL_PEOPLE.concat("/{id}"), 10)
                .exchange()
                .expectStatus().isNotFound();

    }

    @Test
    void givenInitializedDatabase_POST_functionalPeopleResource_shouldCreateAndReturn_Person() {
        Person pacoDeLuciaToSave = new Person(4L, "Paco", "De Lucia");
        webTestClient.
                post().uri(FUNCTIONAL_PEOPLE)
                .body(Mono.just(pacoDeLuciaToSave), Person.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Person.class).isEqualTo(pacoDeLuciaToSave);
    }


}