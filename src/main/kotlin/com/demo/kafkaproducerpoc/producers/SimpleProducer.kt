package com.demo.kafkaproducerpoc.producers

import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class SimpleProducer(private val template: KafkaTemplate<String, Any>) {

    @Value("\${kafka.topic}")
    lateinit var topicName: String

    fun sendMessage(message: String) {
        var producerRecord = ProducerRecord<String, Any>(topicName, "keyValue", message)
        //Producing a message without key
        //var producerRecord = ProducerRecord<String, Any>(topicName, message)
        template.send(producerRecord)
    }

}