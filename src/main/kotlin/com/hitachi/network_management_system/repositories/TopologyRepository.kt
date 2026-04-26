package com.hitachi.network_management_system.repositories

import com.hitachi.network_management_system.dto.ConnectionDTO
import com.hitachi.network_management_system.dto.DeviceDTO
import com.hitachi.network_management_system.dto.SSEInitStateResponseDTO
import com.hitachi.network_management_system.dto.SSEStateResponseDTO
import com.hitachi.network_management_system.enums.DeviceState
import com.hitachi.network_management_system.topology_db.ConnectionDB
import com.hitachi.network_management_system.topology_db.DeviceDB
import jakarta.transaction.Transactional
import org.springframework.stereotype.Repository

@Repository
class TopologyRepository(
    private val devicesRepository: IDevicesRepository,
    private val connectionsRepository: IConnectionsRepository
    ): ITopologyRepository {

    @Transactional
    override fun changeDevice(id: Int, isActive: Boolean): DeviceDB {
        val device = getDevice(id)
        if (device.active == isActive) return device
        device.active = isActive
        return device
    }

    @Transactional
    override fun returnInitState(id: Int): SSEStateResponseDTO {
        val reachableDevices = getDevicesIdList(id)
        return SSEInitStateResponseDTO(DeviceState.INITIAL_STATE.toString(), reachableDevices)
    }

    @Transactional
    override fun getReachableConnections(id: Int): List<ConnectionDB> {
        val allConnections = getAllConnectionsByDeviceId(id)
        val reachableConnections: MutableList<ConnectionDB> = mutableListOf()
        for (connection in allConnections) {
            val device = getDevice(connection.toNode)
            if (!device.active) break
            reachableConnections.add(connection)
        }
        return reachableConnections
    }

    @Transactional
    override fun getAllConnectionsByDeviceId(fromNode: Int): List<ConnectionDB> {
        return connectionsRepository.findAllByFromNode(fromNode)
    }

    @Transactional
    override fun getDevicesIdList(id: Int): List<Int> {
        val reachableConnections = getReachableConnections(id)
        val reachableDevices: MutableList<Int> = mutableListOf()
        reachableConnections.forEach { reachableDevices.add(it.toNode) }
        return reachableDevices
    }

    @Transactional
    override fun getDevice(id: Int): DeviceDB {
        val deviceDB: DeviceDB = devicesRepository.findById(id)
            .orElseThrow { NoSuchElementException("No device found with id $id") }
        return deviceDB
    }

    override fun createDevices(devices: List<DeviceDTO>) {
        val devicesDB: MutableList<DeviceDB> = mutableListOf()
        devices. forEach { devicesDB.add(DeviceDB(it.id, it.name, it.active)) }
        devicesRepository.saveAll(devicesDB)
    }

    override fun createConnections(connections: List<ConnectionDTO>) {
        val connectionsDB: MutableList<ConnectionDB> = mutableListOf()
        connections.forEach {connectionsDB.add(ConnectionDB(fromNode = it.from, toNode = it.to))}
        connectionsRepository.saveAll(connectionsDB)
    }


}