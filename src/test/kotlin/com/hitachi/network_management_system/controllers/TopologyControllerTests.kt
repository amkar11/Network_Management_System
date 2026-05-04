package com.hitachi.network_management_system.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.hitachi.network_management_system.dto.DeviceDTO
import com.hitachi.network_management_system.dto.StateDTO
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.patch

@SpringBootTest
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class TopologyControllerTests(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper
) {

    private val deviceId = 0
    private val baseUrl = "/devices/$deviceId"
    private val getUrl = "$baseUrl/reachable-devices"

    @Nested
    @DisplayName("TopologyController tests happy path")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class TopologyControllerTestsHappyPath {

        @Test
        fun `should return OK and changed device`() {
            // given
            val active = false
            val stateDto = StateDTO(active = active)
            val deviceDto = DeviceDTO(id = deviceId, name="Warszawa", active = active)

            // when
            val performPatchRequest = mockMvc.patch(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(stateDto)
            }

            // then
            performPatchRequest
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        json(objectMapper.writeValueAsString(deviceDto))
                    }
                }
        }

        @Test
        fun `should return OK and initial state`() {
            // given
            val sseString =
                """
                id:0
                event:INITIAL_STATE
                data:{"type":"INITIAL_STATE","deviceIds":[1,2,3,5]}
                retry:1000
                
                
                """.trimIndent()

            // when
            val performGetRequest = mockMvc.get(getUrl)

            // then
            performGetRequest
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content {
                        contentType(MediaType.TEXT_EVENT_STREAM)
                        string(sseString)
                    }
                }
            }
        }

    @Nested
    @DisplayName("TopologyController tests bad path")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class TopologyControllerTestsBadPath {

        val invalidDeviceId = Int.MAX_VALUE
        val baseInvalidUrl = "/devices/$invalidDeviceId"
        val getInvalidUrl = "$baseInvalidUrl/reachable-devices"

        @Test
        fun `should return NOT FOUND while trying to patch device if device with given id does not exist`() {
            // given
            val active = false
            val stateDto = StateDTO(active = active)

            // when/then
            mockMvc.patch(baseInvalidUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(stateDto)
            }
                .andDo { print() }
                .andExpect { status { isNotFound() } }
        }

        @Test
        fun `should return NOT FOUND while trying to subscribe to non-existing device`() {
            // when/then
            mockMvc.get(getInvalidUrl)
                .andDo { print() }
                .andExpect { status { isNotFound() } }
        }
    }
}