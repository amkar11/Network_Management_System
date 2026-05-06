package com.hitachi.network_management_system.daos

import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class ConnectionsDaoTests(
    private val connectionsDAO: IConnectionsDAO,
) {

    //INITIAL DATA GETS SEEDED FROM @POSTCONSTRUCT METHOD IN TopologyDbSeeder CLASS
        @Test
        fun `should return all connections`() = runTest {
            // when
            val connections = connectionsDAO.getAllConnections()

            // then
            assertThat(connections).hasSize(28)
        }
    }
