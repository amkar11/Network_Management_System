package com.hitachi.network_management_system.daos

import com.hitachi.network_management_system.dto.ConnectionDTO
import com.hitachi.network_management_system.repositories.IConnectionsRepository
import com.hitachi.network_management_system.repositories.IDevicesRepository
import com.hitachi.network_management_system.topology_db.ConnectionDB
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Component

@Component
class ConnectionsDAO(
    private val devicesRepository: IDevicesRepository,
    private val connectionsRepository: IConnectionsRepository
) : IConnectionsDAO {

    override suspend fun getReachableConnections(id: Int): List<ConnectionDB> {
        val allConnections = getAllConnectionsByDeviceId(id)
        val reachableConnections: MutableList<ConnectionDB> = mutableListOf()
        for (connection in allConnections) {
            val device = devicesRepository.findById(connection.toNode) ?:
                throw NoSuchElementException("No device found with id $id")
            if (!device.active) break
            reachableConnections.add(connection)
        }
        return reachableConnections
    }

    override suspend fun getAllConnectionsByDeviceId(fromNode: Int): List<ConnectionDB> {
        devicesRepository.findById(fromNode)
            ?: throw NoSuchElementException("No device found with id $fromNode")
        val connections = connectionsRepository.findAllByFromNode(fromNode).toList()
        if (connections.isEmpty()) throw NoSuchElementException("No connections found with device id $fromNode")
        return connections
    }

    override suspend fun createConnections(connections: List<ConnectionDTO>) {
        val connectionsDB = connections.map {ConnectionDB(id = null, fromNode = it.from, toNode = it.to)}
        connectionsRepository.saveAll(connectionsDB).collect()
    }
}