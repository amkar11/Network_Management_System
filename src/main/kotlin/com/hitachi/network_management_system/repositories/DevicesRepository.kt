package com.hitachi.network_management_system.repositories

import com.hitachi.network_management_system.topology_mock_db.TopologyMockDB
import com.hitachi.network_management_system.dto.ConnectionDTO
import com.hitachi.network_management_system.dto.DeviceDTO
import com.hitachi.network_management_system.dto.SSEInitStateResponseDTO
import com.hitachi.network_management_system.dto.SSEStateResponseDTO
import com.hitachi.network_management_system.enums.DeviceState
import org.springframework.stereotype.Repository

@Repository
class DevicesRepository(
    val topologyMockDB: TopologyMockDB
    ) : IDevicesRepository {

    override fun changeDevice(id: Int, isActive: Boolean): DeviceDTO {
        val device = getDevice(id)
        if (device.active == isActive) return device
        device.active = isActive
        return device
    }

    override fun returnInitState(id: Int): SSEStateResponseDTO {
        val reachableDevices = getDevicesIdList(id)
        return SSEInitStateResponseDTO(DeviceState.INITIAL_STATE.toString(), reachableDevices)
    }

    override fun getReachableConnections(id: Int): List<ConnectionDTO> {
        val allConnections = getAllConnectionsByDevicesId(id)
        val reachableConnections: MutableList<ConnectionDTO> = mutableListOf()
        for (connection in allConnections) {
            val device = getDevice(connection.to)
            if (!device.active) break
            reachableConnections.add(connection)
        }
        return reachableConnections
    }

    override fun getAllConnectionsByDevicesId(id: Int): List<ConnectionDTO> {
        val device = getDevice(id)
        val allConnections: List<ConnectionDTO> = topologyMockDB.topology.connections
            .filter { it.from == device.id } .sortedBy { it.to }
        return allConnections
    }

    override fun getDevicesIdList(id: Int): List<Int> {
        val reachableConnections = getReachableConnections(id)
        val reachableDevices: MutableList<Int> = mutableListOf()
        reachableConnections.forEach { reachableDevices.add(it.to) }
        return reachableDevices
    }


    fun getDevice(id: Int): DeviceDTO {
        return topologyMockDB.topology.devices.find { it.id == id}
            ?: throw NoSuchElementException("No device with id: $id")
    }

}