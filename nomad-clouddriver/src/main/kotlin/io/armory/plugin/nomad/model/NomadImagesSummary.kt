package io.armory.plugin.nomad.model

import com.netflix.spinnaker.clouddriver.model.ServerGroup

class NomadImagesSummary(
        private val imageSummaries: List<NomadImageSummary>
) : ServerGroup.ImagesSummary {

    override fun getSummaries(): List<ServerGroup.ImageSummary> {
        return imageSummaries
    }

}
