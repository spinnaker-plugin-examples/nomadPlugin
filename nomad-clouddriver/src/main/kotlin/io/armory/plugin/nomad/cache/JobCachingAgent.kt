package io.armory.plugin.nomad.cache

import com.fasterxml.jackson.databind.ObjectMapper
import com.hashicorp.nomad.apimodel.JobListStub
import com.netflix.spinnaker.clouddriver.cache.OnDemandType
import io.armory.plugin.nomad.NomadCredentials

class JobCachingAgent(objectMapper: ObjectMapper, account: NomadCredentials) :
        AbstractNomadCachingAgent<JobListStub>(objectMapper, account) {

    override val onDemandType = OnDemandType("Job")
    override val namespace = Namespace.JOBS

    override fun getItems(): List<JobListStub> {
        val response = client.jobsApi.list()
        return response.value
    }

    override fun getItemKey(item: JobListStub): String {
        return Keys.getJobKey(account.name, item.jobSummary)
    }

}
