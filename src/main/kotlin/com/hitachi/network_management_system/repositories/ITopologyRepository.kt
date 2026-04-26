package com.hitachi.network_management_system.repositories

import com.hitachi.network_management_system.dto.ConnectionDTO
import com.hitachi.network_management_system.dto.DeviceDTO
import com.hitachi.network_management_system.dto.SSEStateResponseDTO
import com.hitachi.network_management_system.topology_mock_db.ConnectionDB
import com.hitachi.network_management_system.topology_mock_db.DeviceDB

interface ITopologyRepository {
    fun changeDevice(id: Int, isActive: Boolean): DeviceDB
    fun returnInitState(id: Int): SSEStateResponseDTO
    fun getReachableConnections(id: Int): List<ConnectionDB>
    fun getAllConnectionsByDeviceId(fromNode: Int): List<ConnectionDB>
    fun getDevicesIdList(id: Int): List<Int>
    fun getDevice(id: Int): DeviceDB
    fun createDevices(devices: List<DeviceDTO>)
    fun createConnections(connections: List<ConnectionDTO>)
}