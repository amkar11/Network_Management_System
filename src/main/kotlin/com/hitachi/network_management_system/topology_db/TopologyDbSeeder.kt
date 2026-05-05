package com.hitachi.network_management_system.topology_db
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.hitachi.network_management_system.dto.TopologyDTO
import com.hitachi.network_management_system.daos.ConnectionsDAO
import com.hitachi.network_management_system.daos.DevicesDAO
import kotlinx.coroutines.runBlocking
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.io.File

@Component
class TopologyDbSeeder(
    private val mapper: ObjectMapper,
    private val connectionsDao: ConnectionsDAO,
    private val devicesDao: DevicesDAO
) {
    @EventListener(ApplicationReadyEvent::class)
    fun load() = runBlocking {
        val topology: TopologyDTO = mapper.readValue(File("topology.json"))
        devicesDao.createDevices(topology.devices)
        connectionsDao.createConnections(topology.connections)
    }
}