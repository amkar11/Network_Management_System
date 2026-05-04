package com.hitachi.network_management_system.daos

import com.hitachi.network_management_system.dto.ConnectionDTO
import com.hitachi.network_management_system.repositories.IConnectionsRepository
import com.hitachi.network_management_system.repositories.IDevicesRepository
import com.hitachi.network_management_system.topology_db.ConnectionDB
import com.hitachi.network_management_system.topology_db.DeviceDB
import com.hitachi.network_management_system.topology_graph.TopologyGraph
import com.hitachi.network_management_system.topology_graph.TopologyGraph.Companion.getReachableConnections
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Component

@Component
class ConnectionsDAO(
    private val devicesRepository: IDevicesRepository,
    private val connectionsRepository: IConnectionsRepository,
    private val devicesDAO: DevicesDAO
) : IConnectionsDAO {

    override suspend fun getReachableConnections(id: Int): List<DeviceDB> {
        val devices = devicesRepository.findAll().toList()
        val connections = getAllConnections()
        val topologyGraph = TopologyGraph(devices.size, devicesDAO)
        topologyGraph.constructTopologyGraph(connections)
        val reachableConnections = topologyGraph.getReachableConnections(id)
        return reachableConnections
    }

    override suspend fun getAllConnections(): List<ConnectionDB> {
        val connections = connectionsRepository.findAll().toList()
        if (connections.isEmpty()) {
            throw IllegalStateException("There must be connections at the database")
        }
        return connections
    }

    override suspend fun createConnections(connections: List<ConnectionDTO>) {
        val connectionsDB = connections.map {ConnectionDB(id = null, fromNode = it.from, toNode = it.to)}
        connectionsRepository.saveAll(connectionsDB).collect()
    }

    override suspend fun getDevicesIdList(id: Int): List<Int> {
        val reachableConnections = getReachableConnections(id)
        val reachableDevices = reachableConnections.map {it.id as Int}
        return reachableDevices
    }
}