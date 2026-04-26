package com.hitachi.network_management_system.topology_mock_db

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
class DeviceDB(
    @Id var id: Int,
    var name: String,
    var active: Boolean)