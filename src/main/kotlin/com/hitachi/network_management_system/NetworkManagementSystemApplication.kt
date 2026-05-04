package com.hitachi.network_management_system

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories


@SpringBootApplication
@EnableR2dbcRepositories(basePackages = ["com.hitachi.network_management_system.repositories"])
@EnableR2dbcAuditing
class NetworkManagementSystemApplication

fun main(args: Array<String>) {
    runApplication<NetworkManagementSystemApplication>(*args)
}