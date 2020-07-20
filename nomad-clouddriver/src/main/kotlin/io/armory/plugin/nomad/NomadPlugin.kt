package io.armory.plugin.nomad

import com.netflix.spinnaker.kork.plugins.api.spring.SpringLoaderPlugin
import org.slf4j.LoggerFactory
import org.pf4j.PluginWrapper

class NomadPlugin(wrapper: PluginWrapper) : SpringLoaderPlugin(wrapper) {

    private val logger = LoggerFactory.getLogger(NomadPlugin::class.java)

    override fun getPackagesToScan(): List<String> {
        return listOf(
                "io.armory.plugin.nomad"
        )
    }

    override fun start() {
        logger.info("NomadPlugin.start()")
    }

    override fun stop() {
        logger.info("NomadPlugin.stop()")
    }
}
