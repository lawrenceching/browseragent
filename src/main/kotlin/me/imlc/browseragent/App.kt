package me.imlc.browseragent

import com.google.common.collect.Lists
import com.google.common.util.concurrent.ServiceManager
import me.imlc.browseragent.logger.Logger
import java.util.*

class App(private val config: Config) {
    private val serviceManager: ServiceManager
    private val bilibiliMonitorService: BilibiliMonitorService
    private val logger = Logger(App::class.java)
    private val browser = Browser()
    private val es: ElasticSearch = ElasticSearch(
            config.esHost,
            config.esIndex
    )

    fun start() {
        es.connect()
        serviceManager.startAsync()
        serviceManager.awaitHealthy()
    }

    fun stop() {
        serviceManager.stopAsync()
        serviceManager.awaitStopped()
        browser.quit()
        logger.info("Closed browser")
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val logger = Logger("Main")
            val esHost = System.getProperty("elasticsearch.host")
            val esIndex = System.getProperty("elasticsearch.index")
            Objects.nonNull(esHost)
            Objects.nonNull(esIndex)
            val config = Config(
                    true,
                    esHost,
                    esIndex
            )
            logger.info("Start up application with config: $config")
            val app = App(config)
            app.start()
            Runtime.getRuntime().addShutdownHook(object : Thread() {
                override fun run() {
                    app.stop()
                }
            })
        }
    }

    init {
        bilibiliMonitorService = BilibiliMonitorService(
                Lists.newArrayList(
                        "https://www.bilibili.com/video/BV1WE411q7TA",
                        "https://www.bilibili.com/video/BV1Xt411h7Zy",
                        "https://www.bilibili.com/video/BV1oA411e7wE",
                        "https://www.bilibili.com/video/BV1zJ411U7UM",
                        "https://www.bilibili.com/video/BV13b411u7Z1"
                ),
                browser,
                es
        )
        serviceManager = ServiceManager(
                Lists.newArrayList(
                        bilibiliMonitorService
                )
        )
    }
}