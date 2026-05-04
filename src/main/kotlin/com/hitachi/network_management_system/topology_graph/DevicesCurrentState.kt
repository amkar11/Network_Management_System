package com.hitachi.network_management_system.topology_graph

import org.springframework.stereotype.Component

@Component
class DevicesCurrentState {
    val devicesCurrentState: MutableMap<Int, List<Int>> = mutableMapOf()
}