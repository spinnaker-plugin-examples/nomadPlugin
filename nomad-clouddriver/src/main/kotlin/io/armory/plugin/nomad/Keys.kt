package io.armory.plugin.nomad

import com.netflix.spinnaker.clouddriver.cache.KeyParser

class Keys : KeyParser {

    companion object Factory {
        fun getJobKey(account: String, id: String): String {
            return "${NomadCloudProvider.ID}:${Namespace.JOBS}:${account}:${id}"
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
        val result = mutableMapOf("provider" to  NomadCloudProvider.ID, "type" to type)

        return when(type) {
            Namespace.JOBS.ns -> {
                result.also {
                    it["account"] = parts[2]
                    it["id"] = parts[3]
                }
            }
            else -> emptyMap()
        }
    }

}