package io.armory.plugin.nomad.validators

import com.netflix.spinnaker.clouddriver.deploy.DescriptionValidator
import com.netflix.spinnaker.clouddriver.deploy.ValidationErrors
import com.netflix.spinnaker.kork.plugins.api.spring.ExposeToApp
import io.armory.plugin.nomad.NomadOperation
import io.armory.plugin.nomad.NomadOperations
import io.armory.plugin.nomad.descriptions.UpsertNomadJobDescription
import org.springframework.stereotype.Component

@Component
@NomadOperation(NomadOperations.UpsertJob)
@ExposeToApp
class UpsertNomadJobDescriptionValidator : DescriptionValidator<UpsertNomadJobDescription>(){
    override fun validate(priorDescriptions: MutableList<Any?>?, description: UpsertNomadJobDescription, errors: ValidationErrors?) {
        if (description.job?.id == null) {
            errors!!.rejectValue("job.id", "not.nullable")
        }
    }
}
