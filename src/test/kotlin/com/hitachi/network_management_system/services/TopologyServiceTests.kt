package com.hitachi.network_management_system.services

import com.hitachi.network_management_system.dto.DeviceDTO
import com.hitachi.network_management_system.dto.SSEChangedStateResponseDTO
import com.hitachi.network_management_system.dto.SSEInitStateResponseDTO
import com.hitachi.network_management_system.dto.SSEStateResponseDTO
import com.hitachi.network_management_system.enums.DeviceState
import com.hitachi.network_management_system.event_bus.EventBus
import com.hitachi.network_management_system.daos.IDevicesDAO
import com.hitachi.network_management_system.topology_db.DeviceDB
import com.hitachi.network_management_system.topology_graph.DevicesCurrentState
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.http.codec.ServerSentEvent
import reactor.core.publisher.Sinks
import reactor.test.StepVerifier
import java.time.Duration
import kotlin.concurrent.atomics.AtomicInt
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.concurrent.atomics.fetchAndIncrement

class TopologyServiceTests {
    private val mockDevicesDao = mockk<IDevicesDAO>()
    private val mockEventBus = mockk<EventBus>(relaxed = true)
    private val mockDevicesService = mockk<IDevicesService>()
    private val devicesCurrentState = DevicesCurrentState()
    private val topologyService = TopologyService(mockDevicesDao, mockDevicesService,
        mockEventBus, devicesCurrentState)
    private val deviceId = 0
    private val duration = Duration.ofMillis(1000)

    @Nested
    @DisplayName("TopologyService tests happy path")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class TopologyServiceTestsHappyPath {

        @Test
        fun `should return changed devices`() = runTest {
            // given
            val deviceDb = DeviceDB(deviceId, "example", false)
            val deviceDto = DeviceDTO(deviceId, "example", false)
            coEvery { mockDevicesDao.getDevice(deviceId) } returns deviceDb
            coEvery { mockDevicesDao.changeDevice(deviceId, false) } returns deviceDb

            // when
            val changedDevice = topologyService.changeDevice(deviceId, false)

            // then
            assertThat(changedDevice).isEqualTo(deviceDto)
        }

        @Test
        fun `should return initial state`() = runTest {
            // given
            val reachableDevices = listOf(1, 2, 3, 5)
            val event = DeviceState.INITIAL_STATE.toString()
            coEvery { mockDevicesService.getReachableDevices(deviceId) } returns reachableDevices
            val response = SSEInitStateResponseDTO(event, reachableDevices)

            // when
            val initState = topologyService.returnInitState(deviceId)

            // then
            StepVerifier
                .create(initState)
                .expectNextMatches { actual ->
                    actual.id() == deviceId.toString() &&
                    actual.event() == event &&
                    actual.data() == response &&
                    actual.retry() == duration
        }
                .verifyComplete()
        }

        @OptIn(ExperimentalAtomicApi::class)
        @Test
        fun `should return state update`() {
            // given
            val atomicInt = AtomicInt(1)
            val mockEventBusSpy = spyk(EventBus(mockDevicesService, devicesCurrentState))
            val sink = Sinks.many().multicast().onBackpressureBuffer<SSEStateResponseDTO>()
            every { mockEventBusSpy.getSink(deviceId) } returns sink
            val event = DeviceState.REMOVED.toString()
            val stateUpdate = SSEChangedStateResponseDTO(event, deviceId)
            val sse = ServerSentEvent.builder<SSEStateResponseDTO>()
                .id(atomicInt.fetchAndIncrement().toString())
                .data(stateUpdate)
                .event(DeviceState.REMOVED.toString())
                .retry(duration)
                .build()

            // when
            val getStateUpdate = topologyService.getStateUpdate(deviceId)

            // then
            StepVerifier
                .create(getStateUpdate)
                .expectNext(sse)

        }
    }
}