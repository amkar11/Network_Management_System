package com.hitachi.network_management_system

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@OpenAPIDefinition
class NetworkManagementSystemApplication

fun main(args: Array<String>) {
    runApplication<NetworkManagementSystemApplication>(*args)
}
