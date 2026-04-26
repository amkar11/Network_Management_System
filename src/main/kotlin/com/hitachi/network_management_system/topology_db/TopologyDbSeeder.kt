package com.hitachi.network_management_system.topology_db
import com.hitachi.network_management_system.dto.TopologyDTO
import com.hitachi.network_management_system.repositories.ITopologyRepository
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component
import tools.jackson.databind.ObjectMapper
import tools.jackson.module.kotlin.readValue
import java.io.File

@Component
class TopologyDbSeeder(
    private val mapper: ObjectMapper,
    private val repository: ITopologyRepository
) {
    @PostConstruct
    fun load() {
        val topology: TopologyDTO = mapper.readValue(File("topology.json"))
        repository.createDevices(topology.devices)
        repository.createConnections(topology.connections)
    }
}