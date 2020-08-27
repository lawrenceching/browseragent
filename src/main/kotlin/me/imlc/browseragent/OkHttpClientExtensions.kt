package me.imlc.browseragent

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

fun OkHttpClient.Builder.addHttpLoggingInterceptor(): OkHttpClient.Builder {
    val interceptor = HttpLoggingInterceptor()
    interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC)
    this.addInterceptor(interceptor)
    return this
}

