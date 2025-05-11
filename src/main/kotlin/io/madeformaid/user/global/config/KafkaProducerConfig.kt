package io.madeformaid.user.global.config

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.ByteArraySerializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory

//@Configuration
class KafkaProducerConfig {
//    @Bean
//    fun producerFactory(): ProducerFactory<String, ByteArray> {
//        val props = mapOf(
//            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to "host.docker.internal:9092,host.docker.internal:9093,host.docker.internal:9094",
//            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
//            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to ByteArraySerializer::class.java
//        )
//
//        return DefaultKafkaProducerFactory(props)
//    }
//
//    @Bean
//    fun kafkaTemplate(): KafkaTemplate<String, ByteArray> {
//        return KafkaTemplate(producerFactory())
//    }
}
