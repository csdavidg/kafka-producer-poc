package com.demo.kafkaproducerpoc.service

import com.demo.kafkaproducerpoc.data.CreateTopicRequest
import com.demo.kafkaproducerpoc.data.toNewTopic
import org.apache.kafka.clients.admin.*
import org.apache.kafka.clients.consumer.OffsetAndMetadata
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.Uuid
import org.apache.kafka.common.config.ConfigResource
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.concurrent.CompletableFuture
import java.util.stream.Collectors


@Service
class AdminService(val adminClient: AdminClient) {

    fun listTopics(): Set<String> {
        return adminClient.listTopics()
            .names()
            .get()
    }

    fun createTopic(createTopicRequest: CreateTopicRequest): Uuid? {
        return adminClient.createTopics(listOf(createTopicRequest.toNewTopic()))
            .topicId(createTopicRequest.name)
            .get()
    }

    fun describeTopics(topicName: String): MutableMap<String, String>? {
        return adminClient.describeTopics(listOf(topicName))
            .allTopicNames().get().entries
            .stream().collect(Collectors.toMap({ it.key }, { it.value.toString() }))
    }

    fun increaseTopicPartitions(topicName: String, numPartitions: Int) {
        adminClient.createPartitions(
            mapOf(
                topicName to NewPartitions.increaseTo(4)
            )
        ).all().get()
    }

    fun deleteOlderOffsets(cGroupName: String, topicName: String) {

        val offSets: MutableMap<TopicPartition, OffsetAndMetadata> = adminClient.listConsumerGroupOffsets(cGroupName)
            .partitionsToOffsetAndMetadata().get()

        val requestLatestOffset = offSets.keys.stream()
            .filter { it.topic() == topicName }
            .collect(Collectors.toMap({ it }, {
                OffsetSpec.forTimestamp(
                    LocalDateTime.now().minusMinutes(15)
                        .toInstant(ZoneOffset.UTC).toEpochMilli()
                )
            }))

        val currentOffsets = adminClient.listOffsets(requestLatestOffset)
            .all().get()

        val deleteRecordsRequest = currentOffsets.entries
            .stream()
            .collect(Collectors.toMap({ it.key }, { RecordsToDelete.beforeOffset(it.value.offset()) }))
        adminClient.deleteRecords(deleteRecordsRequest)
    }

    fun describeBrokerConfigs(number: Int): MutableMap<String, String>? {
        val configs = ConfigResource(ConfigResource.Type.BROKER, number.toString())
        return adminClient.describeConfigs(listOf(configs))
            .all().get().entries
            .stream()
            .flatMap { it.value.entries().stream() }
            .collect(Collectors.toMap({ it.name() }, { it.toString() }))

    }

    fun listConsumerGroups(): List<String>? {
        return adminClient.listConsumerGroups().valid().get()
            .stream().map { c -> c.groupId() }.toList()
    }

    fun describeConsumerGroup(consumerGroupName: String): MutableMap<String, String>? {
        return adminClient.describeConsumerGroups(listOf(consumerGroupName))
            .describedGroups().entries
            .stream()
            .collect(Collectors.toMap({ it.key }, { it.value.get().toString() }))
    }

    fun getLatestCommittedOffset(cGroupName: String): List<String> {
        return getLatestCommittedOffsetMap(cGroupName)
            .map { e -> "${e.key} ------> ${e.value}" }
            .toList()
    }

    private fun getLatestCommittedOffsetMap(cGroupName: String): MutableMap<TopicPartition, ListOffsetsResult.ListOffsetsResultInfo> {
        val offSets: MutableMap<TopicPartition, OffsetAndMetadata> = adminClient.listConsumerGroupOffsets(cGroupName)
            .partitionsToOffsetAndMetadata().get()

        val requestLatestOffset = offSets.keys.stream()
            .collect(Collectors.toMap({ it }, { OffsetSpec.latest() }))

        return adminClient.listOffsets(requestLatestOffset)
            .all().get()
    }

    fun describeCluster(): Map<String, String> {
        val clusterInfoFuture = CompletableFuture.supplyAsync { adminClient.describeCluster() }

        return clusterInfoFuture.thenApply { clusterInfo ->
            val clusterId = clusterInfo.clusterId().get()
            val nodes = clusterInfo.nodes().get().toString()
            val authOps = clusterInfo.authorizedOperations().get()?.toString() ?: ""
            val controller = clusterInfo.controller().get().toString()

            mapOf(
                "clusterId" to clusterId,
                "nodes" to nodes,
                "authorizedOperations" to authOps,
                "controller" to controller
            )
        }.get()
    }

}