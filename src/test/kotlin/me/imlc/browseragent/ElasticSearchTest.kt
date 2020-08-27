package me.imlc.browseragent

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ElasticSearchTest {


    @Test
    internal fun test() {
        val index = "bilibili-test-${System.currentTimeMillis()}"
        val es = ElasticSearch(
                "http://nas.imlc.tech:9200",
                        index
        )

        es.connect()

        es.save(BilibiliEsData(
                "Hello, Bilibili!",
                "http://example.com",
                System.currentTimeMillis(),
                1,2,3,4
        ))
    }
}