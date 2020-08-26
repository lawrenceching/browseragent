package me.imlc.browseragent

import com.google.common.util.concurrent.AbstractScheduledService
import me.imlc.browseragent.logger.Logger
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class BilibiliMonitorService(val urls: List<String>,
        val browser: Browser): AbstractScheduledService() {

    private val logger = Logger(BilibiliMonitorService::class.java)

    override fun scheduler(): Scheduler {
        return Scheduler.newFixedDelaySchedule(0, 1, TimeUnit.HOURS)
    }

    override fun runOneIteration() {
        urls.forEach {
            val bilibiliMetric = browser.readBilibiliMetricsFrom(it)
            logger.info("$it: $bilibiliMetric")
        }
    }

    override fun executor(): ScheduledExecutorService {
        return Executors.newSingleThreadScheduledExecutor()
    }
}