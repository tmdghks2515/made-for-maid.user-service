package io.madeformaid.user

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import io.madeformaid.shared.config.AuthProperties
import io.madeformaid.shared.context.EnableAuthContext
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@EnableAuthContext
@SpringBootApplication
@EnableConfigurationProperties(AuthProperties::class)
class UserServiceApplication

fun main(args: Array<String>) {
	runApplication<UserServiceApplication>(*args)
}
