package com.demo.kafkaproducerpoc.config

import com.demo.kafkaproducerpoc.controllers.ProducerController
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory

@Configuration
class ProducerConfiguration {

    @Value("\${kafka.brokers}")
    lateinit var kafkaBrokers: String

    @Bean
    fun producerFactory(): ProducerFactory<String, Any> {
        val properties = mutableMapOf<String, Any>()

        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBrokers)
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
        ProducerConfig.PARTITIONER_CLASS_CONFIG

        return DefaultKafkaProducerFactory(properties)
    }

    @Bean
    fun kafkaTemplate(): KafkaTemplate<String, Any> {
        return KafkaTemplate(producerFactory());
    }
}