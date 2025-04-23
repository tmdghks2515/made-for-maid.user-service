package io.madeformaid.user

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import io.madeformaid.shared.config.AuthProperties
import io.madeformaid.webmvc.context.EnableAuthContext
import io.madeformaid.webmvc.exception.EnableGlobalExceptionHandling
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@EnableAuthContext
@EnableGlobalExceptionHandling
@EnableConfigurationProperties(AuthProperties::class)
@SpringBootApplication
class UserServiceApplication

fun main(args: Array<String>) {
	runApplication<UserServiceApplication>(*args)
}
