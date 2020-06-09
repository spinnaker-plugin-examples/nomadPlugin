package io.armory.plugin.nomad.validators

import com.netflix.spinnaker.clouddriver.deploy.DescriptionValidator
import com.netflix.spinnaker.clouddriver.orchestration.AtomicOperations
import io.armory.plugin.nomad.NomadOperation
import io.armory.plugin.nomad.descriptions.UpsertNomadJobDescription
import org.springframework.validation.Errors

@NomadOperation(AtomicOperations.RUN_JOB)
class UpsertNomadJobDescriptionValidator : DescriptionValidator<UpsertNomadJobDescription>(){
    override fun validate(priorDescriptions: MutableList<Any?>?, description: UpsertNomadJobDescription, errors: Errors?) {
        if (description.job?.id == null) {
            errors!!.rejectValue("job.id", "not.nullable")
        }
    }
}

