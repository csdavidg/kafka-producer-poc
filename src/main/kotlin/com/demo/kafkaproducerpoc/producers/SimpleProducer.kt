package com.demo.kafkaproducerpoc.producers

import com.demo.kafkaconsumerpoc.avro.AccountTransaction
import com.demo.kafkaconsumerpoc.avro.TransactionType
import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class SimpleProducer(private val template: KafkaTemplate<String, Any>) {

    @Value("\${kafka.topic}")
    lateinit var topicName: String

    fun sendMessage(message: String) {
        var producerRecord = ProducerRecord<String, Any>(
            topicName, "keyValue",
            AccountTransaction("1234", message, TransactionType.DEBIT, 2.1)
        )
        //Producing a message without key
        //var producerRecord = ProducerRecord<String, Any>(topicName, message)
        template.send(producerRecord)
    }

}