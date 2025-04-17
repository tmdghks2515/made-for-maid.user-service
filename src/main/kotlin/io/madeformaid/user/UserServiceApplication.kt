package io.madeformaid.user

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import io.madeformaid.shared.config.AuthProperties

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties(AuthProperties::class)
class UserServiceApplication

fun main(args: Array<String>) {
	runApplication<UserServiceApplication>(*args)
}
