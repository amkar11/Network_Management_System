var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
import Store from './store.js';
import { getAllDevices } from "./api.js";
import throwNullReferenceError from './helpers/nullError.js';
export default function seeder() {
    return __awaiter(this, void 0, void 0, function* () {
        var _a, _b, _c;
        const imgLinks = Store.imgLinksList;
        const gridContainer = (_a = document.querySelector(".grid-container")) !== null && _a !== void 0 ? _a : throwNullReferenceError('Grid container is not found');
        let devices = yield getAllDevices();
        for (let i = 0; i < devices.length; i++) {
            // device-card div
            const deviceCard = document.createElement("div");
            deviceCard.classList.add("device-card");
            deviceCard.dataset.deviceId = i.toString();
            // Cast to Device class
            const device = (_b = devices[i]) !== null && _b !== void 0 ? _b : throwNullReferenceError(`No device found with id ${i}`);
            Store.addDevice(device);
            // City name span
            const cityName = document.createElement("span");
            cityName.textContent = `${device.name} - id: ${device.id}`;
            // Image
            const img = document.createElement("img");
            img.src = (_c = imgLinks[i]) !== null && _c !== void 0 ? _c : '';
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
            Store.devicesList[i].active = device.active;
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
    });
}
//# sourceMappingURL=seeder.js.map