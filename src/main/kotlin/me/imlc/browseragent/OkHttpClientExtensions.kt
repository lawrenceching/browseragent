package me.imlc.browseragent

import okhttp3.OkHttpClient

fun OkHttpClient.Builder.addHttpLoggingInterceptor(): OkHttpClient.Builder {
    return this
}

