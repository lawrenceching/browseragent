package me.imlc.browseragent

import okhttp3.OkHttpClient

class OkHttpClientProvider {
    companion object {
        fun getOkHttpClient(): OkHttpClient {
            val builder = OkHttpClient.Builder()
            if(LogConfig.isOkHttpClientHttpLoggingEnabled) builder.addHttpLoggingInterceptor()
            return builder.build()
        }
    }
}
