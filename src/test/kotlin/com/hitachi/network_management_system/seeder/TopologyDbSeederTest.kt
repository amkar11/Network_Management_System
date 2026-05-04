package com.hitachi.network_management_system.seeder

import com.hitachi.network_management_system.repositories.IConnectionsRepository
import com.hitachi.network_management_system.repositories.IDevicesRepository
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class TopologyDbSeederTest(
    private val connectionsRepository: IConnectionsRepository,
    private val devicesRepository: IDevicesRepository
) {

    @Test
    fun `should seed all the data from json file`() = runTest {
        // when
        val devices = devicesRepository.findAll().toList()
        val connections = connectionsRepository.findAll().toList()

        // then
        assertThat(devices).hasSize(20)
        assertThat(connections).hasSize(28)
    }
}