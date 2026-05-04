package com.hitachi.network_management_system.daos

import com.hitachi.network_management_system.dto.ConnectionDTO
import com.hitachi.network_management_system.topology_db.ConnectionDB
import com.hitachi.network_management_system.topology_db.DeviceDB

interface IConnectionsDAO {
    suspend fun getReachableConnections(id: Int): List<DeviceDB>
    suspend fun getAllConnections(): List<ConnectionDB>
    suspend fun createConnections(connections: List<ConnectionDTO>)
    suspend fun getDevicesIdList(id: Int): List<Int>
}