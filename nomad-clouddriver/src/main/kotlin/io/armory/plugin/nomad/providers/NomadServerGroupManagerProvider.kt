package io.armory.plugin.nomad.providers

import com.netflix.spinnaker.clouddriver.model.ServerGroupManagerProvider
import com.netflix.spinnaker.kork.plugins.api.spring.ExposeToApp
import io.armory.plugin.nomad.model.NomadServerGroupManager
import io.armory.plugin.nomad.model.NomadServerGroupSummary
import org.springframework.stereotype.Component

@Component
@ExposeToApp
class NomadServerGroupManagerProvider(private val clusterProvider: NomadClusterProvider) : ServerGroupManagerProvider<NomadServerGroupManager> {
    override fun getServerGroupManagersByApplication(application: String): Set<NomadServerGroupManager> {
        val clusters = clusterProvider.getClusterSummaries(application)
        return clusters.values.flatten().map { cluster ->
            val serverGroupSummaries = cluster.serverGroups.map { serverGroup ->
                NomadServerGroupSummary(cluster.account, serverGroup)
            }.toSet()
            NomadServerGroupManager(cluster, serverGroupSummaries)
        }.toSet()
    }
}
