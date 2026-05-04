//package com.hitachi.network_management_system.event_bus
//
//import com.hitachi.network_management_system.dto.SSEStateResponseDTO
//import com.hitachi.network_management_system.event_bus.EventBus.Companion.flux
//import com.hitachi.network_management_system.daos.IConnectionsDAO
//import com.hitachi.network_management_system.daos.IDevicesDAO
//import com.hitachi.network_management_system.topology_db.ConnectionDB
//import com.hitachi.network_management_system.topology_db.DeviceDB
//import io.mockk.coEvery
//import io.mockk.every
//import io.mockk.mockk
//import kotlinx.coroutines.runBlocking
//import kotlinx.coroutines.test.runTest
//import org.assertj.core.api.Assertions.assertThat
//import org.junit.jupiter.api.Assertions.assertTrue
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.DisplayName
//import org.junit.jupiter.api.Nested
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.TestInstance
//import org.junit.jupiter.api.assertThrows
//import reactor.core.publisher.Flux
//import reactor.core.publisher.Sinks
//
//
//class EventBusTests {
//
//    private val mockConnectionsDAO = mockk<IConnectionsDAO>()
//    private val mockDevicesDAO = mockk<IDevicesDAO>()
//    private var eventBus = EventBus(mockConnectionsDAO, mockDevicesDAO)
//    private val subscriber = 0
//
//    @BeforeEach
//    fun setup() {
//        eventBus = EventBus(mockConnectionsDAO, mockDevicesDAO)
//    }
//
//    @Nested
//    @DisplayName("EventBus tests happy path")
//    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
//    inner class EventBusTestsHappyPath {
//
//        @Test
//        fun `should create sink`() {
//            // when
//            val sink = eventBus.createSink()
//            // then
//            assertThat(sink).isInstanceOf(Sinks.Many::class.java)
//        }
//
//        @Test
//        fun `should subscribe to a sink with the given deviceId`() {
//            // when
//            eventBus.subscribe(subscriber)
//
//            // then
//            assertTrue(subscriber in eventBus.getSubscribers())
//        }
//
//        @Test
//        fun `should get all subscribers`() {
//            // given
//            val subscribers = listOf(5, 2, 3, 4)
//
//            // when
//            for (subscriber in subscribers) {
//                eventBus.subscribe(subscriber)
//            }
//
//            //then
//            assertThat(eventBus.getSubscribers()).hasSize(subscribers.size)
//        }
//
//        @Test
//        fun `should get sink by particular subscriber id`() {
//            // given
//            eventBus.subscribe(subscriber)
//
//            // when
//            val sink = eventBus.getSink(subscriber)
//
//            // then
//            assertThat(sink).isNotNull()
//            assertThat(sink).isInstanceOf(Sinks.Many::class.java)
//        }
//
//        @Test
//        fun `should return Flux view of a sink`() {
//            // given
//            val sink = eventBus.createSink()
//
//            // when
//            val flux = sink.flux()
//
//            // then
//            assertThat(flux).isInstanceOf(Flux::class.java)
//
//        }
//
//        @Test
//        fun `should emit SSEStateResponseDTO event`() {
//            // given
//            val event = SSEStateResponseDTO("test event")
//
//            // when
//            eventBus.subscribe(subscriber)
//
//
//            // then
//            val emitResult = eventBus.publish(subscriber, event)
//            assertTrue(emitResult.isSuccess)
//        }
//
//        @Test
//        fun `should emit DeviceState REMOVED and ADDED event to all subscribers with all devices turned on`() = runTest {
//            // given
//            val reachableConnections = populateReachableConnectionMap()
//            reachableConnections .forEach { println("${it.key}: ${it.value.size}")  }
//            for (subscriber in reachableConnections.keys) {
//                val connections = reachableConnections[subscriber]!!
//                coEvery { mockConnectionsDAO.getReachableConnections(subscriber) } returns connections
//                coEvery { mockConnectionsDAO.getAllConnectionsByDeviceId(subscriber) } returns connections
//                for (i in reachableConnections[subscriber]!!.indices) {
//                    every { runBlocking { mockDevicesDAO.getDevice(reachableConnections[subscriber]!![i].toNode) } } returns DeviceDB(subscriber, "example", true)
//                }
//            }
//
//            // when
//            val eventsListFalseState = eventBus.emitChangesToSubscribers(subscriber, false)
//            val eventsListTrueState = eventBus.emitChangesToSubscribers(subscriber, true)
//
//            // then
//            assertThat(eventsListFalseState).hasSize(21)
//            assertThat(eventsListFalseState). allMatch { it.isSuccess }
//            assertThat(eventsListTrueState).hasSize(21)
//            assertThat(eventsListTrueState). allMatch { it.isSuccess }
//        }
//
//        @Test
//        fun `should emit DeviceState REMOVED only for removed devices`() = runTest {
//            // given
//            eventBus.subscribe(subscriber)
//            val connections = mutableListOf<ConnectionDB>()
//            for (i in 0..4) connections.add(ConnectionDB(fromNode = subscriber, toNode = i))
//            coEvery { mockConnectionsDAO.getReachableConnections(subscriber) } returns connections
//            coEvery { mockConnectionsDAO.getAllConnectionsByDeviceId(subscriber) } returns connections
//            for (i in connections.indices) {
//                if (i == 3) {
//                    coEvery { mockDevicesDAO.getDevice(connections[i].toNode) } returns DeviceDB(subscriber, "example", false)
//                    continue
//                }
//                coEvery { mockDevicesDAO.getDevice(connections[i].toNode) } returns DeviceDB(subscriber, "example", true)
//            }
//
//            // when
//            val eventsListFalseState = eventBus.emitChangesToSubscribers(subscriber, false)
//
//            // then
//            assertThat(eventsListFalseState).hasSize(3)
//            assertThat(eventsListFalseState). allMatch { it.isSuccess }
//
//
//        }
//
//        fun populateReachableConnectionMap() : MutableMap<Int, MutableList<ConnectionDB>> {
//            val subscribers = listOf(0, 1, 2, 3, 4, 5)
//            for (subscriber in subscribers) eventBus.subscribe(subscriber)
//            val reachableConnections: MutableMap<Int, MutableList<ConnectionDB>> = mutableMapOf()
//            for (subscriber in subscribers) {
//                reachableConnections[subscriber] = mutableListOf()
//                for (i in 0..subscriber) {
//                    reachableConnections[subscriber]!!.add(ConnectionDB(fromNode = subscriber, toNode = i))
//                }
//            }
//            return reachableConnections
//        }
//    }
//
//    @Nested
//    @DisplayName("EventBus tests bad path")
//    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
//    inner class EventBusTestsBadPath {
//
//        @Test
//        fun `should throw IllegalStateException when getting sink of non-existing subscriber`() {
//            // given
//            val subscriber = Int.MAX_VALUE
//
//            // when
//            val getSinkFunc: (id: Int) -> Sinks.Many<SSEStateResponseDTO> = { eventBus.getSink(it) }
//
//            // then
//            val exception = assertThrows<IllegalStateException> { getSinkFunc(subscriber) }
//            assertThat(exception).hasMessage("No subscriber with id: $subscriber")
//        }
//
//        @Test
//        fun `should do nothing if trying to subscribe to already existing device`() {
//            // given
//            eventBus.subscribe(subscriber)
//            val sink = eventBus.getSink(subscriber)
//
//            // when
//            eventBus.subscribe(subscriber)
//            val sameSink = eventBus.getSink(subscriber)
//
//            // then
//            assertThat(sink).isSameAs(sameSink)
//
//        }
//    }
//
//}