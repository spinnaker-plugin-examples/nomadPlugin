package io.armory.plugin.nomad.descriptions

import com.hashicorp.nomad.apimodel.Job
import com.netflix.spinnaker.clouddriver.orchestration.OperationDescription

data class UpsertNomadJobDescription(
        var job: Job? = null,
        var credentials: String? = null) : OperationDescription {
}
