package com.example.reactivespring.repository;

import com.example.reactivespring.model.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class PersonReactiveRepositoryTest {

    @Autowired
    private PersonReactiveRepository personReactiveRepository;

    // Create 3 people and a list of them
    private final Person jimiHendrix = new Person(1L, "Jimi", "Hendrix");
    private final Person patMetheny = new Person(2L, "Pat", "Metheny");
    private final Person jacoPastorius = new Person(3L, "Jaco", "Pastorius");
    private final Person pacoDeLucia = new Person(4L, "Paco", "De Lucia");
    private final List<Person> listToBeSaved = List.of(jimiHendrix, patMetheny, jacoPastorius);



    @Test
    void givenAnEmptyDB_whenFindAll_shouldReturnOnComplete() {
        // given an empty db

        // when
        StepVerifier
                .create(personReactiveRepository.findAll())
                // then
                .verifyComplete();
    }

    @Test
    @DirtiesContext
    void given3ElementsInDB_whenFindAll_shouldReturn3Elements() {
        // Given a DB with three elements
        prepareDBToHave3ElementsOnly();

        // When
        StepVerifier
                .create(personReactiveRepository.findAll())
                // Then
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    @DirtiesContext
    void givenDBWith3Elements_whenInsert_shouldReturnTheNewObject() {
        // Given a DB with 3 elements
        prepareDBToHave3ElementsOnly();

        // When
        StepVerifier
                .create(personReactiveRepository.insert(pacoDeLucia))
                // Then
                .expectNext(pacoDeLucia)
                .verifyComplete();


    }

    @Test
    @DirtiesContext
    void givenNoElementWithGivenID_whenFindById_shouldReturnOnComplete() {
        // Given a DB with 3 elements
        prepareDBToHave3ElementsOnly();

        // When
        StepVerifier
                .create(personReactiveRepository.findById(pacoDeLucia.getId()))
                // Then
                .verifyComplete();
    }

    @Test
    @DirtiesContext
    void givenDBWith3Elements_whetInsertNewElement_shouldFindById() {
        // Given a DB with 3 elements
        prepareDBToHave3ElementsOnly();

        // When
        StepVerifier
                .create(personReactiveRepository.insert(pacoDeLucia))
                .expectNext(pacoDeLucia)
                .verifyComplete();

        // Then
        StepVerifier
                .create(personReactiveRepository.findById(pacoDeLucia.getId()))
                .expectNext(pacoDeLucia)
                .verifyComplete();
    }

    @Test
    @DirtiesContext
    void givenExistingObject_whenSaveWithUpdatedName_shouldReturnUpdatedObject() {
        // Given a DB with 3 elements
        prepareDBToHave3ElementsOnly();

        // When
        var hendrixRealName = "James Marshall";
        jimiHendrix.setName(hendrixRealName);
        StepVerifier
                .create(personReactiveRepository.save(jimiHendrix))
                // Then - we can use a Predicate
                .expectNextMatches(person -> person.getName().equals(hendrixRealName))
                .verifyComplete();

    }

    @Test
    @DirtiesContext
    void givenDBWith3Elements_whenDeleteOneElement_shouldReturn2Elements() {
        // Given a DB with three elements
        prepareDBToHave3ElementsOnly();
        // When
        StepVerifier
                .create(personReactiveRepository.deleteById(jimiHendrix.getId()))
                .verifyComplete();

        // Then
        StepVerifier
                .create(personReactiveRepository.findAll())
                .assertNext(person -> assertEquals(patMetheny, person))
                .assertNext(person -> assertEquals(jacoPastorius, person))
                .verifyComplete();


    }

    private void prepareDBToHave3ElementsOnly() {
        StepVerifier
                .create(Mono.when(personReactiveRepository.insert(listToBeSaved)))
                .verifyComplete();
    }
}