![CI](https://github.com/spinnaker-plugin-examples/nomadPlugin/workflows/CI/badge.svg)
![Latest Kork](https://github.com/spinnaker-plugin-examples/nomadPlugin/workflows/Latest%20Kork/badge.svg?branch=master)
![Latest Clouddriver](https://github.com/spinnaker-plugin-examples/nomadPlugin/workflows/Latest%20Clouddriver/badge.svg?branch=master)

This plugin add a Nomad cloud provider.

<h2>Usage</h2>

1) Run `./gradlew releaseBundle`
2) Put the `/build/distributions/<project>-<version>.zip` into the [configured plugins location for your service](https://pf4j.org/doc/packaging.html).
3) Configure the Spinnaker service. Put the following in the service yml to enable the plugin and configure the extension.
```
spinnaker:
  extensibility:
    plugins:
      Armory.NomadPlugin:
        enabled: true

nomad:
  accounts:
    - name: account1
      environment: dev
      address: http://127.0.0.1:4646
```

Or use the [examplePluginRepository](https://github.com/spinnaker-plugin-examples/examplePluginRepository) to avoid copying the plugin `.zip` artifact.

To debug the plugin inside a Spinnaker service (like Orca) using IntelliJ Idea follow these steps:

1) Run `./gradlew releaseBundle` in the plugin project.
2) Copy the generated `.plugin-ref` file under `build` in the plugin project submodule for the service to the `plugins` directory under root in the Spinnaker service that will use the plugin .
3) Link the plugin project to the service project in IntelliJ (from the service project use the `+` button in the Gradle tab and select the plugin build.gradle).
4) Configure the Spinnaker service the same way specified above.
5) Create a new IntelliJ run configuration for the service that has the VM option `-Dpf4j.mode=development` and does a `Build Project` before launch.
6) Debug away...

<h2>Features</h2>

Currently this plugin allows reading Nomad jobs with a Clouddriver caching agent, modifying Nomad jobs with a Clouddriver atomic operation, and searching Nomad jobs with a Clouddriver searchable provider.

<h3>View Jobs</h3>
GET `(clouddriver url)/cache/nomad/job`
returns something like this:

```json
[{
	"account": "account1",
	"details": {
		"CreateIndex": 11,
		"Datacenters": [
			"dc1"
		],
		"ID": "example",
		"JobModifyIndex": 845,
		"JobSummary": {
			"Children": {
				"Dead": 0,
				"Pending": 0,
				"Running": 0
			},
			"CreateIndex": 11,
			"JobID": "example",
			"ModifyIndex": 849,
			"Namespace": "default",
			"Summary": {
				"cache": {
					"Complete": 1,
					"Failed": 0,
					"Lost": 0,
					"Queued": 0,
					"Running": 3,
					"Starting": 0
				}
			}
		},
		"ModifyIndex": 845,
		"Name": "example2",
		"ParameterizedJob": false,
		"ParentID": "",
		"Periodic": false,
		"Priority": 50,
		"Status": "running",
		"StatusDescription": "",
		"Stop": false,
		"SubmitTime": 1591733805353030000,
		"Type": "service"
	}
}]
```
<h3>Upsert Job</h3>
POST the following to `(clouddriver url)/nomad/ops/upsertJob`

```json
{
	"credentials": "account1",
	"job": {
		"ID": "example",
		"Name": "example",
		"Type": "service",
		"Priority": 50,
		"Datacenters": ["dc1"],
		"TaskGroups": [{
			"Name": "cache",
			"Count": 3,
			"Tasks": [{
				"Name": "redis",
				"Driver": "docker",
				"Config": {
					"image": "redis:3.2",
					"port_map": {
						"db": 6379
					}
				},
				"Services": [{
					"Name": "redis-cache",
					"Tags": ["global", "cache"],
					"PortLabel": "db",
					"Checks": [{
						"Name": "alive",
						"Type": "tcp",
						"Interval": 10000000000,
						"Timeout": 2000000000
					}]
				}],
				"Resources": {
					"CPU": 500,
					"MemoryMB": 256,
					"Networks": [{
						"MBits": 10,
						"DynamicPorts": [{
							"Label": "db",
							"Value": 0
						}]
					}]
				}
			}],
			"RestartPolicy": {
				"Attempts": 2,
				"Interval": 300000000000,
				"Delay": 25000000000,
				"Mode": "fail"
			},
			"EphemeralDisk": {
				"SizeMB": 300
			}
		}],
		"Update": {
			"MaxParallel": 1,
			"MinHealthyTime": 10000000000,
			"HealthyDeadline": 180000000000,
			"ProgressDeadline": 200000000000,
			"AutoRevert": false,
			"Canary": 0
		}
	}
}
```

<h3>Search Jobs</h3>
GET `(clouddriver url)/search?type=jobs&platform=aws&q=example`
returns something like this (and yes that is weird that you have to specify `platform=aws` right now):

```json
[{
	"pageNumber": 1,
	"pageSize": 10000,
	"platform": "aws",
	"query": "example",
	"results": [{
		"account": "account1",
		"id": "example2",
		"provider": "nomad",
		"type": "jobs"
	}],
	"totalMatches": 1
}]
```
