package com.hitachi.network_management_system.daos

import com.hitachi.network_management_system.dto.DeviceDTO
import com.hitachi.network_management_system.topology_db.DeviceDB

interface IDevicesDAO {
    suspend fun changeDevice(id: Int, isActive: Boolean): DeviceDB
    suspend fun getDevicesIdList(id: Int): List<Int>
    suspend fun getDevice(id: Int): DeviceDB
    suspend fun createDevices(devices: List<DeviceDTO>)
}