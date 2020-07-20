package io.armory.plugin.nomad

import com.netflix.spinnaker.cats.agent.Agent
import com.netflix.spinnaker.cats.agent.AgentProvider
import com.netflix.spinnaker.clouddriver.security.AccountCredentialsProvider
import org.springframework.stereotype.Component

@Component
class NomadAgentProvider(
        nomadCredentialsInitializer: NomadCredentialsInitializer,
        val accountCredentialsProvider: AccountCredentialsProvider
) : AgentProvider {

    init {
        nomadCredentialsInitializer.loadCredentials()
    }

    override fun supports(providerName: String?): Boolean {
        return providerName == NomadCloudProvider.PROVIDER_NAME
    }

    override fun agents(): Collection<Agent> {
        return accountCredentialsProvider.all
                .filter { it is NomadCredentials }
                .map {
                    JobCachingAgent(it as NomadCredentials)
                }
    }

}