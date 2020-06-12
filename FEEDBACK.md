# rz feedback

I feel like doing a PR as feedback might be the best way to do this...

## structure

The structure of cloud providers has always bothered me.
A lot of this is non-functional, but I think we could use more (any) guidance on layout.
My own preference, I'd like to see things organized by resource, rather than class type.

```
io.armory.plugin.nomad
    resources
        job
            UpsertNomadJobAtomicOperation
            UpsertNomadJobAtomicOperationConverter
            UpsertNomadJobDescription
            UpsertNomadJobValidator
            JobCachingAgent
        loadbalancer
        securitygroup
        ...
```
