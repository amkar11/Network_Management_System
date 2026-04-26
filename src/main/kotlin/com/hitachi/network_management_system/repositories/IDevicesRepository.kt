package com.hitachi.network_management_system.repositories

import com.hitachi.network_management_system.topology_db.DeviceDB
import org.springframework.data.jpa.repository.JpaRepository

interface IDevicesRepository : JpaRepository<DeviceDB, Int>