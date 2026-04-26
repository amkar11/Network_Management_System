package com.hitachi.network_management_system.repositories

import com.hitachi.network_management_system.dto.ConnectionDTO
import com.hitachi.network_management_system.dto.DeviceDTO
import com.hitachi.network_management_system.dto.SSEStateResponseDTO
import com.hitachi.network_management_system.topology_mock_db.DeviceDB
import org.springframework.data.jpa.repository.JpaRepository

interface IDevicesRepository : JpaRepository<DeviceDB, Int> {

}