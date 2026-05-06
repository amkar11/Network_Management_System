package com.hitachi.network_management_system.services

import com.hitachi.network_management_system.enums.DeviceState

interface IDevicesService {
    suspend fun getReachableDevices(id: Int): List<Int>
    fun getAddedOrRemovedDevices(devicesOldState: List<Int>,
                                 devicesNewState: List<Int>,
                                 active: DeviceState
    ): List<Int>
}