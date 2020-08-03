package io.armory.plugin.nomad.cache

import com.fasterxml.jackson.databind.ObjectMapper
import com.netflix.spinnaker.clouddriver.cache.OnDemandType
import io.armory.plugin.nomad.*

class DeploymentCachingAgent(objectMapper: ObjectMapper, account: NomadCredentials) :
        AbstractNomadCachingAgent<JobDeployment>(objectMapper, account) {

    override val onDemandType = OnDemandType("Deployment")
    override val namespace = Namespace.DEPLOYMENTS

    override fun getItems(): List<JobDeployment> {
        val deploymentsResponse = client.deploymentsApi.list()
        val deployments = deploymentsResponse.value
        return deployments.groupBy { it.jobId }.map { (jobId, deployment) ->
            val jobVersionsResponse = client.jobsApi.versions(jobId)
            val jobVersions = jobVersionsResponse.value.versions
            val deploymentsForJobByVersion = deployment.groupBy { it.jobVersion }
            jobVersions.map { jobVersion ->
                val sortedDeployments = deploymentsForJobByVersion
                        .getOrDefault(jobVersion.version, emptyList()).sortedBy { it.createIndex }
                JobDeployment(account.name, jobVersion, sortedDeployments)
            }
        }.flatten()
    }

    override fun getItemKey(item: JobDeployment): String {
        return Keys.getDeploymentKey(account.name, item.job)
    }

}
