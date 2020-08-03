package io.armory.plugin.nomad.cache

import com.hashicorp.nomad.apimodel.Deployment
import com.hashicorp.nomad.apimodel.Job

data class JobDeployment(
        var account: String,
        var job: Job,
        var deployments: List<Deployment>
        )