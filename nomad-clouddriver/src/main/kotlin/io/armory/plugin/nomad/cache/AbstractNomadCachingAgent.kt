package io.armory.plugin.nomad.cache

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.hashicorp.nomad.javasdk.NomadApiClient
import com.netflix.spectator.api.DefaultRegistry
import com.netflix.spinnaker.cats.agent.*
import com.netflix.spinnaker.cats.cache.DefaultCacheData
import com.netflix.spinnaker.cats.provider.ProviderCache
import com.netflix.spinnaker.clouddriver.cache.OnDemandAgent
import com.netflix.spinnaker.clouddriver.cache.OnDemandAgent.OnDemandResult
import com.netflix.spinnaker.clouddriver.cache.OnDemandMetricsSupportable
import com.netflix.spinnaker.clouddriver.cache.OnDemandType
import io.armory.plugin.nomad.NomadCloudProvider
import io.armory.plugin.nomad.NomadCredentials

abstract class AbstractNomadCachingAgent<T>(private val objectMapper: ObjectMapper, val account: NomadCredentials) : CachingAgent, OnDemandAgent, AccountAware {

    abstract val onDemandType: OnDemandType
    abstract val namespace: Namespace

    protected val client = NomadApiClient(account.credentials)
    protected val registry = DefaultRegistry()

    override fun getProviderName() = NomadCloudProvider::class.java.name

    override fun getAccountName() = account.name

    override fun getAgentType() = this::class.java.simpleName

    override fun getProvidedDataTypes() = setOf(AgentDataType.Authority.AUTHORITATIVE.forType(namespace.ns))

    override fun getOnDemandAgentType() = "$agentType-OnDemand"

    override fun handles(type: OnDemandType?, cloudProvider: String?) =
            type == onDemandType && cloudProvider == NomadCloudProvider.ID


    override fun handle(providerCache: ProviderCache?, data: MutableMap<String, *>?): OnDemandAgent.OnDemandResult? {
        return OnDemandResult(agentType,
                loadData(providerCache),
                emptyMap())
    }

    override fun pendingOnDemandRequests(providerCache: ProviderCache) : Collection<Map<String, Any>> {
        val keys = providerCache.getIdentifiers(namespace.ns)
        val accountKeys = keys.filter { Keys().parseKey(it)["account"] == accountName }
        return providerCache.getAll(namespace.ns, accountKeys).map {
            mapOf(
                    "account" to accountName,
                    "details" to it.attributes
            )
        }
    }

    override fun loadData(providerCache: ProviderCache?): CacheResult {
        val items = getItems()
        val data = items.map {
            val attributes = objectMapper.convertValue(it, object: TypeReference<Map<String, Any>>() {})
            DefaultCacheData(
                    getItemKey(it),
                    attributes,
                    emptyMap()
            )
        }
        val cacheResults = mapOf(namespace.ns to data)
        return DefaultCacheResult(cacheResults)
    }

    override fun getMetricsSupport(): OnDemandMetricsSupportable {
        TODO("Not yet implemented")
    }

    abstract fun getItems(): List<T>

    abstract fun getItemKey(item: T): String

}
