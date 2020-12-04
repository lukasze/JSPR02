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

    TODO 1 dependency (reactive web)

    TODO 2 controller zwracajacy Flux<Person>

    TODO 3 uzyj embededMongo dla wystartowanej aplikacji (wykomentuj scope z zaleznosci)

    TODO 4   Przetestuj w przegladarce

    TODO 5 Zainicjalizuj z CommandLineRunner

    TODO 6 Co trzeba zrobic, zeby webflux zwrocil strumien?
 */

