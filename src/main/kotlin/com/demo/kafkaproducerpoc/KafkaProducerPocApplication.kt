package com.demo.kafkaproducerpoc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KafkaProducerPocApplication

fun main(args: Array<String>) {
	runApplication<KafkaProducerPocApplication>(*args)
}
