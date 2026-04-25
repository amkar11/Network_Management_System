package com.hitachi.network_management_system.repositories

import com.hitachi.network_management_system.dto.ConnectionDTO
import com.hitachi.network_management_system.dto.DeviceDTO
import com.hitachi.network_management_system.dto.SSEStateResponseDTO

interface IDevicesRepository {
    fun changeDevice(id: Int, isActive: Boolean): DeviceDTO
    fun returnInitState(id: Int): SSEStateResponseDTO
    fun getReachableConnections(id: Int): List<ConnectionDTO>
    fun getAllConnectionsByDevicesId(id: Int): List<ConnectionDTO>
    fun getDevicesIdList(id: Int): List<Int>
}