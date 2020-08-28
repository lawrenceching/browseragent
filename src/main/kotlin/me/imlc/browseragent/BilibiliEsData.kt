package me.imlc.browseragent

import com.google.gson.Gson
import java.sql.Timestamp

data class BilibiliEsData(
        val title: String,
        val url: String,
        val timestamp: Long,
        val like: Int?,
        val coin: Int?,
        val collect: Int?,
        val share: Int?,
        val view: Int?,
        val danmu: Int?
) {
    fun toJson(): String {
        return """
{
    "title": "$title",
    "url": "$url",
    "like": $like,
    "coin": $coin,
    "collect": $collect,
    "share": $share,
    "view": $view,
    "danmu": $danmu,
    "@timestamp": $timestamp
}
        """.trimIndent()
    }
}