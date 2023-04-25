package com.demo.kafkaproducerpoc.config

import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.KafkaAdminClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KafkaClientConfiguration {

    @Value("\${kafka.brokers}")
    lateinit var kafkaBrokers: String
    
    @Bean
    fun kafkaAdminClient(): AdminClient {
        return KafkaAdminClient
            .create(
                mutableMapOf<String, Any>(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG to kafkaBrokers)
            )
    }

}