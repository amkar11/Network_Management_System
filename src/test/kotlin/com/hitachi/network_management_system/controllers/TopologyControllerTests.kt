package com.hitachi.network_management_system.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.hitachi.network_management_system.dto.DeviceDTO
import com.hitachi.network_management_system.dto.StateDTO
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.returnResult
import reactor.test.StepVerifier

@SpringBootTest
@AutoConfigureWebTestClient
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class TopologyControllerTests(
    private val webTestClient: WebTestClient,
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
            val performPatchRequest = webTestClient.patch()
                .uri(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(objectMapper.writeValueAsString(stateDto))
                .exchange()

            // then
            performPatchRequest
                .expectStatus().isOk
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().json(objectMapper.writeValueAsString(deviceDto))
        }

        @Test
        fun `should return OK and initial state`() {
            // given
            val deviceIds = listOf(1,2,3,5,8,9,10,12,6,11,7,13,14,4,15,16,17,18,19)

            // when
            val performGetRequest = webTestClient.get()
                .uri(getUrl)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()

            // then
            val getRequestResult = performGetRequest
                .expectStatus().isOk
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_EVENT_STREAM)
                .returnResult<String>()

            StepVerifier.create(getRequestResult.responseBody)
                .assertNext { chunk ->
                    println(chunk)

                    assert(chunk.contains("INITIAL_STATE"))
                    assert(chunk.contains("deviceIds"))
                    assert(chunk.contains(deviceIds.joinToString(",")))
                }
                .thenCancel()
                .verify()
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
            webTestClient.patch().uri(baseInvalidUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(objectMapper.writeValueAsString(stateDto))
                .exchange()
                .expectStatus().isNotFound
        }

        @Test
        fun `should return NOT FOUND while trying to subscribe to non-existing device`() {
            // when/then
            webTestClient.get()
                .uri(getInvalidUrl)
                .exchange()
                .expectStatus().isNotFound
        }
    }
}