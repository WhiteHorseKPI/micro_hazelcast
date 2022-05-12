package com.microservices.facadeservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.*;


@Service
public class FacadeService {

    static Logger logger = LoggerFactory.getLogger(FacadeService.class);

    static List<WebClient> loggingWebClients;
    static WebClient messagesWebClient;


    public FacadeService() {
        loggingWebClients = List.of(
                WebClient.create("http://localhost:8890"),
                WebClient.create("http://localhost:8891"),
                WebClient.create("http://localhost:8892")
        );
        messagesWebClient = WebClient.create("http://localhost:8889");
    }

    public static String messages() {

        WebClient loggingWebClient = getRandomLoggingClient();

        var cachedValues = loggingWebClient
                .get()
                .uri("/log")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        var message = messagesWebClient
                .get()
                .uri("/message")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return String.format("%s: %s", cachedValues, message);
    }

    static Mono<Void> addMessage(@RequestBody String text) {

        Map<String, Object> message = new HashMap<>();
        message.put("UUID", UUID.randomUUID());
        message.put("Text", text);

        WebClient loggingWebClient = getRandomLoggingClient();
        logger.info(loggingWebClient.toString());

        return loggingWebClient.post()
                .uri("/log")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(message), Map.class)
                .retrieve()
                .bodyToMono(Void.class);
    }


    private static WebClient getRandomLoggingClient() {

        final int numberOfLoggingServices = loggingWebClients.size();
        return loggingWebClients.get(new Random().nextInt(numberOfLoggingServices));
    }

}
