package io.armory.plugin.nomad.model

import com.hashicorp.nomad.apimodel.Job
import com.netflix.spinnaker.clouddriver.model.Cluster
import com.netflix.spinnaker.clouddriver.model.LoadBalancer
import com.netflix.spinnaker.moniker.Moniker
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
            val appParts = NomadApplication.parseName(name)
            val parts = appParts.jobId.split("-")
            return Name(appParts.namespace, parts[0], parts[1])
        }
    }

    val namespace = job.namespace

    val application = "$namespace.${job.id}"

    override fun getMoniker(): Moniker {
        return Moniker.builder()
                .app(application)
                .cluster(name)
                .detail(taskGroupName)
                .build()
    }

    override fun getLoadBalancers(): Set<LoadBalancer> {
        return emptySet()
    }

    override fun getName(): String {
        return "$application-$taskGroupName"
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