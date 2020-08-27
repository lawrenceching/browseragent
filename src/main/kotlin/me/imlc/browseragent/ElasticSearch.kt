package me.imlc.browseragent

import me.imlc.browseragent.logger.Logger
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.RequestBody
import java.lang.RuntimeException

class ElasticSearch(
        val esHost: String,
        val esIndex: String
) {

    private val httpClient = OkHttpClientProvider.getOkHttpClient()
    private val logger = Logger(ElasticSearch::class.java)
    private var connected = false

    fun connect() {
        if(!isIndexExist()) {
            logger.info("Index $esIndex does not exist")
            logger.info("Going to create index $esIndex")
            createIndex()
        }

        connected = true
    }

    private fun isIndexExist(): Boolean {
        val req = Request.Builder()
                .method("HEAD", null)
                .url("$esHost/$esIndex")
                .build()

        val resp = httpClient.newCall(req).execute()

        return when (val code = resp.code()) {
            200 -> true
            404 -> false
            else -> {
                throw RuntimeException("Unable to check if index exist because of $code response")
            }
        }
    }

    private fun createIndex() {
        // language=JSON
        val body = """
{
  "mappings": {
      "properties": {
        "@timestamp": {
          "type": "date"
        },
        "like": {
          "type": "long"
        },
        "coin": {
          "type": "long"
        },
        "collect": {
          "type": "long"
        },
        "share": {
          "type": "long"
        },
        "title": {
          "type": "text",
          "fields": {
            "keyword": {
              "type": "keyword",
              "ignore_above": 256
            }
          }
        },
        "url": {
          "type": "text",
          "fields": {
            "keyword": {
              "type": "keyword",
              "ignore_above": 256
            }
          }
        }
      }
    }
}
        """.trimIndent()

        if(logger.isDebugEnabled()) {
            logger.debug(body)
        }

        val req = Request.Builder()
                .method("PUT", RequestBody.create(MediaType.parse("application/json"), body))
                .url("$esHost/$esIndex")
                .build()

        val resp = httpClient.newCall(req).execute()

        if(!resp.isSuccessful) {
            throw RuntimeException("Unable to create index because of ${resp.code()} response: ${resp.body()?.string()}")
        }

        if(logger.isDebugEnabled()) {
            logger.debug(resp.body()?.string())
        }

        logger.info("Index $esIndex has been created")
        checkIndex()
    }

    private fun checkIndex() {
        val req = Request.Builder()
                .url("$esHost/$esIndex?pretty")
                .build()

        val resp = httpClient.newCall(req).execute()

        if(resp.isSuccessful) {
            logger.info(resp.body()?.string())
        }
    }


    fun save(data: BilibiliEsData) {

        if(!connected) {
            throw IllegalStateException("ElasticSearch is not connected, make sure you call connect() before you use it")
        }

        val reqBody = data.toJson()
        logger.info("Saving data: $reqBody")
        val req = Request.Builder()
                .method("POST", RequestBody.create(
                        MediaType.parse("application/json"),
                        reqBody
                ))
                .url("$esHost/$esIndex/_doc?pretty")
                .build()

        val resp = httpClient.newCall(req).execute()
        val body = resp.body()?.string()

        if(resp.isSuccessful) {
            logger.info("Saved data: $body")
        } else {
            throw RuntimeException("Unable to save data ${data.title} because of ${resp.code()} response: \n$body")
        }

    }

}