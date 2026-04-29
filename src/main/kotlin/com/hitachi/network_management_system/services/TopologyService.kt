package com.hitachi.network_management_system.services

import com.hitachi.network_management_system.dto.DeviceDTO
import com.hitachi.network_management_system.dto.SSEInitStateResponseDTO
import com.hitachi.network_management_system.dto.SSEStateResponseDTO
import com.hitachi.network_management_system.enums.DeviceState
import com.hitachi.network_management_system.event_bus.EventBus
import com.hitachi.network_management_system.event_bus.EventBus.Companion.flux
import com.hitachi.network_management_system.daos.IDevicesDAO
import org.springframework.http.codec.ServerSentEvent
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.time.Duration
import kotlin.concurrent.atomics.AtomicInt
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.concurrent.atomics.fetchAndIncrement

@Service
@OptIn(ExperimentalAtomicApi::class)
class TopologyService(
    private val devicesDAO: IDevicesDAO,
    private val eventBus: EventBus
) : ITopologyService {

    val atomicInt = AtomicInt(1)

    override fun changeDevice(id: Int, isActive: Boolean): DeviceDTO {
        eventBus.emitChangesToSubscribers(id, isActive)
        val changedDevice = devicesDAO.changeDevice(id, isActive)
        return DeviceDTO(
            id = changedDevice.id,
            name = changedDevice.name,
            active = changedDevice.active)
    }

    override fun returnInitState(id: Int): Flux<ServerSentEvent<SSEStateResponseDTO>> {
        val reachableDevices = devicesDAO.getDevicesIdList(id)
        val sseResponse = SSEInitStateResponseDTO(DeviceState.INITIAL_STATE.toString(), reachableDevices)
        val initState = Flux.just(ServerSentEvent.builder<SSEStateResponseDTO>()
            .id("0")
            .data(sseResponse)
            .event(DeviceState.INITIAL_STATE.toString())
            .retry(Duration.ofMillis(1000))
            .build()
        )
        eventBus.subscribe(id)
        return initState
    }

    override fun getStateUpdate(id: Int): Flux<ServerSentEvent<SSEStateResponseDTO>> {
        val update = eventBus.getSink(id).flux(). map {
            ServerSentEvent.builder<SSEStateResponseDTO>()
                .id(atomicInt.fetchAndIncrement().toString())
                .data(it)
                .event("update")
                .retry(Duration.ofMillis(1000))
                .build()
        }
        return update
    }
}