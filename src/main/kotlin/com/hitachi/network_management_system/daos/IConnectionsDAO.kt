package com.hitachi.network_management_system.daos

import com.hitachi.network_management_system.dto.ConnectionDTO
import com.hitachi.network_management_system.topology_db.ConnectionDB

interface IConnectionsDAO {
    fun getReachableConnections(id: Int): List<ConnectionDB>
    fun getAllConnectionsByDeviceId(fromNode: Int): List<ConnectionDB>
    fun createConnections(connections: List<ConnectionDTO>)
}