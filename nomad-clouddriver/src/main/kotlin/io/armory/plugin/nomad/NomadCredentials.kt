package io.armory.plugin.nomad

import com.hashicorp.nomad.javasdk.NomadApiClient
import com.hashicorp.nomad.javasdk.NomadApiConfiguration
import com.netflix.spinnaker.clouddriver.security.AccountCredentials

data class NomadCredentials(
        private val name: String,
        private val environment: String,
        val config: NomadApiConfiguration
) : AccountCredentials<NomadApiClient> {

    override fun getName() = name

    override fun getEnvironment() = environment

    override fun getCloudProvider() = NomadCloudProvider.ID

    override fun getAccountType() = cloudProvider

    override fun getCredentials(): NomadApiClient {
        return NomadApiClient(config)
    }

}
