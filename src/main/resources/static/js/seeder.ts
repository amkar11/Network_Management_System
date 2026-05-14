import Store from './store.js'
import { getAllDevices } from "./api.js";
import throwNullReferenceError from './helpers/nullError.js'

export default async function seeder() {
    const imgLinks = Store.imgLinksList
    const gridContainer = document.querySelector(".grid-container")
        ?? throwNullReferenceError('Grid container is not found');
    let devices = await getAllDevices();

    for (let i = 0; i < devices.length; i++) {
        // device-card div
        const deviceCard = document.createElement("div");
        deviceCard.classList.add("device-card");
        deviceCard.dataset.deviceId = i.toString();

        // Cast to Device class
        const device = devices[i] ??
            throwNullReferenceError(`No device found with id ${i}`);
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
        backgroundSpan.textContent = (device.active) ? 'Turned on' : 'Turned off';
        if (!device.active) {
            switchButton.classList.add('switch-turned-off');
            deviceCard.classList.add('device-card-turned-off');
            backgroundSpan.classList.add('switch-background-span-turned-off');
        }
        Store.devicesList[i]!!.active = device.active;


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
