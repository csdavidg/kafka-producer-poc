package com.demo.kafkaproducerpoc.controllers

import com.demo.kafkaproducerpoc.data.CreateTopicRequest
import com.demo.kafkaproducerpoc.service.AdminService
import org.apache.kafka.clients.admin.ConfigEntry
import org.apache.kafka.clients.admin.TopicDescription
import org.apache.kafka.common.Uuid
import org.apache.kafka.common.config.ConfigResource
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin")
class AdminController(val adminService: AdminService) {

    @GetMapping("/topics/list")
    fun listTopics(): Set<String> {
        return adminService.listTopics()
    }

    @PostMapping("/topics/create")
    fun createTopic(@RequestBody createTopicRequest: CreateTopicRequest): Uuid? {
        return adminService.createTopic(createTopicRequest)
    }

    @GetMapping("/topics/describe/{topicName}")
    fun describeTopics(@PathVariable topicName: String): MutableMap<String, String>? {
        return adminService.describeTopics(topicName)
    }

    @PutMapping("/topics/{topicName}/increase/partitions/{numPartitions}")
    fun increasePartitions(@PathVariable topicName: String, @PathVariable numPartitions: Int) {
        adminService.increaseTopicPartitions(topicName, numPartitions)
    }

    @GetMapping("/broker/describe/config/{number}")
    fun describeConfigs(@PathVariable number: Int): MutableMap<String, String>? {
        return adminService.describeBrokerConfigs(number)
    }

    @GetMapping("/consumer/group/list")
    fun listConsumerGroups(): List<String>? {
        return adminService.listConsumerGroups()
    }

    @GetMapping("/consumer/group/describe/{cGroupName}")
    fun describeConsumerGroup(@PathVariable cGroupName: String): MutableMap<String, String>? {
        return adminService.describeConsumerGroup(cGroupName)
    }

    @DeleteMapping("/delete/records/topic/{topicName}/consumer/group/{cGroupName}")
    fun deleteRecordsFromTopic(@PathVariable cGroupName: String, @PathVariable topicName: String) {
        adminService.deleteOlderOffsets(cGroupName, topicName)
    }

    @GetMapping("/consumer/group/offset/{cGroupName}")
    fun getOffsets(@PathVariable cGroupName: String): List<String> {
        return adminService.getLatestCommittedOffset(cGroupName)
    }

    @GetMapping("/cluster/describe")
    fun describeCluster(): Map<String, String> {
        return adminService.describeCluster()
    }
}