package me.imlc.browseragent

data class Config(
        val isDebugEnabled: Boolean = true
)

class LogConfig {
    companion object {
        var isDebugEnabled: Boolean = false
        var isOkHttpClientHttpLoggingEnabled: Boolean = false
    }
}
