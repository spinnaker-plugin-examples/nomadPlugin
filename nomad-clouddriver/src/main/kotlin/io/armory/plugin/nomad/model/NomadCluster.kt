package io.armory.plugin.nomad.model

import com.hashicorp.nomad.apimodel.Job
import com.netflix.spinnaker.clouddriver.model.Cluster
import com.netflix.spinnaker.clouddriver.model.LoadBalancer
import io.armory.plugin.nomad.NomadCloudProvider

class NomadCluster(val account: String,
                   private val job: Job,
                   private val serverGroups: Set<NomadServerGroup>,
                   private val taskGroupName: String) : Cluster {

    data class Name(
            val namespace: String,
            val jobId: String,
            val taskGroup: String
    )

    companion object NomadClusterCompanion {
        fun parseName(name: String): Name {
            val parts = name.split("-")
            return Name(parts[0], parts[1], parts[2])
        }
    }

    override fun getLoadBalancers(): Set<out LoadBalancer> {
        return emptySet()
    }

    override fun getName(): String {
        return "${job.namespace}-${job.id}-$taskGroupName"
    }

    override fun getAccountName(): String {
        return account
    }

    override fun getType(): String {
        return NomadCloudProvider.ID
    }

    override fun getServerGroups(): Set<NomadServerGroup> {
        return serverGroups
    }
}