package com.example.reactivespring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReactiveSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReactiveSpringApplication.class, args);
    }

}

/*
    Mongo Repository + test class - findAll
    TODO 1 dodajemy zaleznosci:
        data mongo db
        lombok
        de.flapdoodle.embed.mongo

    TODO 2 @Document Person

    TODO 3 mongo repository

    TODO 4 Junit5 tests
 */

