package io.armory.plugin.nomad.model

import com.hashicorp.nomad.apimodel.AllocationListStub
import com.netflix.spinnaker.clouddriver.model.HealthState
import com.netflix.spinnaker.clouddriver.model.Instance
import io.armory.plugin.nomad.NomadCloudProvider

class NomadInstance(private val allocation: AllocationListStub) : Instance {

    override fun getProviderType(): String {
        return NomadCloudProvider.ID
    }

    override fun getCloudProvider(): String {
        return NomadCloudProvider.ID
    }

    override fun getLaunchTime(): Long {
        return allocation.createTime
    }

    override fun getName(): String {
        return allocation.id
    }

    override fun getZone(): String {
        return ""
    }

    override fun getHealth(): List<Map<String, Any>> {
        return listOf(allocation.taskStates)
    }

    override fun getHealthState(): HealthState {
        return when(allocation.clientStatus) {
            "pending" -> HealthState.Starting
            "running" -> HealthState.Up
            "complete" -> HealthState.Succeeded
            "failed" -> HealthState.Failed
            "lost" -> HealthState.Down
            else -> HealthState.Unknown
        }
    }
}