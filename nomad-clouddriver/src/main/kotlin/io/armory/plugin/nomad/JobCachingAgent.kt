package io.armory.plugin.nomad

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.hashicorp.nomad.apimodel.JobListStub
import com.netflix.spectator.api.DefaultRegistry
import com.netflix.spinnaker.cats.agent.*
import com.netflix.spinnaker.cats.cache.DefaultCacheData
import com.netflix.spinnaker.cats.provider.ProviderCache
import com.netflix.spinnaker.clouddriver.cache.OnDemandAgent
import com.netflix.spinnaker.clouddriver.cache.OnDemandAgent.OnDemandResult
import com.netflix.spinnaker.clouddriver.cache.OnDemandAgent.OnDemandType
import com.netflix.spinnaker.clouddriver.cache.OnDemandMetricsSupport

class JobCachingAgent(val account: NomadCredentials) : CachingAgent, OnDemandAgent, AccountAware {

    private val objectMapper = ObjectMapper()
    private val registry = DefaultRegistry()
    private val metricsSupport = OnDemandMetricsSupport(
            registry,
            this,
            "${NomadCloudProvider.ID}:${NomadCloudProvider.ID}:${OnDemandType.Job}"
    )

    private val types: Set<AgentDataType> = setOf(
            AgentDataType.Authority.AUTHORITATIVE.forType(Namespace.JOBS.ns)
    )

    override fun getProviderName() = NomadCloudProvider::class.java.name

    override fun getAccountName() = account.name

    override fun getAgentType() = JobCachingAgent::class.java.simpleName

    override fun getProvidedDataTypes() = types

    override fun getOnDemandAgentType() = "$agentType-OnDemand"

    override fun getMetricsSupport() = metricsSupport

    override fun handles(type: OnDemandAgent.OnDemandType?, cloudProvider: String?) =
            type == OnDemandType.Job && cloudProvider == NomadCloudProvider.ID

    /**
     * The method names of caching agents don't make a lot of sense to me: It's unclear when they're actually invoked
     * and as part of what lifecycle. I don't have suggestions yet on what to name them.
     */
    override fun handle(providerCache: ProviderCache?, data: MutableMap<String, *>?): OnDemandAgent.OnDemandResult? {
        return OnDemandResult(agentType,
                loadData(providerCache),
                emptyMap())
    }

    override fun pendingOnDemandRequests(providerCache: ProviderCache) : Collection<Map<String, Any>> {
        val keys = providerCache.getIdentifiers(Namespace.JOBS.ns)
        val accountKeys = keys.filter { Keys().parseKey(it)["account"] == accountName }
        return providerCache.getAll(Namespace.JOBS.ns, accountKeys).map {
            mapOf(
                    "account" to accountName,
                    "details" to it.attributes
            )
        }
    }

    override fun loadData(providerCache: ProviderCache?): CacheResult {
        val jobs = getJobs()
        val data = jobs.map {
            // I'm not a fan of this. Spinnaker does far too much value conversions between maps and domain objects.
            // What would it look like if we had a CacheData implementation that supported actual types, rather than
            // forcing developers to convert back/forth between maps?
            val attributes = objectMapper.convertValue(it, object: TypeReference<Map<String, Any>>() {})
            DefaultCacheData(
                    Keys.getJobKey(account.name, it.name),
                    attributes,
                    emptyMap()
            )
        }
        val cacheResults = mapOf(Namespace.JOBS.ns to data)
        return DefaultCacheResult(cacheResults)
    }

    private fun getJobs(): List<JobListStub> {
        val response = account.credentials.jobsApi.list()
        return response.value
    }

}
