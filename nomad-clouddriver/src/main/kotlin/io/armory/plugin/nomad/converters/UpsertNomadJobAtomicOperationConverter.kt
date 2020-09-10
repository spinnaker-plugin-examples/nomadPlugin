package io.armory.plugin.nomad.converters

import com.fasterxml.jackson.databind.ObjectMapper
import com.netflix.spinnaker.clouddriver.orchestration.AtomicOperation
import com.netflix.spinnaker.clouddriver.orchestration.AtomicOperationConverter
import com.netflix.spinnaker.clouddriver.security.AccountCredentialsProvider
import com.netflix.spinnaker.kork.plugins.api.spring.ExposeToApp
import io.armory.plugin.nomad.NomadCredentials
import io.armory.plugin.nomad.NomadOperation
import io.armory.plugin.nomad.NomadOperations
import io.armory.plugin.nomad.descriptions.UpsertNomadJobDescription
import io.armory.plugin.nomad.ops.UpsertNomadJobAtomicOperation
import org.springframework.stereotype.Component
import java.lang.IllegalStateException

@Component
@NomadOperation(NomadOperations.UpsertJob)
@ExposeToApp
class UpsertNomadJobAtomicOperationConverter(
        private val objectMapper: ObjectMapper,
        val accountCredentialsProvider: AccountCredentialsProvider) : AtomicOperationConverter {

    fun getCredentialsObject(name: String): NomadCredentials {
        try {
            val repoCredential = accountCredentialsProvider.getCredentials(name)
            return repoCredential!! as NomadCredentials
        } catch (e: Exception) {
            throw IllegalStateException("credentials not found (name: ${name}, names: ${accountCredentialsProvider.getAll().map{ it.name }}", e)
        }
    }

    override fun convertOperation(input: Map<String?, Any?>): AtomicOperation<*> {
        val credentials: NomadCredentials = getCredentialsObject(input.get("credentials").toString())
        return UpsertNomadJobAtomicOperation(convertDescription(input), credentials)
    }

    override fun convertDescription(input: Map<String?, Any?>): UpsertNomadJobDescription {
        return objectMapper.convertValue(input, UpsertNomadJobDescription::class.java)
    }

}
