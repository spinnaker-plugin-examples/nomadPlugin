package io.armory.plugin.nomad.converters

import com.fasterxml.jackson.databind.ObjectMapper
import com.netflix.spinnaker.clouddriver.security.AccountCredentialsProvider
import dev.minutest.junit.JUnit5Minutests
import dev.minutest.rootContext
import io.mockk.mockk
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class UpsertNomadJobAtomicOperationConverterTest : JUnit5Minutests {

    val credentialsProvider =  mockk<AccountCredentialsProvider>()

    val op = UpsertNomadJobAtomicOperationConverter(ObjectMapper(), credentialsProvider)

    val input: Map<String?, Any?> = mapOf("job" to mapOf(
            "ID" to "example",
            "Name" to "example",
            "Type" to "service",
            "Priority" to 50
    ))

    fun tests() = rootContext {
        test("convertDescription") {
            val actualJob = op.convertDescription(input).job!!
            expectThat(actualJob.id).isEqualTo("example")
            expectThat(actualJob.name).isEqualTo("example")
            expectThat(actualJob.type).isEqualTo("service")
            expectThat(actualJob.priority).isEqualTo(50)
        }
    }
}
