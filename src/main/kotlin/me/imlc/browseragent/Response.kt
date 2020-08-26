package me.imlc.browseragent

import com.google.gson.Gson

data class Response(
        val message: String,
        val body: Any? = null
) {
    fun toJson(): String {
        return Gson().toJson(this)
    }
}
