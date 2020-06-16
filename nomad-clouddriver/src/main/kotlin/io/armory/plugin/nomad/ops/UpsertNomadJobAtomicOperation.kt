package io.armory.plugin.nomad.ops

import com.netflix.spinnaker.clouddriver.orchestration.AtomicOperation
import io.armory.plugin.nomad.NomadCredentials
import io.armory.plugin.nomad.descriptions.UpsertNomadJobDescription

class UpsertNomadJobAtomicOperation(val description: UpsertNomadJobDescription, val account: NomadCredentials) : AtomicOperation<Any> {

    /**
     * I'd like to audit how many AtomicOperations actually use `priorOutputs`. I think only one or two places use it
     * and I suspect they could be refactored such that this isn't necessary anymore.
     */
    override fun operate(priorOutputs: MutableList<Any?>?): UpsertNomadJobResponse {
        val r = account.credentials.jobsApi.register(description.job)
        val statusLine = r.httpResponse.statusLine
        return UpsertNomadJobResponse(r.value, r.rawEntity, statusLine.statusCode, statusLine.reasonPhrase)
    }

}

data class UpsertNomadJobResponse(
        val value: String,
        val rawEntity: String,
        val statusCode: Int,
        val reason: String
)
