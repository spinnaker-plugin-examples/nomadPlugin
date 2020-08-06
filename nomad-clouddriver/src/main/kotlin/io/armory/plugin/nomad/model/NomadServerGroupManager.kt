package io.armory.plugin.nomad.model

import com.netflix.spinnaker.clouddriver.model.ServerGroupManager
import com.netflix.spinnaker.clouddriver.model.ServerGroupSummary

class NomadServerGroupManager(
        val cluster: NomadCluster,
        private val serverGroups: Set<NomadServerGroupSummary>) : ServerGroupManager {
    override fun getName(): String {
        return cluster.name
    }

    override fun getMoniker(): com.netflix.spinnaker.moniker.Moniker {
        return cluster.moniker
    }

    override fun getServerGroups(): Set<ServerGroupSummary> {
        return serverGroups
    }

    override fun getAccount(): String {
        return cluster.account
    }

    override fun getRegion(): String {
        return cluster.namespace
    }
}