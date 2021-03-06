package io.armory.plugin.nomad.model

import com.hashicorp.nomad.apimodel.*
import com.netflix.spinnaker.clouddriver.model.HealthState
import com.netflix.spinnaker.clouddriver.model.Instance
import com.netflix.spinnaker.clouddriver.model.ServerGroup
import com.netflix.spinnaker.moniker.Moniker
import io.armory.plugin.nomad.NomadCloudProvider

class NomadServerGroup(
        val job: Job,
        private val taskGroupName: String,
        private val instances: Set<NomadInstance>) : ServerGroup {

    data class Name(
            val namespace: String,
            val jobId: String,
            val taskGroup: String,
            val jobVersion: String
    )

    companion object NomadClusterCompanion {
        fun parseName(name: String): Name {
            val appParts = NomadApplication.parseName(name)
            val parts = appParts.jobId.split("-")
            return Name(appParts.namespace, parts[0], parts[1], parts[2])
        }
    }

    val taskGroup = job.taskGroups.find { it.name == taskGroupName }!!

    val application = "${job.namespace}.${job.id}"

    val cluster = "$application-${taskGroup.name}"

    override fun getMoniker(): Moniker {
        return Moniker.builder()
                .app(application)
                .cluster(cluster)
                .detail(taskGroupName)
                .sequence(job.version.toInt())
                .build()
    }

    override fun getName(): String {
        return "$cluster-${job.version}"
    }

    override fun getCapacity(): ServerGroup.Capacity {
        val count = taskGroup.count
        return ServerGroup.Capacity(
                count,
                count,
                count
        )
    }

    override fun getZones(): Set<String> {
        return job.datacenters.toSet()
    }

    override fun getCloudProvider(): String {
        return NomadCloudProvider.ID
    }

    override fun getRegion(): String {
        return job.namespace
    }

    override fun isDisabled(): Boolean {
        return false
    }

    override fun getImageSummary(): ServerGroup.ImageSummary {
        return NomadImageSummary(name, taskGroup.tasks.first())
    }

    override fun getImagesSummary(): ServerGroup.ImagesSummary {
        return NomadImagesSummary(taskGroup.tasks.map {
            NomadImageSummary(name, it)
        })
    }

    override fun getType(): String {
        return NomadCloudProvider.ID
    }

    override fun getInstanceCounts(): ServerGroup.InstanceCounts {
        val instanceCounts = ServerGroup.InstanceCounts()
        instances.forEach {
            when(it.healthState) {
                HealthState.Starting -> instanceCounts.starting = instanceCounts.starting + 1
                HealthState.Up -> instanceCounts.up = instanceCounts.up + 1
                HealthState.Succeeded -> instanceCounts.outOfService = instanceCounts.outOfService + 1
                HealthState.OutOfService -> instanceCounts.outOfService = instanceCounts.outOfService + 1
                HealthState.Failed -> instanceCounts.down = instanceCounts.down + 1
                HealthState.Down -> instanceCounts.down = instanceCounts.down + 1
                else -> instanceCounts.unknown = instanceCounts.unknown + 1
            }
        }
        return instanceCounts
    }

    override fun getLaunchConfig(): Map<String, Any> {
        return mapOf(
                job.id to job
        )
    }

    override fun getLoadBalancers(): Set<String> {
        return emptySet()
    }

    override fun getCreatedTime(): Long {
        return job.submitTime
    }

    override fun getSecurityGroups(): Set<String> {
        return emptySet()
    }

    override fun getInstances(): Set<Instance> {
        return instances
    }
}