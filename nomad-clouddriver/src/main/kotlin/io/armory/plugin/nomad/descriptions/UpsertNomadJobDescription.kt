package io.armory.plugin.nomad.descriptions

import com.hashicorp.nomad.apimodel.Job

data class UpsertNomadJobDescription(
        var job: Job? = null,
        var credentials: String? = null) {
}
