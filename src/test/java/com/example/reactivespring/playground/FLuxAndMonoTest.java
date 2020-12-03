package com.example.reactivespring.playground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FLuxAndMonoTest {

    @Test
    void fluxWith3Elements() {

        Flux<String> stringFlux = Flux.just("First", "Second", "Third")
                                        .log();

        stringFlux.subscribe(System.out::println);
    }

    @Test
    void fluxWithError() {
        Flux<String> stringFlux = Flux.just("First", "Second", "Third")
                .concatWith(Flux.error(new RuntimeException("An error in the stream")))
                .concatWith(Flux.just("Fourth - after error"))
                .log();

        stringFlux.subscribe(
                System.out::println,
                System.err::println,
                () -> System.out.println("That's all folks!")
        );
    }

    @Test
    void givenFluXWith3Strings_should3StringsAndComplete(){
        Flux<String> stringFlux = Flux.just("First", "Second", "Third")
                .log();

        StepVerifier
                .create(stringFlux)
                .expectNext("First")
                .expectNext("Second")
                .expectNext("Third")
                .verifyComplete();

    }
}
