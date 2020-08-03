package io.armory.plugin.nomad.cache

import com.fasterxml.jackson.databind.ObjectMapper
import com.hashicorp.nomad.apimodel.AllocationListStub
import com.hashicorp.nomad.apimodel.JobListStub
import com.netflix.spinnaker.cats.cache.Cache
import org.springframework.stereotype.Repository

@Repository
class CacheRepository(private val objectMapper: ObjectMapper, val cache: Cache) {

    fun findJobsByKey(key: String): Set<JobListStub> {
        val keys = cache.filterIdentifiers(Namespace.JOBS.ns, key)
        return cache.getAll(Namespace.JOBS.ns, keys).map {
            objectMapper.convertValue(it.attributes, JobListStub::class.java)
        }.toSet()
    }

    fun findAllocationsByKey(key: String): Set<AllocationListStub> {
        val keys = cache.filterIdentifiers(Namespace.ALLOCATIONS.ns, key)
        return cache.getAll(Namespace.ALLOCATIONS.ns, keys).map {
            objectMapper.convertValue(it.attributes, AllocationListStub::class.java)
        }.toSet()
    }

    fun findJobDeploymentsByKey(key: String): Set<JobDeployment> {
        val keys = cache.filterIdentifiers(Namespace.DEPLOYMENTS.ns, key)
        return cache.getAll(Namespace.DEPLOYMENTS.ns, keys).map {
            objectMapper.convertValue(it.attributes, JobDeployment::class.java)
        }.toSet()
    }

}
