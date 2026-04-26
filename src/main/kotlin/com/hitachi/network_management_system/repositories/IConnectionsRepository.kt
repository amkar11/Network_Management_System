package com.hitachi.network_management_system.repositories

import com.hitachi.network_management_system.dto.ConnectionDTO
import com.hitachi.network_management_system.topology_mock_db.ConnectionDB
import org.springframework.data.jpa.repository.JpaRepository

interface IConnectionsRepository : JpaRepository<ConnectionDB, Int> {
    fun findAllByFromNode(fromNode: Int): List<ConnectionDB>
}