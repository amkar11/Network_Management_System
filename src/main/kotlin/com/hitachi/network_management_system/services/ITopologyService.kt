package com.hitachi.network_management_system.services

import com.hitachi.network_management_system.dto.DeviceDTO
import com.hitachi.network_management_system.dto.SSEStateResponseDTO
import org.springframework.http.codec.ServerSentEvent
import reactor.core.publisher.Flux

interface ITopologyService {
    suspend fun changeDevice(id: Int, isActive: Boolean): DeviceDTO
    suspend fun  returnInitState(id: Int): Flux<ServerSentEvent<SSEStateResponseDTO>>
    fun getStateUpdate(id: Int): Flux<ServerSentEvent<SSEStateResponseDTO>>
}