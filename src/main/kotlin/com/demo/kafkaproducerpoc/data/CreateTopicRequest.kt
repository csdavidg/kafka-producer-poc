package com.demo.kafkaproducerpoc.data

import org.apache.kafka.clients.admin.NewTopic

data class CreateTopicRequest(val name: String, val numPartitions: Int, val replicationFactor: Short)

fun CreateTopicRequest.toNewTopic() : NewTopic {
    return NewTopic(name, numPartitions, replicationFactor)
}