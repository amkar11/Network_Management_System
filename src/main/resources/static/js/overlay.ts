import Store from './store.js'
import throwNullReferenceError from "./helpers/nullError.js";
import type Device from "./models/device.js";

export function drawInitialState(initialStateJson: string) {
    const initialState = JSON.parse(initialStateJson) ??
        throwNullReferenceError("initialState must be object parsed from JSON");
    const gridConnectionsContainer = document.querySelector('.grid-connections-container') ??
        throwNullReferenceError("Grid connections container is not found");
    for (const deviceId of initialState.deviceIds) {
        const deviceStore = Store.getDeviceById(deviceId) ??
            throwNullReferenceError(`There is no device with id ${deviceId} in the store`);
        const deviceCard = drawDeviceCard(deviceStore);
        gridConnectionsContainer.appendChild(deviceCard);
    }
}

export function addOrDeleteDeviceCard(updateDataJson: string) {
    const updateData = JSON.parse(updateDataJson) ??
        throwNullReferenceError("updateData must be object parsed from JSON");
    const deviceStore = Store.getDeviceById(updateData.deviceId) ??
        throwNullReferenceError(`There is no device with id ${updateData.deviceId} in the store`);
    const gridConnectionsContainer = document.querySelector('.grid-connections-container') ??
        throwNullReferenceError("Grid connections container is not found");
    if (updateData.type === 'ADDED') {
        const deviceCard = drawDeviceCard(deviceStore);
        gridConnectionsContainer.appendChild(deviceCard);
    } else if (updateData.type === 'REMOVED') {
        const deviceCard = document
                .querySelector(`.grid-connections-container .device-card[data-device-id="${updateData.deviceId}"]`) ??
            throwNullReferenceError(`There is no card with such id ${updateData.deviceId}`);
        gridConnectionsContainer.removeChild(deviceCard);
    }
}

export function clearOverlay() {
    const deviceCards = document.querySelectorAll('.grid-connections-container .device-card');
    const gridConnectionsContainer = document.querySelector('.grid-connections-container') ??
        throwNullReferenceError("Grid connections container is not found");
    for (const device of deviceCards) {
        gridConnectionsContainer.removeChild(device);
    }
}

export function drawDeviceCard(device: Device): HTMLDivElement {
    // Create device card elements
    const deviceCard = document.createElement('div');
    deviceCard.classList.add('device-card');
    deviceCard.dataset.deviceId = device.id.toString();
    const cityNameSpan = document.createElement('span');
    cityNameSpan.textContent = `${device.name} - id: ${device.id}`;
    const cityImage = document.createElement('img');
    cityImage.src = Store.imgLinksList[device.id] ??
        throwNullReferenceError("Image was not found in the imgLinksList");
    cityImage.alt = device.name;

    // Assemble device card
    deviceCard.appendChild(cityNameSpan);
    deviceCard.appendChild(cityImage);
    return deviceCard;
}