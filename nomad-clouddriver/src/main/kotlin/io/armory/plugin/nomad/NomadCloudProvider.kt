package io.armory.plugin.nomad

import com.netflix.spinnaker.cats.agent.AgentProvider
import com.netflix.spinnaker.cats.agent.AgentSchedulerAware
import com.netflix.spinnaker.cats.cache.Cache
import com.netflix.spinnaker.clouddriver.cache.SearchableProvider
import com.netflix.spinnaker.clouddriver.cache.SearchableProvider.SearchableResource
import com.netflix.spinnaker.clouddriver.core.CloudProvider
import com.netflix.spinnaker.kork.plugins.api.spring.ExposeToApp
import io.armory.plugin.nomad.cache.Keys
import io.armory.plugin.nomad.cache.Namespace
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.util.*

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@ExposeToApp
class NomadCloudProvider(agentProviders: Collection<AgentProvider>) : AgentSchedulerAware(), SearchableProvider, CloudProvider {

    companion object Nomad {
        val ID = "nomad"
        val PROVIDER_NAME = NomadCloudProvider::class.java.name
    }

    val nomadAgents = agentProviders
            .filter { it.supports(PROVIDER_NAME) }
            .map { it.agents(null) }
            .flatten()

    override fun getId() = ID

    override fun getDisplayName() = "Nomad"

    override fun getOperationAnnotationType() = NomadOperation::class.java

    override fun getProviderName() = NomadCloudProvider::class.java.name

    override fun getAgents() = nomadAgents

    override fun getDefaultCaches() = mutableSetOf(Namespace.JOBS.ns)

    override fun getSearchResultHydrators() = Collections.singletonMap(
            SearchableResource(Namespace.JOBS.ns, ID),
            JobsSearchResultHydrator())

    override fun getUrlMappingTemplates() = mapOf<String, String>()

    override fun parseKey(key: String?): Map<String, String> {
        return Keys().parseKey(key)
    }

}

class JobsSearchResultHydrator : SearchableProvider.SearchResultHydrator {
    @Override
    override fun hydrateResult(cacheView: Cache, result: MutableMap<String, String?>, id: String): Map<String, String?> {
        // needed by deck to render correctly in infrastructure search results
        result["job"] = result.get("name")
        return result
    }
}
