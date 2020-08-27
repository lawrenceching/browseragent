package me.imlc.browseragent.logger

import me.imlc.browseragent.LogConfig
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Logger {

    constructor(name: String) {
        this.logger = LoggerFactory.getLogger(name)
    }

    constructor(cls: Class<*>) {
        this.logger = LoggerFactory.getLogger(cls)
    }

    private val logger: Logger

    fun info(msg: String?) {
        logger.info(msg)
    }

    fun debug(msg: String?) {
        if(LogConfig.isDebugEnabled) logger.info(msg)
    }

    fun error(msg: String?, t: Throwable) {
        logger.error(msg, t)
    }

    fun error(msg: String?) {
        logger.error(msg)
    }

    fun isDebugEnabled(): Boolean {
        return LogConfig.isDebugEnabled
    }
}

