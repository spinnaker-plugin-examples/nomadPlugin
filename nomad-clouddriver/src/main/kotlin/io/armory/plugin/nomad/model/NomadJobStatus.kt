package io.armory.plugin.nomad.model

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.netflix.spinnaker.clouddriver.model.JobState
import com.netflix.spinnaker.clouddriver.model.JobStatus
import io.armory.plugin.nomad.NomadCloudProvider
import io.armory.plugin.nomad.cache.JobDeployment
import java.io.Serializable

class NomadJobStatus(
        private val objectMapper: ObjectMapper,
        private val account: String,
        val jobDeployment: JobDeployment) : JobStatus {

    private val job = jobDeployment.job

    override fun getProvider(): String {
        return NomadCloudProvider.ID
    }

    override fun getCreatedTime(): Long {
        return job.submitTime
    }

    override fun getCompletedTime(): Long {
        return 0
    }

    override fun getLocation(): String {
        return job.namespace
    }

    override fun getName(): String {
        return job.id
    }

    override fun getCompletionDetails(): Map<String, Serializable> {
        return jobDeployment.deployments.map {
            id to objectMapper.convertValue(it, object: TypeReference<HashMap<String, Any>>() {})
        }.toMap()
    }

    override fun getId(): String {
        return job.id
    }

    override fun getJobState(): JobState {
        return when(job.status) {
            "pending" -> JobState.Starting
            "running" -> JobState.Running
            "dead" -> JobState.Failed
            else -> JobState.Unknown
        }
    }

    override fun getAccount(): String {
        return account
    }
}