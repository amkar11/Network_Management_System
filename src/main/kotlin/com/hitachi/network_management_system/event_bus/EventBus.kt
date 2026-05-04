package com.hitachi.network_management_system.event_bus

import com.hitachi.network_management_system.dto.SSEChangedStateResponseDTO
import com.hitachi.network_management_system.dto.SSEStateResponseDTO
import com.hitachi.network_management_system.enums.DeviceState
import com.hitachi.network_management_system.daos.IConnectionsDAO
import com.hitachi.network_management_system.daos.IDevicesDAO
import com.hitachi.network_management_system.topology_db.ConnectionDB
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import kotlin.collections.forEach

@Component
class EventBus(
    private val connectionsDAO: IConnectionsDAO,
    private val devicesDAO: IDevicesDAO
) {
    private val subscribers: MutableMap<Int, Sinks.Many<SSEStateResponseDTO>> = mutableMapOf()

    fun getSubscribers() : List<Int> = subscribers.keys.toList()

    fun publish(id: Int, event: SSEStateResponseDTO): Sinks.EmitResult {
        val sink = getSink(id)
        return sink.tryEmitNext(event)
    }
    companion object {
            fun Sinks.Many<SSEStateResponseDTO>.flux(): Flux<SSEStateResponseDTO> {
                return this.asFlux()
        }
    }

    fun subscribe(id: Int) {
        if (!subscribers.containsKey(id)) {
            val sink = createSink()
            subscribers[id] = sink
        }
    }

    fun createSink(): Sinks.Many<SSEStateResponseDTO> {
        val sink = Sinks.many().multicast().onBackpressureBuffer<SSEStateResponseDTO>()
        return sink
    }

    fun getSink(id: Int): Sinks.Many<SSEStateResponseDTO> {
        return subscribers[id] ?: throw IllegalStateException("No subscriber with id: $id")
    }

    suspend fun emitChangesToSubscribers(id: Int, isActive: Boolean): List<Sinks.EmitResult> {
        val subscribers: List<Int> = getSubscribers()
        val eventType: DeviceState = if (isActive) DeviceState.ADDED else DeviceState.REMOVED

        // This eventsList is for testing purposes
        val eventsList: MutableList<Sinks.EmitResult> = mutableListOf()

        for (subscriber in subscribers) {
            // ADDED requires all connections independent of status, while REMOVED only currently reachable ones
            val connections: List<ConnectionDB> = if (eventType == DeviceState.ADDED) {
                connectionsDAO.getAllConnectionsByDeviceId(subscriber)
            } else {
                connectionsDAO.getReachableConnections(subscriber)
            }

            // Maps List<ConnectionDB> to list which contains IDs of reachable devices
            val reachableDevices: MutableList<Int> = mutableListOf()
            connections.forEach { reachableDevices.add(it.toNode) }

            if (id !in reachableDevices) continue

            // To loop only through devices which come after the changed one
            val index: Int = reachableDevices.indexOf(id)

            // If event is ADDED and there is turned off device prior to the changed one it breaks the loop cause
            // in spite of device being added it is still unreachable due to turned off device prior to it
            var broke = false
            if (eventType == DeviceState.ADDED) {
                for (i in reachableDevices.indices) {
                    val device = devicesDAO.getDevice((reachableDevices[i]))
                    if (!device.active && i < index){
                        broke = true
                        break
                    }
                }
            }
            if (broke) continue

            // Loops through all reachable devices - if some device which comes after the changed one is unreachable - loop is broken
            // Otherwise, SSEChangedState event gets emitted and Sinks.EmitResult added to the eventsList for testing purposes
            for (i in index..reachableDevices.lastIndex) {
                if (i != index && !devicesDAO.getDevice(reachableDevices[i]).active) break
                val event = publish(subscriber, SSEChangedStateResponseDTO(eventType.toString(), reachableDevices[i]))
                eventsList.add(event)
            }
        }
        return eventsList
    }


}