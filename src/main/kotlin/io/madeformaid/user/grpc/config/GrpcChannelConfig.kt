package io.madeformaid.user.grpc.config

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GrpcChannelConfig {

    @Bean
    fun affiliationServiceChannel(): ManagedChannel {
        return ManagedChannelBuilder
            .forAddress("localhost", 8083)
            .usePlaintext() // TLS 사용 안함
            .build()
    }
}
