package io.armory.plugin.nomad.providers

import com.netflix.spinnaker.clouddriver.model.Application
import com.netflix.spinnaker.clouddriver.model.ApplicationProvider
import com.netflix.spinnaker.kork.plugins.api.spring.ExposeToApp
import io.armory.plugin.nomad.cache.CacheRepository
import io.armory.plugin.nomad.cache.Keys
import io.armory.plugin.nomad.model.NomadApplication
import org.springframework.stereotype.Component

@Component
@ExposeToApp
class NomadApplicationProvider(
        private val cache: CacheRepository,
        private val nomadClusterProvider: NomadClusterProvider) : ApplicationProvider {

    private fun getApplicationsByKey(key: String): Set<Application> {
        val jobs = cache.findJobsByKey(key)
        return jobs.map { job ->
            val clusters = nomadClusterProvider.getClustersByKey(
                    Keys.getDeploymentKey("*", job.jobSummary.namespace, job.id, "*")
            ).values.flatten()
            NomadApplication(job.jobSummary.namespace, job.jobSummary.jobId, clusters)
        }.toSet()
    }

    override fun getApplications(expand: Boolean): Set<Application> {
        return getApplicationsByKey(Keys.getJobKey("*", "*", "*"))
    }

    override fun getApplication(name: String): Application? {
        val parsedName = NomadApplication.parseName(name)
        return getApplicationsByKey(Keys.getJobKey("*", parsedName.namespace, parsedName.jobId)).firstOrNull()
    }
}
