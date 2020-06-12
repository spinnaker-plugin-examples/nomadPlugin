package io.armory.plugin.nomad.descriptions

import com.hashicorp.nomad.apimodel.Job

data class UpsertNomadJobDescription(
  /**
   * I'm seeing _a lot_ of nullable types in this codebase where they shouldn't be.
   */
  var job: Job? = null,
  var credentials: String? = null) {
}
