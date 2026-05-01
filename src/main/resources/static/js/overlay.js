import Store from './store.js';
import throwNullReferenceError from "./helpers/nullError.js";
export function drawInitialState(initialStateJson) {
    var _a, _b, _c;
    const initialState = (_a = JSON.parse(initialStateJson)) !== null && _a !== void 0 ? _a : throwNullReferenceError("initialState must be object parsed from JSON");
    const gridConnectionsContainer = (_b = document.querySelector('.grid-connections-container')) !== null && _b !== void 0 ? _b : throwNullReferenceError("Grid connections container is not found");
    for (const deviceId of initialState.deviceIds) {
        const deviceStore = (_c = Store.getDeviceById(deviceId)) !== null && _c !== void 0 ? _c : throwNullReferenceError(`There is no device with id ${deviceId} in the store`);
        const deviceCard = drawDeviceCard(deviceStore);
        gridConnectionsContainer.appendChild(deviceCard);
    }
}
export function addOrDeleteDeviceCard(updateDataJson) {
    var _a, _b, _c, _d;
    const updateData = (_a = JSON.parse(updateDataJson)) !== null && _a !== void 0 ? _a : throwNullReferenceError("updateData must be object parsed from JSON");
    const deviceStore = (_b = Store.getDeviceById(updateData.deviceId)) !== null && _b !== void 0 ? _b : throwNullReferenceError(`There is no device with id ${updateData.deviceId} in the store`);
    const gridConnectionsContainer = (_c = document.querySelector('.grid-connections-container')) !== null && _c !== void 0 ? _c : throwNullReferenceError("Grid connections container is not found");
    if (updateData.type === 'ADDED') {
        const deviceCard = drawDeviceCard(deviceStore);
        gridConnectionsContainer.appendChild(deviceCard);
    }
    else if (updateData.type === 'REMOVED') {
        const deviceCard = (_d = document
            .querySelector(`.grid-connections-container .device-card[data-device-id="${updateData.deviceId}"]`)) !== null && _d !== void 0 ? _d : throwNullReferenceError(`There is no card with such id ${updateData.deviceId}`);
        gridConnectionsContainer.removeChild(deviceCard);
    }
}
export function clearOverlay() {
    var _a;
    const deviceCards = document.querySelectorAll('.grid-connections-container .device-card');
    const gridConnectionsContainer = (_a = document.querySelector('.grid-connections-container')) !== null && _a !== void 0 ? _a : throwNullReferenceError("Grid connections container is not found");
    for (const device of deviceCards) {
        gridConnectionsContainer.removeChild(device);
    }
}
export function drawDeviceCard(device) {
    var _a;
    // Create device card elements
    const deviceCard = document.createElement('div');
    deviceCard.classList.add('device-card');
    deviceCard.dataset.deviceId = device.id.toString();
    const cityNameSpan = document.createElement('span');
    cityNameSpan.textContent = `${device.name} - id: ${device.id}`;
    const cityImage = document.createElement('img');
    cityImage.src = (_a = Store.imgLinksList[device.id]) !== null && _a !== void 0 ? _a : throwNullReferenceError("Image was not found in the imgLinksList");
    cityImage.alt = device.name;
    // Assemble device card
    deviceCard.appendChild(cityNameSpan);
    deviceCard.appendChild(cityImage);
    return deviceCard;
}
//# sourceMappingURL=overlay.js.map