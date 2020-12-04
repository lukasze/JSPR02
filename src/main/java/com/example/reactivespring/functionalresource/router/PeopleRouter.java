package com.example.reactivespring.functionalresource.router;

import com.example.reactivespring.functionalresource.handler.PersonHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;


import static com.example.reactivespring.config.PersonConstans.FUNCTIONAL_PEOPLE;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;


@Configuration
public class PeopleRouter {
    @Bean
    public RouterFunction<ServerResponse> peopleRoute(PersonHandler personHandler) {
        return RouterFunctions
                .route(
                        GET(FUNCTIONAL_PEOPLE).and(accept(MediaType.APPLICATION_JSON)),
                        personHandler::getAllItems
                ).andRoute(
                        GET(FUNCTIONAL_PEOPLE + "/{id}").and(accept(MediaType.APPLICATION_JSON)),
                        personHandler::getOneItem
                ).andRoute(
                        POST(FUNCTIONAL_PEOPLE).and(accept(MediaType.APPLICATION_JSON)),
                        personHandler::create
                );
    }
}

