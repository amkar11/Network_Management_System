package com.hitachi.network_management_system.topology_graph

import com.hitachi.network_management_system.daos.DevicesDAO
import com.hitachi.network_management_system.daos.IConnectionsDAO
import com.hitachi.network_management_system.repositories.IDevicesRepository
import com.hitachi.network_management_system.topology_db.DeviceDB
import com.hitachi.network_management_system.topology_graph.TopologyGraph.Companion.getReachableConnections
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class TopologyGraphTests(
    private val devicesRepository: IDevicesRepository,
    private val connectionsDao: IConnectionsDAO
) {

    private val devicesDao = DevicesDAO(devicesRepository)
    private val vertices = runBlocking { devicesDao.getAllDevices().size }
    private var topologyGraph = TopologyGraph(vertices, devicesDao)
    private val connections = runBlocking { connectionsDao.getAllConnections() }

    @BeforeEach
    fun initTopologyGraph() {
        topologyGraph = TopologyGraph(vertices, devicesDao)
    }

    @Test
    fun `should add edge to adjacency list`() {
        // given
        val sourceDeviceDb = DeviceDB(0, "Warszawa", true)
        val destinationDeviceDb = DeviceDB(11, "Gdynia", false)
        val edgeOne = TopologyGraph.Edge(sourceDeviceDb, destinationDeviceDb)
        val edgeTwo = TopologyGraph.Edge(destinationDeviceDb, sourceDeviceDb)

        // when
        topologyGraph.addEdge(sourceDeviceDb, destinationDeviceDb)
        val adjacencyList = topologyGraph.adjacencyList

        // then
        assertThat(adjacencyList[0][0].destination).isEqualTo(edgeOne.destination)
        assertThat(adjacencyList[11][0].source).isEqualTo(edgeTwo.source)

    }

    @Test
    fun `should construct topology graph from the list of connections`() = runTest {
        // when
        topologyGraph.constructTopologyGraph(connections)
        val adjacencyList = topologyGraph.adjacencyList

        // then
        assertThat(adjacencyList[9][0].destination.id).isEqualTo(1)
        assertThat(adjacencyList[9][1].destination.id).isEqualTo(5)
        assertThat(adjacencyList[19][0].destination.id).isEqualTo(15)
        assertThat(adjacencyList[19][1].destination.id).isEqualTo(18)
    }

    @Test
    fun `should get reachable connections for device with id 7 when all devices are active`() = runTest {
        // when
        devicesDao.changeDevice(15, true)
        topologyGraph.constructTopologyGraph(connections)
        val reachableConnections = topologyGraph.getReachableConnections(7)

        // then
        assertThat(reachableConnections).hasSize(19)

    }

    @Test
    fun `should get reachable connections for device with id 7 when device 15 is inactive`() = runTest {
        //
        devicesDao.changeDevice(15, false)
        topologyGraph.constructTopologyGraph(connections)
        val reachableConnections = topologyGraph.getReachableConnections(7)

        // then
        assertThat(reachableConnections).hasSize(14)

    }
}