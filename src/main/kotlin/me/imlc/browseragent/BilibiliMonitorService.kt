package me.imlc.browseragent

import com.google.common.util.concurrent.AbstractScheduledService
import me.imlc.browseragent.logger.Logger
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class BilibiliMonitorService(
        private val urls: List<String>,
        private val browser: Browser,
        private val es: ElasticSearch
): AbstractScheduledService() {

    private val logger = Logger(BilibiliMonitorService::class.java)

    override fun scheduler(): Scheduler {
        return Scheduler.newFixedDelaySchedule(0, 1, TimeUnit.HOURS)
    }

    override fun runOneIteration() {
        urls.forEach {
            val bilibiliMetric = browser.readBilibiliMetricsFrom(it)
            logger.info("$it: $bilibiliMetric")
            es.save(BilibiliEsData(
                title = bilibiliMetric.title,
                url = bilibiliMetric.url,
                like = bilibiliMetric.like,
                coin = bilibiliMetric.coin,
                collect = bilibiliMetric.collect,
                share = bilibiliMetric.share,
                view = bilibiliMetric.view,
                danmu = bilibiliMetric.danmu,
                timestamp = System.currentTimeMillis()
            ))
        }

        browser.closeTabs()
    }

    override fun executor(): ScheduledExecutorService {
        return Executors.newSingleThreadScheduledExecutor()
    }
}