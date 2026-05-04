package com.hitachi.network_management_system.topology_db

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table(value = "connections")
data class ConnectionDB(
    @Column("id")
    @Id val id: Int? = null,

    @Column("from_node")
    val fromNode: Int,

    @Column("to_node")
    val toNode: Int)