package com.hitachi.network_management_system.controllers

import com.hitachi.network_management_system.dto.DeviceDTO
import com.hitachi.network_management_system.dto.SSEStateResponseDTO
import com.hitachi.network_management_system.dto.StateDTO
import com.hitachi.network_management_system.services.ITopologyService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.ServerSentEvent
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/devices/{id}")
class TopologyController(
    private val service: ITopologyService,
) {
    @ExceptionHandler(NoSuchElementException::class)
        fun handleNotFound(e: NoSuchElementException): ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.NOT_FOUND)

    @PatchMapping
    suspend fun changeDevice(@PathVariable id: Int, @RequestBody state: StateDTO): DeviceDTO =
            service.changeDevice(id, state.active)

    @GetMapping("/reachable-devices", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
   suspend fun getReachableDevices(@PathVariable id: Int): Flux<ServerSentEvent<SSEStateResponseDTO>> {
        val initState: Flux<ServerSentEvent<SSEStateResponseDTO>> = service.returnInitState(id)
        val update: Flux<ServerSentEvent<SSEStateResponseDTO>> = service.getStateUpdate(id)
        return Flux.concat(initState, update)
    }
}