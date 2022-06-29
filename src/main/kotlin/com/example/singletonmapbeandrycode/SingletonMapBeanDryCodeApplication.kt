package com.example.singletonmapbeandrycode

import java.time.Instant
import java.util.concurrent.ConcurrentHashMap
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class SingletonMapBeanDryCodeApplication

fun main(args: Array<String>) {
    runApplication<SingletonMapBeanDryCodeApplication>(*args)
}

data class Message(
    val body: String = "",
    val timestamp: Instant = Instant.now(),
)

@Configuration
class ConcurrentHashMapBeanConfig {

    @Bean
    fun myConcurrentHashMap(): MutableMap<Instant, Message> =
        ConcurrentHashMap()
}

@RestController
class ReadingResource(private val myConcurrentHashMap: MutableMap<Instant, Message>) {

    @GetMapping
    fun getAllMessages() =
        myConcurrentHashMap.values.sortedByDescending { it.timestamp }
}

@RestController
class WritingResource(private val myConcurrentHashMap: MutableMap<Instant, Message>) {

    @PostMapping
    fun sentMessage(@RequestBody message: Message) =
        myConcurrentHashMap.putIfAbsent(message.timestamp, message)
}
