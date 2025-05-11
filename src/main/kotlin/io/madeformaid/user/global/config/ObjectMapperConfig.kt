package io.madeformaid.user.global.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ObjectMapperConfig {
    @Bean
    fun objectMapper(): ObjectMapper {
        return ObjectMapper()
            .registerModule(KotlinModule.Builder().build()) // Kotlin 모듈 추가
            .registerModule(JavaTimeModule()) // 시간 직렬화 모듈 추가
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) // ISO-8601 형식 사용
    }
}
