package com.hitachi.network_management_system.services

import com.hitachi.network_management_system.enums.DeviceState
import com.hitachi.network_management_system.topology_db.DeviceDB

interface IDevicesService {
    suspend fun getReachableDevices(id: Int): List<DeviceDB>
    suspend fun getDevicesIdList(id: Int): List<Int>
    suspend fun getAddedOrRemovedDevices(devicesOldState: List<Int>,
                                         devicesNewState: List<Int>,
                                         active: DeviceState
    ): List<Int>
}