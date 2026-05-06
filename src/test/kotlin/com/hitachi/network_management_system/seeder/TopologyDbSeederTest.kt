package com.hitachi.network_management_system.seeder

import com.hitachi.network_management_system.daos.IConnectionsDAO
import com.hitachi.network_management_system.daos.IDevicesDAO
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class TopologyDbSeederTest(
    private val connectionsDao: IConnectionsDAO,
    private val devicesDao: IDevicesDAO,
) {

    // Tests also createDevices() and createConnections() functions since
    // they are used to populate the database, and if test passes, then
    // these functions work as well
    @Test
    fun `should seed all the data from json file`() = runTest {
        // when
        val devices = devicesDao.getAllDevices()
        val connections = connectionsDao.getAllConnections()

        println(devices)
        println()
        println(connections)

        // then
        assertThat(devices).hasSize(20)
        assertThat(connections).hasSize(28)
    }
}