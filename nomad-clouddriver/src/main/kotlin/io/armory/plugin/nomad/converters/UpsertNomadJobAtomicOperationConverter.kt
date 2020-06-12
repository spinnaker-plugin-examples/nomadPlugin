package io.armory.plugin.nomad.converters

import com.fasterxml.jackson.databind.ObjectMapper
import com.netflix.spinnaker.clouddriver.orchestration.AtomicOperation
import com.netflix.spinnaker.clouddriver.orchestration.AtomicOperationConverter
import com.netflix.spinnaker.clouddriver.security.AccountCredentialsProvider
import com.netflix.spinnaker.clouddriver.security.ProviderVersion
import io.armory.plugin.nomad.NomadCredentials
import io.armory.plugin.nomad.NomadOperation
import io.armory.plugin.nomad.NomadOperations
import io.armory.plugin.nomad.descriptions.UpsertNomadJobDescription
import io.armory.plugin.nomad.ops.UpsertNomadJobAtomicOperation
import java.lang.IllegalStateException

@NomadOperation(NomadOperations.UpsertJob)
class UpsertNomadJobAtomicOperationConverter(
        val accountCredentialsProvider: AccountCredentialsProvider) : AtomicOperationConverter {

    val objectMapper = ObjectMapper()

    override fun acceptsVersion(version: ProviderVersion?): Boolean {
        return version == ProviderVersion.v1
    }

    fun getCredentialsObject(name: String): NomadCredentials {
        try {
            val repoCredential = accountCredentialsProvider.getCredentials(name)
            return repoCredential!! as NomadCredentials
        } catch (e: Exception) {
            throw IllegalStateException("credentials not found (name: ${name}, names: ${accountCredentialsProvider.getAll().map{ it.name }}", e)
        }
    }

    /**
     * I've always hated the AtomicOperationConverter because it forces two `convertDescriptions` per request.
     *
     * I've changed this interface in my refactor: `convertOperation(description: Any, rawInput: Map<String, Any?>`
     * for backwards compatibility, but really only `description` would be needed. I'd like to see this change make it
     * into the initial `clouddriver-api` refactor, deprecating this method.
     */
    override fun convertOperation(input: Map<Any?, Any?>): AtomicOperation<*> {
        val credentials: NomadCredentials = getCredentialsObject(input.get("credentials").toString())
        return UpsertNomadJobAtomicOperation(convertDescription(input), credentials)
    }

    override fun convertDescription(input: Map<Any?, Any?>): UpsertNomadJobDescription {
        return objectMapper.convertValue(input, UpsertNomadJobDescription::class.java)
    }

}
