package com.hitachi.network_management_system.daos

import com.hitachi.network_management_system.topology_db.ConnectionDB
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import kotlin.test.Test

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class ConnectionsDaoTests(
    private val connectionsDAO: IConnectionsDAO,
    private val devicesDAO: IDevicesDAO
) {

    val deviceId = 0

    @Nested
    @DisplayName("ConnectionsDAO tests happy path")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class ConnectionsDaoHappyPath {

        @Test
        fun `should return all connections by deviceId`() {
            //given
            val secondConnectionToNode = 2

            // when
            val connections = connectionsDAO.getAllConnectionsByDeviceId(deviceId)

            // then
            assertThat(connections).hasSize(4)
            assertThat(connections[2].fromNode).isEqualTo(deviceId)
            assertThat(connections[1].toNode).isEqualTo(secondConnectionToNode)
        }

        @Test
        fun `should return all reachable connections by deviceId`() {
            // given
            val deviceIdToChange = 3
            val connections = connectionsDAO.getReachableConnections(deviceId)
            assertThat(connections).hasSize(4)
            assertThat(connections[2].toNode).isEqualTo(deviceIdToChange)

            // when
            devicesDAO.changeDevice(deviceIdToChange, isActive = false)
            val reachableConnections = connectionsDAO.getReachableConnections(deviceId)

            // then
            assertThat(reachableConnections).hasSize(2)

        }
    }

    @Nested
    @DisplayName("ConnectionsDAO tests bad path")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class ConnectionsDaoBadPath {

        @Test
        fun `should throw NoSuchElementException while trying to subscribe to non-existing device`() {
            // given
            val falseId = Int.MAX_VALUE

            // when
            val getAllConnectionsFunc: () -> List<ConnectionDB>
                    = { connectionsDAO.getAllConnectionsByDeviceId(falseId) }

            // then
            assertThrows<NoSuchElementException> { getAllConnectionsFunc() }
        }
    }
}