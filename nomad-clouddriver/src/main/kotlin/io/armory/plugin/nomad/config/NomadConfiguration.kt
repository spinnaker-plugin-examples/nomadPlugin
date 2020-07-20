package io.armory.plugin.nomad.config

import io.armory.plugin.nomad.NomadProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(NomadProperties::class)
class NomadConfiguration {

}
