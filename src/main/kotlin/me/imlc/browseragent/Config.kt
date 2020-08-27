package me.imlc.browseragent

data class Config(
        val isDebugEnabled: Boolean = true,
        val esHost: String,
        val esIndex: String
)

class LogConfig {
    companion object {
        var isDebugEnabled: Boolean = true
        var isOkHttpClientHttpLoggingEnabled: Boolean = false
    }
}
