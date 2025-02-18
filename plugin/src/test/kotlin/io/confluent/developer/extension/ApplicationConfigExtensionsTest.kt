package io.confluent.developer.extension

import com.typesafe.config.ConfigFactory
import io.ktor.server.config.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class ApplicationConfigExtensionsTest {

    @Test
    @DisplayName("should extract config value based on path - config from file")
    fun configMapTestBlah() {
        val config = ApplicationConfig("kafka-config-map.conf")
        val fixture = "org.apache.kafka.common.serialization.LongSerializer"
        //val map = configMap(config, "ktor.kafka.producer")
        val map = config.toMap("ktor.kafka.producer")

        assertThat(map["key.serializer"]).isEqualTo(fixture)
    }
    @Test
    @DisplayName("should extract configs value based on path - inline config")
    fun testToMapWithPath() {
        val string = """
            ktor {
              kafka {
                producer {
                  value.serializer = KafkaJsonSchemaSerializer
                  key {
                    serializer =  LongSerializer
                  }
                }
              }
            }
        """.trimIndent()

        val config = HoconApplicationConfig(ConfigFactory.parseString(string))
        val path = "ktor.kafka.producer"
        val map = config.toMap(path)
        //var map = configMap(config, path)

        assertEquals("LongSerializer", map["key.serializer"])
        assertEquals("KafkaJsonSchemaSerializer", map["value.serializer"])
    }

}
