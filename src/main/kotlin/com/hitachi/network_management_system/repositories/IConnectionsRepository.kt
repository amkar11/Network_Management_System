package com.hitachi.network_management_system.repositories

import com.hitachi.network_management_system.topology_db.ConnectionDB
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface IConnectionsRepository : CoroutineCrudRepository<ConnectionDB, Int> {
    fun findAllByFromNode(fromNode: Int): Flow<ConnectionDB>
}