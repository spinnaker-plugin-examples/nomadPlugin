package io.armory.plugin.nomad.cache

import com.fasterxml.jackson.databind.ObjectMapper
import com.netflix.spinnaker.cats.agent.Agent
import com.netflix.spinnaker.cats.agent.AgentProvider
import com.netflix.spinnaker.clouddriver.security.AccountCredentialsProvider
import com.netflix.spinnaker.credentials.Credentials
import io.armory.plugin.nomad.NomadCloudProvider
import io.armory.plugin.nomad.NomadCredentials
import io.armory.plugin.nomad.NomadCredentialsInitializer
import org.springframework.stereotype.Component

@Component
class NomadAgentProvider(
        private val objectMapper: ObjectMapper,
        nomadCredentialsInitializer: NomadCredentialsInitializer,
        val accountCredentialsProvider: AccountCredentialsProvider
) : AgentProvider {

    init {
        nomadCredentialsInitializer.loadCredentials()
    }

    override fun supports(providerName: String?): Boolean {
        return providerName == NomadCloudProvider.PROVIDER_NAME
    }

    override fun agents(credentials: Credentials?): Collection<Agent>? {
        return accountCredentialsProvider.all
                .filter { it is NomadCredentials }
                .map {
                    val credentials = it as NomadCredentials
                    listOf(
                            AllocationCachingAgent(objectMapper, credentials),
                            DeploymentCachingAgent(objectMapper, credentials),
                            JobCachingAgent(objectMapper, credentials))
                }.flatten()
    }

}