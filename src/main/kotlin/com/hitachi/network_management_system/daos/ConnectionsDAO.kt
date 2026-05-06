package com.hitachi.network_management_system.daos

import com.hitachi.network_management_system.dto.ConnectionDTO
import com.hitachi.network_management_system.repositories.IConnectionsRepository
import com.hitachi.network_management_system.topology_db.ConnectionDB
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service

@Service
class ConnectionsDAO(
    val connectionsRepository: IConnectionsRepository
)  : IConnectionsDAO {


    override suspend fun getAllConnections(): List<ConnectionDB> {
        val connections = connectionsRepository.findAll().toList()
        if (connections.isEmpty()) {
            throw IllegalStateException("There must be connections at the database")
        }
        return connections
    }

    override suspend fun createConnections(connections: List<ConnectionDTO>) {
        val connectionsDB = connections.map { ConnectionDB(id = null, fromNode = it.from, toNode = it.to) }
        connectionsRepository.saveAll(connectionsDB).collect()
    }
}