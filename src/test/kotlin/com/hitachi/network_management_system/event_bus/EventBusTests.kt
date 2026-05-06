package com.hitachi.network_management_system.event_bus

import com.hitachi.network_management_system.dto.SSEStateResponseDTO
import com.hitachi.network_management_system.event_bus.EventBus.Companion.flux
import com.hitachi.network_management_system.services.IDevicesService
import com.hitachi.network_management_system.topology_graph.DevicesCurrentState
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class EventBusTests(
    private var eventBus: EventBus,
    private val devicesService: IDevicesService,
    private val devicesCurrentState: DevicesCurrentState
) {
    private val subscriber = 0

    @BeforeEach
    fun setup() {
        eventBus = EventBus(devicesService, devicesCurrentState)
    }

    @Nested
    @DisplayName("EventBus tests happy path")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class EventBusTestsHappyPath {

        @Test
        fun `should create sink`() {
            // when
            val sink = eventBus.createSink()
            // then
            assertThat(sink).isInstanceOf(Sinks.Many::class.java)
        }

        @Test
        fun `should subscribe to a sink with the given deviceId`() {
            // when
            eventBus.subscribe(subscriber)

            // then
            assertTrue(subscriber in eventBus.getSubscribers())
        }

        @Test
        fun `should get all subscribers`() {
            // given
            val subscribers = listOf(5, 2, 3, 4)

            // when
            for (subscriber in subscribers) {
                eventBus.subscribe(subscriber)
            }

            //then
            assertThat(eventBus.getSubscribers()).hasSize(subscribers.size)
        }

        @Test
        fun `should get sink by particular subscriber id`() {
            // given
            eventBus.subscribe(subscriber)

            // when
            val sink = eventBus.getSink(subscriber)

            // then
            assertThat(sink).isNotNull()
            assertThat(sink).isInstanceOf(Sinks.Many::class.java)
        }

        @Test
        fun `should return Flux view of a sink`() {
            // given
            val sink = eventBus.createSink()

            // when
            val flux = sink.flux()

            // then
            assertThat(flux).isInstanceOf(Flux::class.java)

        }

        @Test
        fun `should emit SSEStateResponseDTO event`() {
            // given
            val event = SSEStateResponseDTO("test event")

            // when
            eventBus.subscribe(subscriber)

            // then
            val emitResult = eventBus.publish(subscriber, event)
            assertTrue(emitResult.isSuccess)
        }
    }

    @Nested
    @DisplayName("EventBus tests bad path")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class EventBusTestsBadPath {

        @Test
        fun `should throw IllegalStateException when getting sink of non-existing subscriber`() {
            // given
            val subscriber = Int.MAX_VALUE

            // when
            val getSinkFunc: (id: Int) -> Sinks.Many<SSEStateResponseDTO> = { eventBus.getSink(it) }

            // then
            val exception = assertThrows<IllegalStateException> { getSinkFunc(subscriber) }
            assertThat(exception).hasMessage("No subscriber with id: $subscriber")
        }
    }
}