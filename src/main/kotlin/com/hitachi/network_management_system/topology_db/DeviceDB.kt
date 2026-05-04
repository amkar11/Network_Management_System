package com.hitachi.network_management_system.topology_db

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table


@Table(value = "devices")
data class DeviceDB(
    @Column("id")
    @Id val id: Int? = null,

    @Column("name")
    val name: String,

    @Column("active")
    val active: Boolean)