package io.armory.plugin.nomad.cache

import com.hashicorp.nomad.apimodel.AllocationListStub
import com.hashicorp.nomad.apimodel.Job
import com.hashicorp.nomad.apimodel.JobSummary
import com.netflix.spinnaker.clouddriver.cache.KeyParser
import io.armory.plugin.nomad.NomadCloudProvider

class Keys : KeyParser {

    companion object Factory {

        fun getJobKey(account: String, namespace: String, jobId: String): String {
            return "${NomadCloudProvider.ID}:${Namespace.JOBS}:${account}:${namespace}:${jobId}"
        }

        fun getJobKey(account: String, jobSummary: JobSummary): String {
            return getJobKey(account, jobSummary.namespace, jobSummary.jobId)
        }

        fun getDeploymentKey(account: String, namespace: String, jobId: String, jobVersion: String): String {
            return "${NomadCloudProvider.ID}:${Namespace.DEPLOYMENTS}:${account}:${namespace}:${jobId}:${jobVersion}"
        }

        fun getDeploymentKey(account: String, job: Job): String {
            return getDeploymentKey(
                    account,
                    job.namespace,
                    job.id,
                    job.version.toString())
        }

        fun getAllocationKey(account: String, namespace: String, jobId: String, jobVersion: String, allocationId: String): String {
            return "${NomadCloudProvider.ID}:${Namespace.ALLOCATIONS}:${account}:${namespace}:${jobId}:${jobVersion}:${allocationId}"
        }

        fun getAllocationKey(account: String, allocation: AllocationListStub): String {
            return getAllocationKey(account, allocation.namespace, allocation.jobId, allocation.jobVersion.toString(), allocation.id)
        }

    }

    // This is intentionally 'aws'. Refer to todos in clouddriver-web SearchController#search for why.
    override fun getCloudProvider() = "aws"


    override fun canParseType(type: String?): Boolean {
        return Namespace.values().any{ it.ns == type }
    }

    override fun canParseField(field: String?) = false

    override fun parseKey(key: String?): Map<String, String> {
        val parts = key!!.split(":")
        if (parts.size < 2 || parts[0] != NomadCloudProvider.ID) {
            return emptyMap()
        }
        val type = parts[1]
        val result = mutableMapOf("provider" to NomadCloudProvider.ID, "type" to type)

        return when(type) {
            Namespace.JOBS.ns -> {
                result.also {
                    it["account"] = parts[2]
                    it["namespace"] = parts[3]
                    it["job"] = parts[4]
                }
            }
            Namespace.DEPLOYMENTS.ns -> {
                result.also {
                    it["account"] = parts[2]
                    it["namespace"] = parts[3]
                    it["job"] = parts[4]
                    it["version"] = parts[5]
                }
            }
            Namespace.ALLOCATIONS.ns -> {
                result.also {
                    it["account"] = parts[2]
                    it["namespace"] = parts[3]
                    it["job"] = parts[4]
                    it["version"] = parts[5]
                    it["id"] = parts[6]
                }
            }
            else -> emptyMap()
        }
    }

}