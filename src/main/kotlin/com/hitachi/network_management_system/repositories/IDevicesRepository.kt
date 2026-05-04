package com.hitachi.network_management_system.repositories

import com.hitachi.network_management_system.topology_db.DeviceDB
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface IDevicesRepository : CoroutineCrudRepository<DeviceDB, Int>