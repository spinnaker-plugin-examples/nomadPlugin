package io.armory.plugin.nomad

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class NomadOperation(val value: String)

object NomadOperations {
    const val UpsertJob = "upsertJob"
}
