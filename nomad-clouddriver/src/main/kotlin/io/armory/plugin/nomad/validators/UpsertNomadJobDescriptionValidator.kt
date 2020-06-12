package io.armory.plugin.nomad.validators

import com.netflix.spinnaker.clouddriver.deploy.DescriptionValidator
import io.armory.plugin.nomad.NomadOperation
import io.armory.plugin.nomad.NomadOperations
import io.armory.plugin.nomad.descriptions.UpsertNomadJobDescription
import org.springframework.validation.Errors

@NomadOperation(NomadOperations.UpsertJob)
class UpsertNomadJobDescriptionValidator : DescriptionValidator<UpsertNomadJobDescription>(){
    /**
     * More nullable types. This is more the fault of the existing interfaces, but we should `@NullableByDefault` the
     * `clouddriver-api` package-info so it's clearer for developers that they don't need to null-check.
     */
    override fun validate(priorDescriptions: MutableList<Any?>?, description: UpsertNomadJobDescription, errors: Errors?) {
        if (description.job?.id == null) {
            errors!!.rejectValue("job.id", "not.nullable")
        }
    }
}

