package io.armory.plugin.nomad

enum class Namespace(val ns: String) {
    JOBS("jobs");

    override fun toString(): String {
        return ns
    }

}