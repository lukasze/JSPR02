package com.example.reactivespring.resource;

import com.example.reactivespring.model.Person;
import com.example.reactivespring.repository.PersonReactiveRepository;
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

import java.util.List;

import static com.example.reactivespring.config.PersonConstans.PEOPLE;

@SpringBootTest
@DirtiesContext
@AutoConfigureWebTestClient
@Log4j2
@ActiveProfiles("test")
class PersonResourceTest {

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
    void givenInitializedBase_getAll_shouldReturn3Elements() {
        webTestClient.
                get().uri(PEOPLE)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Person.class)
                .hasSize(3);

    }

}
