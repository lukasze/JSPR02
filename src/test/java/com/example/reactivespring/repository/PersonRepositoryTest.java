package com.example.reactivespring.repository;


import com.example.reactivespring.model.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class PersonRepositoryTest {
    @Autowired
    private PersonRepository personRepository;

    @Test
    void givenAlistOfPerson_whenSaveAndFindAll_shouldReturnAllObjects(){
        // given
        assertTrue(personRepository.findAll().isEmpty());
        var  jimiHendrix = new Person(1L, "Jimi", "Hendrix");
        var  patMehteny = new Person(2L, "Pat", "Metheny");
        var  jacoPastorius = new Person(3L, "Jaco", "Pastorius");

        var listToBeSaved = List.of(jimiHendrix, patMehteny, jacoPastorius);
        // when
        personRepository.saveAll(listToBeSaved);

        var aListFromDB = personRepository.findAll();

        // then
        assertAll(
                () -> assertEquals(3, aListFromDB.size()),
                () -> assertEquals(jimiHendrix, aListFromDB.get(0)),
                () -> assertEquals(patMehteny, aListFromDB.get(1)),
                () -> assertEquals(jacoPastorius, aListFromDB.get(2))
        );


    }


}