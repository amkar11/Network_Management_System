package com.hitachi.network_management_system.event_bus

import com.hitachi.network_management_system.dto.ConnectionDTO
import com.hitachi.network_management_system.dto.SSEChangedStateResponseDTO
import com.hitachi.network_management_system.dto.SSEStateResponseDTO
import com.hitachi.network_management_system.enums.DeviceState
import com.hitachi.network_management_system.repositories.IDevicesRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import kotlin.collections.forEach

@Service
class EventBus(
    private val repository: IDevicesRepository
) {

    private val subscribers: MutableMap<Int, Sinks.Many<SSEStateResponseDTO>> = mutableMapOf()

    fun publish(id: Int, event: SSEStateResponseDTO) {
        val sink = subscribers[id] ?: throw IllegalStateException("No subscriber with id: $id")
        sink.tryEmitNext(event)
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

    fun emitChangesToSubscribers(id: Int, isActive: Boolean) {
        val subscribers: List<Int> = getSubscribers()
        val eventType: DeviceState = if (isActive) DeviceState.ADDED else DeviceState.REMOVED
        for (subscriber in subscribers) {
            val connections: List<ConnectionDTO> = repository.getAllConnectionsByDevicesId(subscriber)
            val reachableDevices: MutableList<Int> = mutableListOf()
            connections.forEach { reachableDevices.add(it.to) }
            if (id in reachableDevices) {
                val index: Int = reachableDevices.indexOf(id)
                for (i in index..reachableDevices.lastIndex) {
                    publish(subscriber, SSEChangedStateResponseDTO(eventType.toString(), reachableDevices[i]))
                }
            }
        }
    }

    fun getSubscribers() : List<Int> = subscribers.keys.toList()
}