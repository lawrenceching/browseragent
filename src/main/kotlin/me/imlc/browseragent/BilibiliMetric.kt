package me.imlc.browseragent

data class BilibiliMetric(
        val title: String,
        val url: String,
        val like: Int?,
        val coin: Int?,
        val collect: Int?,
        val share: Int?
)