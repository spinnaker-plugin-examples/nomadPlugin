package io.armory.plugin.nomad.providers

import com.netflix.spinnaker.clouddriver.model.ClusterProvider
import com.netflix.spinnaker.clouddriver.model.ServerGroup
import com.netflix.spinnaker.kork.plugins.api.spring.ExposeToApp
import io.armory.plugin.nomad.NomadCloudProvider
import io.armory.plugin.nomad.cache.CacheRepository
import io.armory.plugin.nomad.cache.Keys
import io.armory.plugin.nomad.model.NomadApplication
import io.armory.plugin.nomad.model.NomadCluster
import io.armory.plugin.nomad.model.NomadInstance
import io.armory.plugin.nomad.model.NomadServerGroup
import org.springframework.stereotype.Component

@Component
@ExposeToApp
class NomadClusterProvider(private val cacheRepository: CacheRepository) : ClusterProvider<NomadCluster> {

    override fun getCloudProviderId(): String {
        return NomadCloudProvider.ID
    }

    fun getClustersByKey(key: String): Map<String, Set<NomadCluster>> {
        val jobDeployments = cacheRepository.findJobDeploymentsByKey(key)
        return jobDeployments.groupBy { it.account }.mapValues { (account, jobDeployments) ->
            jobDeployments.groupBy { it.job.id }.mapValues { (jobId, jobDeploymentVersions) ->
                val serverGroups: List<NomadServerGroup> = jobDeploymentVersions.map { jobDeployment ->
                    val job = jobDeployment.job
                    val allocations = cacheRepository.findAllocationsByKey(
                            Keys.getAllocationKey(account, job.namespace, job.id, job.version.toString(), "*")
                    )
                    val allocationsByTaskGroup = allocations.groupBy { it.taskGroup }
                    val taskGroupNames = job.taskGroups.map { it.name }
                    taskGroupNames.map { taskGroupName ->
                        val instances = allocationsByTaskGroup
                                .getOrDefault(taskGroupName, emptyList())
                                .map { NomadInstance(it) }.toSet()
                        NomadServerGroup(job, taskGroupName, instances)
                    }
                }.flatten()
                val serverGroupsByTaskGroup = serverGroups.groupBy { it.taskGroup.name }
                val latestJob = jobDeploymentVersions.map { it.job }. sortedBy { it.version }.last()
                serverGroupsByTaskGroup.mapValues { (taskGroupName, serverGroups) ->
                    NomadCluster(account, latestJob, serverGroups.toSet(), taskGroupName)
                }.values
            }.values.flatten().toSet()
        }
    }

    override fun getClusters(): Map<String, Set<NomadCluster>> {
        return getClustersByKey(Keys.getDeploymentKey("*", "*", "*", "*"))
    }

    override fun getClusterSummaries(application: String): Map<String, Set<NomadCluster>> {
        return getClusterDetails(application)
    }

    override fun getClusterDetails(application: String): Map<String, Set<NomadCluster>> {
        val parseName = NomadApplication.parseName(application)
        return getClustersByKey(Keys.getDeploymentKey("*", parseName.namespace, parseName.jobId, "*"))
    }

    override fun getClusters(application: String, account: String): Set<NomadCluster> {
        val parseName = NomadApplication.parseName(application)
        return getClustersByKey(Keys.getDeploymentKey(account, parseName.namespace, parseName.jobId, "*"))
                .getOrDefault(account, emptySet())
    }

    override fun getCluster(application: String, account: String, name: String): NomadCluster? {
        return getCluster(application, account, name,  false)
    }

    override fun getCluster(application: String, account: String, name: String, includeDetails: Boolean): NomadCluster? {
        val parseName = NomadCluster.parseName(name)
        return getClustersByKey(Keys.getDeploymentKey(account, parseName.namespace, parseName.jobId, "*"))
                .getOrDefault(account, emptySet()).firstOrNull()
    }

    override fun getServerGroup(account: String, region: String, name: String): ServerGroup? {
        return getServerGroup(account, region, name, false)
    }

    override fun getServerGroup(account: String, region: String, name: String, includeDetails: Boolean): ServerGroup? {
        val parsedName = NomadServerGroup.parseName(name)
        val jobDeployment = cacheRepository.findJobDeploymentsByKey(
                Keys.getDeploymentKey(account, parsedName.namespace, parsedName.jobId, parsedName.jobVersion)
        ).first()
        val job = jobDeployment.job
        val allocations = cacheRepository.findAllocationsByKey(
                Keys.getAllocationKey(account, job.namespace, job.id, job.version.toString(), "*")
        )
        val allocationsByTaskGroup = allocations.groupBy { it.taskGroup }
        val instances = allocationsByTaskGroup
                .getOrDefault(parsedName.taskGroup, emptyList())
                .map { NomadInstance(it) }.toSet()
        return NomadServerGroup(job, parsedName.taskGroup, instances)
    }

    override fun supportsMinimalClusters(): Boolean {
        return false
    }

}