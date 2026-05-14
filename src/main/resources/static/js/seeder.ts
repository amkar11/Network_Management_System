import topology from '../topology.json' with {type: 'json'}
import Device from './models/device.js'
import Store from './store.js'
import {getAllDevices} from "./api.js";
import throwNullReferenceError from './helpers/nullError.js'

// By default, devices are fetched from topology
// I will try to fetch devices from API

export default async function seeder() {
    const imgLinks = Store.imgLinksList
    const gridContainer = document.querySelector(".grid-container")
        ?? throwNullReferenceError('Grid container is not found');
    let devices: Device[] = [];
    if (sessionStorage.getItem('devices') !== null) {
        devices = JSON.parse(sessionStorage.getItem('devices') as string) as Device[];
    }
    let devicesApi = await getAllDevices()


    for (let i = 0; i < topology.devices.length; i++) {
        // device-card div
        const deviceCard = document.createElement("div");
        deviceCard.classList.add("device-card");
        deviceCard.dataset.deviceId = i.toString();

        // Cast to Device class
        if (topology.devices[i] === undefined)
            throwNullReferenceError(`No device found with id ${i}`);
        const device = (topology.devices[i] as Device)
        Store.addDevice(device)

        // City name span
        const cityName = document.createElement("span");
        cityName.textContent = `${device.name} - id: ${device.id}`;

        // Image
        const img = document.createElement("img");
        img.src = imgLinks[i] ?? '';
        img.alt = device.name;

        // Switch container, background and button
        const switchContainer = document.createElement("div");
        switchContainer.classList.add("switch-container");
        const switchBackground = document.createElement("div");
        switchBackground.classList.add("switch-background");
        const backgroundSpan = document.createElement("span");
        const switchButton = document.createElement("div");
        switchButton.classList.add("switch");
        backgroundSpan.textContent = "Turned on";
        if (devices.length > 0) {
            const device = devices[i] as Device;
            backgroundSpan.textContent = (device.active) ? 'Turned on' : 'Turned off';
            if (!device.active) {
                switchButton.classList.add('switch-turned-off');
                deviceCard.classList.add('device-card-turned-off');
                switchContainer.classList.add('switch-background-span-turned-off');
            }
            Store.devicesList[i]!!.active = device.active;
        }

        // Assemble card
        switchBackground.append(backgroundSpan)
        switchContainer.append(switchBackground);
        switchContainer.append(switchButton);
        deviceCard.append(cityName)
        deviceCard.append(img)
        deviceCard.append(switchContainer)

        // Append card to grid-container
        gridContainer.append(deviceCard)
    }
}
