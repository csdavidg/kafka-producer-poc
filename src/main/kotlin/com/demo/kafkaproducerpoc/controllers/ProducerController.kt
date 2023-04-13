package com.demo.kafkaproducerpoc.controllers

import com.demo.kafkaproducerpoc.producers.SimpleProducer
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ProducerController(val simpleProducer: SimpleProducer) {

    @GetMapping("/producer/{message}")
    fun sendMessage(@PathVariable("message") message: String) {
        simpleProducer.sendMessage(message)
    }

}