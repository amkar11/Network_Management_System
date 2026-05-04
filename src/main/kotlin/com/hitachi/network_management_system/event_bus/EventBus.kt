package com.hitachi.network_management_system.event_bus

import com.hitachi.network_management_system.dto.SSEChangedStateResponseDTO
import com.hitachi.network_management_system.dto.SSEStateResponseDTO
import com.hitachi.network_management_system.enums.DeviceState
import com.hitachi.network_management_system.daos.IConnectionsDAO
import com.hitachi.network_management_system.daos.IDevicesDAO
import com.hitachi.network_management_system.topology_graph.DevicesCurrentState
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks

@Component
class EventBus(
    private val connectionsDAO: IConnectionsDAO,
    private val devicesDAO: IDevicesDAO,
    private val devicesCurrentState: DevicesCurrentState
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

    suspend fun emitChangesToSubscribers(isActive: Boolean): List<Sinks.EmitResult> {
        val subscribers: List<Int> = getSubscribers()
        val eventType: DeviceState = if (isActive) DeviceState.ADDED else DeviceState.REMOVED

        // This eventsList is for testing purposes
        val eventsList: MutableList<Sinks.EmitResult> = mutableListOf()

        for (subscriber in subscribers) {
            val devicesNewState = connectionsDAO.getDevicesIdList(subscriber)

            val devicesOldState = devicesCurrentState.devicesCurrentState[subscriber] ?:
            throw IllegalStateException("Device is already subscribed," +
                    " so there must be his state in devicesCurrentState")

            val addedOrRemovedDevices: List<Int>
                = devicesDAO.getAddedOrRemovedDevices(devicesOldState, devicesNewState, eventType)

            for (device in addedOrRemovedDevices) {
                val event = publish(subscriber, SSEChangedStateResponseDTO(eventType.toString(), device))
                eventsList.add(event)
            }
        }
        return eventsList
    }
}