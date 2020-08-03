package io.armory.plugin.nomad.model

import com.hashicorp.nomad.apimodel.Task
import com.netflix.spinnaker.clouddriver.model.ServerGroup

class NomadImageSummary(
        private val serverGroupName: String,
        private val task: Task
) : ServerGroup.ImageSummary {

    val source = when(task.driver) {
        "docker" -> task.config["image"] as String
        "java" -> task.config["jar_path"] as String
        else -> ""
    }

    override fun getBuildInfo(): Map<String, Any> {
        return emptyMap()
    }

    override fun getImageName(): String {
        return source
    }

    override fun getImage(): Map<String, Any> {
        return mapOf(
                "driver" to task.driver,
                "artifacts" to task.artifacts,
                "config" to task.config
        )
    }

    override fun getImageId(): String {
        return source
    }

    override fun getServerGroupName(): String {
        return serverGroupName
    }

}
