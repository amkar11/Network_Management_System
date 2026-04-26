package com.hitachi.network_management_system.repositories

import com.hitachi.network_management_system.dto.DeviceDTO
import com.hitachi.network_management_system.dto.SSEStateResponseDTO
import com.hitachi.network_management_system.topology_db.DeviceDB

interface IDevicesDAO {
    fun changeDevice(id: Int, isActive: Boolean): DeviceDB
    fun returnInitState(id: Int): SSEStateResponseDTO
    fun getDevicesIdList(id: Int): List<Int>
    fun getDevice(id: Int): DeviceDB
    fun createDevices(devices: List<DeviceDTO>)
}