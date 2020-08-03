package io.armory.plugin.nomad.cache

import com.fasterxml.jackson.databind.ObjectMapper
import com.hashicorp.nomad.apimodel.AllocationListStub
import com.netflix.spinnaker.clouddriver.cache.OnDemandType
import io.armory.plugin.nomad.*

class AllocationCachingAgent(objectMapper: ObjectMapper, account: NomadCredentials) :
        AbstractNomadCachingAgent<AllocationListStub>(objectMapper, account) {

    override val onDemandType = OnDemandType("Allocation")
    override val namespace = Namespace.ALLOCATIONS

    override fun getItems(): List<AllocationListStub> {
        val response = client.allocationsApi.list()
        return response.value
    }

    override fun getItemKey(item: AllocationListStub): String {
        return Keys.getAllocationKey(account.name, item)
    }

}
