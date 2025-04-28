package io.madeformaid.user.grpc.config

import affiliation.ShopServiceGrpc
import io.grpc.ManagedChannel
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GrpcClientConfig(
    private val affiliationServiceChannel: ManagedChannel
) {
    @Bean
    fun shopServiceStub(): ShopServiceGrpc.ShopServiceBlockingStub {
        return ShopServiceGrpc.newBlockingStub(affiliationServiceChannel)
    }
}
