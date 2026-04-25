package com.hitachi.network_management_system.dto

data class SSEInitStateResponseDTO(override val type: String, val deviceIds: List<Int>) : SSEStateResponseDTO(type)