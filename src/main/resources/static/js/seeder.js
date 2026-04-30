import topology from '../topology.json' with { type: 'json' };
import Device from './models/device.js';
import Store from './store.js';
import throwNullReferenceError from './helpers/nullError.js';
export default function seeder() {
    var _a, _b;
    const imgLinks = Store.imgLinksList;
    const gridContainer = (_a = document.querySelector(".grid-container")) !== null && _a !== void 0 ? _a : throwNullReferenceError('Grid container is not found');
    for (let i = 0; i < topology.devices.length; i++) {
        // device-card div
        const deviceCard = document.createElement("div");
        deviceCard.classList.add("device-card");
        deviceCard.dataset.deviceId = i.toString();
        // Cast to Device class
        if (topology.devices[i] === undefined)
            throwNullReferenceError(`No device found with id ${i}`);
        const device = topology.devices[i];
        Store.addDevice(device);
        // City name span
        const cityName = document.createElement("span");
        cityName.textContent = device.name;
        // Image
        const img = document.createElement("img");
        img.src = (_b = imgLinks[i]) !== null && _b !== void 0 ? _b : '';
        img.alt = device.name;
        // Switch container, background and button
        const switchContainer = document.createElement("div");
        switchContainer.classList.add("switch-container");
        const switchBackground = document.createElement("div");
        switchBackground.classList.add("switch-background");
        const backgroundSpan = document.createElement("span");
        backgroundSpan.textContent = 'Turned on';
        const switchButton = document.createElement("div");
        switchButton.classList.add("switch");
        // Assemble card
        switchBackground.append(backgroundSpan);
        switchContainer.append(switchBackground);
        switchContainer.append(switchButton);
        deviceCard.append(cityName);
        deviceCard.append(img);
        deviceCard.append(switchContainer);
        // Append card to grid-container
        gridContainer.append(deviceCard);
    }
}
//# sourceMappingURL=seeder.js.map