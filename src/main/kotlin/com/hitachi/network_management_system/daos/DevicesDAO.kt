package com.hitachi.network_management_system.daos

import com.hitachi.network_management_system.dto.DeviceDTO
import com.hitachi.network_management_system.repositories.IDevicesRepository
import com.hitachi.network_management_system.topology_db.DeviceDB
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service

@Service
class DevicesDAO(
    val devicesRepository: IDevicesRepository
) : IDevicesDAO {
     override suspend fun getAllDevices(): List<DeviceDB> {
        val devices =  devicesRepository.findAll().toList()
        if (devices.isEmpty()) {
            throw IllegalStateException("There must be list of devices in the database")
        }
        return devices
    }

    override suspend fun changeDevice(id: Int, isActive: Boolean): DeviceDB {
        val device = getDevice(id)
        if (device.active == isActive) return device
        val updated = device.copy(active = isActive)
        return devicesRepository.save(updated)
    }

     override suspend fun getDevice(id: Int): DeviceDB {
        val deviceDB = devicesRepository.findById(id) ?:
        throw NoSuchElementException("No device found with id $id")
        return deviceDB
    }

     override suspend fun createDevices(devices: List<DeviceDTO>) {
        val devicesDB = devices.map { DeviceDB(null, it.name, it.active) }
        devicesRepository.saveAll(devicesDB).collect()
    }

}