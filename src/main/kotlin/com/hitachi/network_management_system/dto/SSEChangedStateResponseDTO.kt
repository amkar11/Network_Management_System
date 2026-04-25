package com.hitachi.network_management_system.dto

data class SSEChangedStateResponseDTO(override val type: String, val deviceId: Int) : SSEStateResponseDTO(type)