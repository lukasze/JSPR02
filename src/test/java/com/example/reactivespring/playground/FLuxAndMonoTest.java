package com.example.reactivespring.playground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;
import java.util.List;

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

    @Test
    void givenFluXWith3Strings_should3StringsAndError(){
        var exceptionMsg = "An error in the stream";
        Flux<String> stringFlux = Flux.just("First", "Second", "Third")
                .concatWith(Flux.error(new RuntimeException(exceptionMsg)))
                .log();

        StepVerifier
                .create(stringFlux)
                .expectNext("First")
                .expectNextCount(2)
//                .expectError(RuntimeException.class)
                .expectErrorMessage(exceptionMsg)
                .verify();

    }

    @Test
    void monoComplete(){
        StepVerifier.create(Mono.just("Just one Mono").log())
                    .expectNext("Just one Mono")
                .verifyComplete();
    }

    @Test
    void monoError(){
        StepVerifier.create(Mono.error(new RuntimeException()))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void givenPredicateShouldFilter(){
        var iterable = List.of("Ania","Jan","Ola");
        Flux<String> stringFlux = Flux.fromIterable(iterable)
                .filter(s -> s.length()<=3)
                .log();

      StepVerifier.create(stringFlux)
                    .expectNextCount(2)
                    .verifyComplete();

    }

    @Test
    void givenLambdaShouldTransofrm(){
        var iterable = List.of("Ania","Jan","Ola");
        Flux<String> stringFlux = Flux.fromIterable(iterable)
                .map(String::toUpperCase)
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("ANIA","JAN","OLA")
                .verifyComplete();

    }

}
