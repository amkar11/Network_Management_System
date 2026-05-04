package com.hitachi.network_management_system.daos

import com.hitachi.network_management_system.dto.DeviceDTO
import com.hitachi.network_management_system.enums.DeviceState
import com.hitachi.network_management_system.topology_db.DeviceDB

interface IDevicesDAO {
    suspend fun changeDevice(id: Int, isActive: Boolean): DeviceDB
    suspend fun getDevice(id: Int): DeviceDB
    suspend fun createDevices(devices: List<DeviceDTO>)
    suspend fun getAllDevices(): List<DeviceDB>
    suspend fun getAddedOrRemovedDevices(devicesOldState: List<Int>,
                                         devicesNewState: List<Int>,
                                         active: DeviceState): List<Int>
}