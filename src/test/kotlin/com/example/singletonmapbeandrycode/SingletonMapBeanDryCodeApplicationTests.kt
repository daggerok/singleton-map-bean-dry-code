package com.example.singletonmapbeandrycode

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.returnResult
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.body
import org.springframework.web.reactive.function.client.bodyToFlux
import org.springframework.web.reactive.function.client.bodyToMono
import org.springframework.web.reactive.function.client.toEntity
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.test.test

@SpringBootTest(webEnvironment = RANDOM_PORT)
class SingletonMapBeanDryCodeApplicationTests @Autowired constructor(@LocalServerPort port: Int, builder: WebClient.Builder) {

    val testWebClient = builder.baseUrl("http://127.0.0.1:$port").build()

    @Test
    fun `should test`() {
        // given
        testWebClient.post().uri("/")
            .body(Mono.just(mapOf("body" to "Hello")))
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .toEntity<Message>()
            .log("1")
            .test()
            .expectNextCount(1) // .consumeNextWith { println("sent 1st: $it") }
            .verifyComplete()

        testWebClient.post().uri("/")
            .body(Mono.just(mapOf("body" to "all")))
            .retrieve()
            .toEntity<Message>()
            .log("2")
            .test()
            .expectNextCount(1) // .consumeNextWith { println("sent 2nd: $it") }
            .verifyComplete()

        // then
        testWebClient.get().uri("/")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToFlux<Message>()
            .log("3")
            .test()
            .expectNextCount(2) // .consumeNextWith { println("received 1st: $it") } // .consumeNextWith { println("received 2nd: $it") }
            .verifyComplete()
    }
}
