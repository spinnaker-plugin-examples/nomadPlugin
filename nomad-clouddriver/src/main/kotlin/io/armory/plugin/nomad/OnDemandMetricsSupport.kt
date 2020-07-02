package io.armory.plugin.nomad

import com.netflix.spectator.api.Counter
import com.netflix.spectator.api.Registry
import com.netflix.spectator.api.Timer
import com.netflix.spinnaker.clouddriver.cache.OnDemandAgent
import com.netflix.spinnaker.clouddriver.cache.OnDemandMetricsSupportable
import java.util.concurrent.TimeUnit
import java.util.function.Supplier

class OnDemandMetricsSupport(registry: Registry, agent: OnDemandAgent, onDemandType: String) : OnDemandMetricsSupportable {
    private val onDemandTotal: Timer
    private val dataRead: Timer
    private val dataTransform: Timer
    private val onDemandStore: Timer
    private val cacheWrite: Timer
    private val cacheEvict: Timer
    private val onDemandErrors: Counter
    private val onDemandCount: Counter
    private fun <T> record(timer: Timer, closure: Supplier<T>): T {
        val start = System.nanoTime()
        return try {
            closure.get()
        } finally {
            val elapsed = System.nanoTime() - start
            timer.record(elapsed, TimeUnit.NANOSECONDS)
        }
    }

    override fun <T> readData(closure: Supplier<T>): T {
        return record(dataRead, closure)
    }

    override fun <T> transformData(closure: Supplier<T>): T {
        return record(dataTransform, closure)
    }

    override fun <T> onDemandStore(closure: Supplier<T>): T {
        return record(onDemandStore, closure)
    }

    override fun <T> cacheWrite(closure: Supplier<T>): T {
        return record(cacheWrite, closure)
    }

    override fun <T> cacheEvict(closure: Supplier<T>): T {
        return record(cacheEvict, closure)
    }

    override fun countError() {
        onDemandErrors.increment()
    }

    override fun countOnDemand() {
        onDemandCount.increment()
    }

    override fun recordTotalRunTimeNanos(nanos: Long) {
        onDemandTotal.record(nanos, TimeUnit.NANOSECONDS)
    }

    init {
        val tags = arrayOf(
                "providerName",
                agent.providerName,
                "agentType",
                agent.onDemandAgentType,
                "onDemandType",
                onDemandType
        )
        onDemandTotal = registry.timer(OnDemandMetricsSupportable.ON_DEMAND_TOTAL_TIME, *tags)
        dataRead = registry.timer(OnDemandMetricsSupportable.DATA_READ, *tags)
        dataTransform = registry.timer(OnDemandMetricsSupportable.DATA_TRANSFORM, *tags)
        onDemandStore = registry.timer(OnDemandMetricsSupportable.ON_DEMAND_STORE, *tags)
        cacheWrite = registry.timer(OnDemandMetricsSupportable.CACHE_WRITE, *tags)
        cacheEvict = registry.timer(OnDemandMetricsSupportable.CACHE_EVICT, *tags)
        onDemandErrors = registry.counter(OnDemandMetricsSupportable.ON_DEMAND_ERROR, *tags)
        onDemandCount = registry.counter(OnDemandMetricsSupportable.ON_DEMAND_COUNT, *tags)
    }
}
