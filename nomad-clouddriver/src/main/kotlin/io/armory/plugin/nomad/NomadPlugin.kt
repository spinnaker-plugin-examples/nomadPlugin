package io.armory.plugin.nomad

import com.netflix.spinnaker.kork.plugins.api.spring.PrivilegedSpringPlugin
import io.armory.plugin.nomad.converters.UpsertNomadJobAtomicOperationConverter
import org.slf4j.LoggerFactory
import org.pf4j.PluginWrapper
import org.springframework.beans.factory.support.BeanDefinitionRegistry

class NomadPlugin(wrapper: PluginWrapper) : PrivilegedSpringPlugin(wrapper) {

    override fun registerBeanDefinitions(registry: BeanDefinitionRegistry) {
        listOf(
                beanDefinitionFor(NomadProperties::class.java),
                beanDefinitionFor(Keys::class.java),
                beanDefinitionFor(NomadCloudProvider::class.java),
                beanDefinitionFor(NomadAgentProvider::class.java),
                beanDefinitionFor(NomadCredentialsInitializer::class.java),
                beanDefinitionFor(UpsertNomadJobAtomicOperationConverter::class.java)
        ).forEach {
            registerBean(it, registry)
        }
    }

    private val logger = LoggerFactory.getLogger(NomadPlugin::class.java)

    override fun start() {
        logger.info("NomadPlugin.start()")
    }

    override fun stop() {
        logger.info("NomadPlugin.stop()")
    }
}
