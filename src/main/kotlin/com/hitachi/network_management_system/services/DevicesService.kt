package com.hitachi.network_management_system.services

import com.hitachi.network_management_system.enums.DeviceState
import com.hitachi.network_management_system.daos.ConnectionsDAO
import com.hitachi.network_management_system.daos.DevicesDAO
import com.hitachi.network_management_system.topology_db.DeviceDB
import com.hitachi.network_management_system.topology_graph.TopologyGraph
import com.hitachi.network_management_system.topology_graph.TopologyGraph.Companion.getReachableConnections
import org.springframework.stereotype.Component

@Component
class DevicesService(
    private val devicesDao: DevicesDAO,
    private val connectionsDAO: ConnectionsDAO
) : IDevicesService {

    override suspend fun getReachableDevices(id: Int): List<DeviceDB> {
        val devices = devicesDao.getAllDevices()
        val connections = connectionsDAO.getAllConnections()
        val topologyGraph = TopologyGraph(devices.size, devicesDao)
        topologyGraph.constructTopologyGraph(connections)
        val reachableConnections = topologyGraph.getReachableConnections(id)
        return reachableConnections
    }

    override suspend fun getDevicesIdList(id: Int): List<Int> {
        val reachableDevices = getReachableDevices(id)
        val reachableDevicesIds = reachableDevices.map {it.id as Int}
        return reachableDevicesIds
    }

    override suspend fun getAddedOrRemovedDevices(
        devicesOldState: List<Int>,
        devicesNewState: List<Int>,
        active: DeviceState
    ): List<Int> {
        val devicesOldStateSet = devicesOldState.toSet()
        val devicesNewStateSet = devicesNewState.toSet()
        return if (active == DeviceState.ADDED) {
            (devicesNewStateSet subtract devicesOldStateSet).toList()
        } else {
            (devicesOldStateSet subtract devicesNewStateSet).toList()
        }
    }
}