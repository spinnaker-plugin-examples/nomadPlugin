package io.armory.plugin.nomad.cache

enum class Namespace(val ns: String) {
    ALLOCATIONS("allocations"),
    DEPLOYMENTS("deployments"),
    JOBS("jobs");

    override fun toString(): String {
        return ns
    }

}