package io.armory.plugin.nomad

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("nomad")
data class NomadProperties(
        var accounts: List<NomadAccount>
)

data class NomadAccount(
        var name: String? = null,
        var environment: String? = null,
        var address: String? = null,
        var region: String? = null,
        var namespace: String? = null,
        var authToken: String? = null,
        var tls: Tls? = null
)

data class Tls(
        var caCertificateFile: String,
        var skipVerify: Boolean,
        var clientCertificateFile: String,
        var clientKeyFile: String
)
