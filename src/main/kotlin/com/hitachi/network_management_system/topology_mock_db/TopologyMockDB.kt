package com.hitachi.network_management_system.topology_mock_db
import com.hitachi.network_management_system.dto.TopologyDTO
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component
import tools.jackson.databind.ObjectMapper
import tools.jackson.module.kotlin.readValue
import java.io.File

@Component
class TopologyMockDB(
    private val mapper: ObjectMapper
) {

    lateinit var topology: TopologyDTO

    @PostConstruct
    fun load() {
        topology = mapper.readValue(File("topology.json"))
        println(topology)
    }
}