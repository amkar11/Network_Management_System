package com.hitachi.network_management_system.repositories

import com.hitachi.network_management_system.dto.ConnectionDTO
import com.hitachi.network_management_system.topology_db.ConnectionDB
import org.springframework.stereotype.Repository

@Repository
class ConnectionsDAO(
    private val devicesRepository: IDevicesRepository,
    private val connectionsRepository: IConnectionsRepository
) : IConnectionsDAO {

    override fun getReachableConnections(id: Int): List<ConnectionDB> {
        val allConnections = getAllConnectionsByDeviceId(id)
        val reachableConnections: MutableList<ConnectionDB> = mutableListOf()
        for (connection in allConnections) {
            val device = devicesRepository.findById(id)
                .orElseThrow { NoSuchElementException("No device found with id $id") }
            if (!device.active) break
            reachableConnections.add(connection)
        }
        return reachableConnections
    }

    override fun getAllConnectionsByDeviceId(fromNode: Int): List<ConnectionDB> {
        return connectionsRepository.findAllByFromNode(fromNode)
    }

    override fun createConnections(connections: List<ConnectionDTO>) {
        val connectionsDB: MutableList<ConnectionDB> = mutableListOf()
        connections.forEach {connectionsDB.add(ConnectionDB(fromNode = it.from, toNode = it.to))}
        connectionsRepository.saveAll(connectionsDB)
    }
}