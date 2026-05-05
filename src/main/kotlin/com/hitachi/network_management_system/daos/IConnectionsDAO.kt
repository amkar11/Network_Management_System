package com.hitachi.network_management_system.daos

import com.hitachi.network_management_system.dto.ConnectionDTO
import com.hitachi.network_management_system.topology_db.ConnectionDB

interface IConnectionsDAO {
    suspend fun getAllConnections(): List<ConnectionDB>
    suspend fun createConnections(connections: List<ConnectionDTO>)
}