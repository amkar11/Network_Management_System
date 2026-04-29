package com.hitachi.network_management_system.daos

import com.hitachi.network_management_system.topology_db.DeviceDB
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import kotlin.test.Test

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class DevicesDaoTests(
    private val devicesDAO: IDevicesDAO
) {

    //INITIAL DATA GETS SEEDED FROM @POSTCONSTRUCT METHOD IN TopologyDbSeeder CLASS

    //Test device initial data
    val id = 0
    val name = "Warszawa"
    val active = true

    @Nested
    @DisplayName("DevicesDAO tests happy path")
    @TestInstance(TestInstance.Lifecycle.PER_METHOD)
    inner class DevicesDaoHappyPath {

        @Test
        fun `should return device by its id`() {
            // when
            val device = devicesDAO.getDevice(id)

            // then
            assertThat(device.name).isEqualTo(name)
            assertThat(device.active).isEqualTo(active)
        }

        @Test
        fun `should change devices status and return that device with new status`() {
            // given
            val device = devicesDAO.getDevice(id)
            assertThat(device.id).isEqualTo(id)
            assertThat(device.name).isEqualTo(name)
            assertThat(device.active).isEqualTo(active)

            // when
            val changedDevice = devicesDAO.changeDevice(id, isActive = false)

            // then
            assertThat(changedDevice.id).isEqualTo(id)
            assertThat(changedDevice.name).isEqualTo(name)
            assertThat(changedDevice.active).isEqualTo(false)

            devicesDAO.changeDevice(id, isActive = true)
        }

        @Test
        fun `should return devices id list`() {
            // given
            val idList = listOf(11, 13, 14)

            // when
            val devicesIdList = devicesDAO.getDevicesIdList(7)

            // then
            assertThat(devicesIdList).isEqualTo(idList)
        }
    }

    @Nested
    @DisplayName("DevicesDAO tests bad path")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class DevicesDaoBadPath {

        @Test
        fun `should throw NoSuchElementException when getting device`() {
            // given
            val falseId = Int.MAX_VALUE

            // when
            val getDeviceFunc: (id: Int) -> DeviceDB = { devicesDAO.getDevice(it) }

            // then
            val exception = assertThrows<NoSuchElementException> { getDeviceFunc(falseId) }
            assertThat(exception.message).isEqualTo("No device found with id $falseId")
        }

        @Test
        fun `should return the same device when devices active equals to isActive`() {
            // given
            val device = devicesDAO.getDevice(id)
            assertThat(device.id).isEqualTo(id)
            assertThat(device.name).isEqualTo(name)
            assertThat(device.active).isEqualTo(active)

            // when
            val changedDevice = devicesDAO.changeDevice(id, isActive = active)

            // then
            assertThat(changedDevice.id).isEqualTo(id)
            assertThat(changedDevice.name).isEqualTo(name)
            assertThat(changedDevice.active).isEqualTo(active)
        }
    }
}