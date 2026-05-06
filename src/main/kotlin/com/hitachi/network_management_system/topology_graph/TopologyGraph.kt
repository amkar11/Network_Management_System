package com.hitachi.network_management_system.topology_graph

import com.hitachi.network_management_system.daos.DevicesDAO
import com.hitachi.network_management_system.topology_db.ConnectionDB
import com.hitachi.network_management_system.topology_db.DeviceDB
import java.util.LinkedList

class TopologyGraph(
    val vertices: Int,
    val devicesDAO: DevicesDAO
) {
    val adjacencyList: ArrayList<ArrayList<Edge>> = ArrayList(vertices)

    init {
        for (i in 0..<vertices) {
            adjacencyList.add(ArrayList())
        }
    }

    fun addEdge(source: DeviceDB, destination: DeviceDB) {
        if (source.id is Int && destination.id is Int) {
            adjacencyList[source.id].add(Edge(source, destination))
            adjacencyList[destination.id].add(Edge(destination, source))
        } else {
            throw IllegalStateException("Id of some device is somehow null ")
        }
    }

    suspend fun constructTopologyGraph(connections: List<ConnectionDB>) {
        for (connection in connections) {
            val sourceDevice = devicesDAO.getDevice(connection.fromNode)
            val destinationDevice = devicesDAO.getDevice(connection.toNode)
            addEdge(sourceDevice, destinationDevice)
        }
    }

    companion object {
        fun TopologyGraph.getReachableConnections(startVertex: Int): List<DeviceDB> {
            val visited = BooleanArray(vertices)
            val queue = LinkedList<Int>()
            val reachableDevices = mutableListOf<DeviceDB>()
            visited[startVertex] = true
            queue.add(startVertex)

            while (queue.isNotEmpty()) {
                val currentVertex = queue.poll()

                var noActiveDevices = 0

                for (edge in this.adjacencyList[currentVertex]) {
                    val neighbor: DeviceDB = edge.destination
                    if (!neighbor.active) {
                        noActiveDevices++
                        if (noActiveDevices == this.adjacencyList[currentVertex].size) {
                            return reachableDevices.toList()
                        }
                        continue
                    }

                    if (neighbor.id is Int && !visited[neighbor.id]) {
                        visited[neighbor.id] = true
                        queue.add(neighbor.id)
                        reachableDevices.add(neighbor)
                    }
                }
            }
            return reachableDevices.toList()
        }
    }

    class Edge(val source: DeviceDB, val destination: DeviceDB)
}