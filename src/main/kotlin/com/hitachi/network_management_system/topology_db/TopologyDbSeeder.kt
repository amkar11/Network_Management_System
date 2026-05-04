package com.hitachi.network_management_system.topology_db
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.hitachi.network_management_system.dto.TopologyDTO
import com.hitachi.network_management_system.daos.IConnectionsDAO
import com.hitachi.network_management_system.daos.IDevicesDAO
import jakarta.annotation.PostConstruct
import kotlinx.coroutines.runBlocking
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.io.File

@Component
class TopologyDbSeeder(
    private val mapper: ObjectMapper,
    private val connectionsDAO: IConnectionsDAO,
    private val devicesDAO: IDevicesDAO
) {
    @EventListener(ApplicationReadyEvent::class)
    fun load() = runBlocking {
        val topology: TopologyDTO = mapper.readValue(File("topology.json"))
        devicesDAO.createDevices(topology.devices)
        connectionsDAO.createConnections(topology.connections)
    }
}