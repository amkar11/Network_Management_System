package com.hitachi.network_management_system.services

import com.hitachi.network_management_system.enums.DeviceState
import com.hitachi.network_management_system.daos.ConnectionsDAO
import com.hitachi.network_management_system.daos.DevicesDAO
import com.hitachi.network_management_system.topology_graph.TopologyGraph
import com.hitachi.network_management_system.topology_graph.TopologyGraph.Companion.getReachableConnections
import org.springframework.stereotype.Component

@Component
class DevicesService(
    private val devicesDao: DevicesDAO,
    private val connectionsDAO: ConnectionsDAO
) : IDevicesService {

    override suspend fun getReachableDevices(id: Int): List<Int> {
        val devices = devicesDao.getAllDevices()
        val connections = connectionsDAO.getAllConnections()
        val topologyGraph = TopologyGraph(devices.size, devicesDao)
        topologyGraph.constructTopologyGraph(connections)
        val reachableConnections = topologyGraph.getReachableConnections(id)
            .map {it.id as Int}
        return reachableConnections
    }

    override fun getAddedOrRemovedDevices(
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