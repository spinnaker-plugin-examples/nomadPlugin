package io.armory.plugin.nomad

import com.hashicorp.nomad.javasdk.NomadApiConfiguration
import com.netflix.spinnaker.clouddriver.security.AccountCredentialsRepository
import javax.annotation.PostConstruct

class NomadCredentialsInitializer(
        val accountCredentialsRepository: AccountCredentialsRepository,
        val properties: NomadProperties) {

    fun loadCredentials() {
        val nomadCredentials = properties.accounts.map {
            val config = NomadApiConfiguration.Builder()
                    .setAddress(it.address!!)
            it.region?.let { config.setRegion(it) }
            it.namespace?.let { config.setNamespace(it) }
            it.authToken?.let { config.setAuthToken(it) }
            it.tls?.let {
                config.apply {
                    setTlsCaFile(it.caCertificateFile)
                    setTlsSkipVerify(it.skipVerify)
                    setTlsCertAndKeyFiles(it.clientCertificateFile, it.clientKeyFile)
                }
            }
            NomadCredentials(
                    it.name!!,
                    it.environment!!,
                    config.build()
            )
        }
        nomadCredentials.forEach {
            accountCredentialsRepository.save(it.name, it)
        }
    }

}
