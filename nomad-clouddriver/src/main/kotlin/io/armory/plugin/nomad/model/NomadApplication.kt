package io.armory.plugin.nomad.model

import com.netflix.spinnaker.clouddriver.model.Application

class NomadApplication(
        val namespace: String,
        val jobId: String,
        private val clusters: Collection<NomadCluster>
) : Application {

    data class Name(
            val namespace: String,
            val jobId: String
    )

    companion object NomadApplicationCompanion {
        fun parseName(name: String): Name {
            val parts = name.split("-")
            return Name(parts[0], parts[1])
        }
    }

    override fun getAttributes(): Map<String, String> {
        return emptyMap()
    }

    override fun getClusterNames(): Map<String, Set<String>> {
        return clusters.groupBy { it.accountName }.mapValues { it.value.map { it.name }.toSet() }
    }

    override fun getName(): String {
        return "$namespace-$jobId"
    }

}
