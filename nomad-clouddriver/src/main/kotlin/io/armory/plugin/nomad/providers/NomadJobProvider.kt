package io.armory.plugin.nomad.providers

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.netflix.spinnaker.clouddriver.model.JobProvider
import com.netflix.spinnaker.kork.plugins.api.spring.ExposeToApp
import io.armory.plugin.nomad.NomadCloudProvider
import io.armory.plugin.nomad.cache.CacheRepository
import io.armory.plugin.nomad.cache.Keys
import io.armory.plugin.nomad.cache.Namespace
import io.armory.plugin.nomad.model.NomadJobStatus
import org.springframework.stereotype.Component

@Component
@ExposeToApp
class NomadJobProvider(
        private val objectMapper: ObjectMapper,
        private val cache: CacheRepository) : JobProvider<NomadJobStatus> {

    override fun getPlatform(): String {
        return NomadCloudProvider.ID
    }

    override fun getFileContents(account: String, namespace: String, jobId: String, version: String): Map<String, Any> {
        val jobVersions = cache.findJobDeploymentsByKey(Keys.getDeploymentKey(account, namespace, jobId, version))
        return objectMapper.convertValue(jobVersions.first().job, object: TypeReference<Map<String, Any>>() {})
    }

    override fun cancelJob(account: String?, location: String?, id: String?) {

    }

    override fun collectJob(account: String, namespace: String, jobId: String): NomadJobStatus {
        val jobVersions = cache.findJobDeploymentsByKey(Keys.getDeploymentKey(account, namespace, jobId, "*"))
        val jobVersion = jobVersions.sortedBy { it.job.version }.last()
        return NomadJobStatus(objectMapper, account, jobVersion)
    }
}