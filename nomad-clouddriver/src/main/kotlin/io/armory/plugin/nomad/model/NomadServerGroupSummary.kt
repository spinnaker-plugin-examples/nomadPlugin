package io.armory.plugin.nomad.model

import com.netflix.spinnaker.clouddriver.model.ServerGroupSummary
import com.netflix.spinnaker.moniker.Moniker

class NomadServerGroupSummary(
        private val account: String,
        val serverGroup: NomadServerGroup) : ServerGroupSummary {
    override fun getName(): String {
        return serverGroup.name
    }

    override fun getAccount(): String {
        return account
    }

    override fun getRegion(): String {
        return serverGroup.region
    }

    override fun getMoniker(): Moniker {
        return serverGroup.moniker
    }
}