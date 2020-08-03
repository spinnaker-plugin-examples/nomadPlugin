package io.armory.plugin.nomad.providers

import com.netflix.spinnaker.clouddriver.model.InstanceProvider
import com.netflix.spinnaker.kork.plugins.api.spring.ExposeToApp
import io.armory.plugin.nomad.NomadCloudProvider
import io.armory.plugin.nomad.cache.CacheRepository
import io.armory.plugin.nomad.cache.Keys
import io.armory.plugin.nomad.model.NomadInstance
import org.springframework.stereotype.Component

@Component
@ExposeToApp
class NomadInstanceProvider(private val cacheRepository: CacheRepository) : InstanceProvider<NomadInstance, String> {

    override fun getCloudProvider(): String {
        return NomadCloudProvider.ID
    }

    override fun getConsoleOutput(account: String?, namespace: String?, id: String?): String {
        return ""
    }

    override fun getInstance(account: String, namespace: String, id: String): NomadInstance? {
        val key = Keys.getAllocationKey(account, namespace, "*", "*", id)
        val allocations = cacheRepository.findAllocationsByKey(key)
        val allocation = allocations.firstOrNull()
        return if (allocation != null) {
            NomadInstance(allocation)
        } else {
            null
        }
    }

}
