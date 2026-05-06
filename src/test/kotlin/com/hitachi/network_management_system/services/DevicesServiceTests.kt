package com.hitachi.network_management_system.services

import com.hitachi.network_management_system.enums.DeviceState
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class DevicesServiceTests(
    private val devicesService: IDevicesService,
) {

        @Test
        fun `should return list of reachable devices ids`() = runTest {
            // when
            val reachableDevices = devicesService.getReachableDevices(0)

            // then
            assertThat(reachableDevices).hasSize(19)

        }

        @Test
        fun `should return added if DeviceState event is ADDED`() {
            // given
            val devicesNewState = listOf(3, 4, 5, 7, 2, 9)
            val devicesOldState = listOf(3, 4, 2)

            // when
            val addedDevices = devicesService.getAddedOrRemovedDevices(devicesOldState, devicesNewState, DeviceState.ADDED)

            // then
            assertThat(addedDevices).isEqualTo(listOf(5, 7, 9))

        }

        @Test
        fun `should return removed if DeviceState event is REMOVED`() {
            // given
            val devicesNewState = listOf(3, 4, 9)
            val devicesOldState = listOf(1, 4, 7, 3, 8, 9)

            // when
            val removedDevices = devicesService.getAddedOrRemovedDevices(devicesOldState, devicesNewState, DeviceState.REMOVED)

            // then
            assertThat(removedDevices).isEqualTo(listOf(1, 7, 8))

        }
    }
