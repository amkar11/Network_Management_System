import throwNullReferenceError from "./helpers/nullError.js";
import Store from "./store.js";
import { performPatchRequest, createSseConnection, closeSseConnection } from "./api.js";

export default class Ui {

    async toggleDevice(e: Event) {
        const deviceCard
            = (e.target! as HTMLElement).closest(".device-card") as HTMLDivElement
            ?? throwNullReferenceError('Device is not found')

        const deviceId = Number(deviceCard.dataset.deviceId) ??
            throwNullReferenceError(`deviceId data attribute with id does not exist`);
        const deviceStore = Store.getDeviceById(deviceId) ??
        throwNullReferenceError(`There is no device with id ${deviceId} in the store`);

        const patchDeviceResponse = await performPatchRequest(deviceStore.id, !deviceStore.active)
        deviceStore.active = patchDeviceResponse.active

        const switchButton = deviceCard.querySelector(".switch") ??
            throwNullReferenceError('Switch is not found');
        const switchSpan = deviceCard.querySelector(".switch-background span") ??
            throwNullReferenceError('Switch is not found');
        deviceCard.classList.toggle('device-card-turned-off')
        switchButton.classList.toggle('switch-turned-off');
        switchSpan.classList.toggle('switch-background-span-turned-off');
        switchSpan.textContent = (switchSpan.textContent == 'Turned on') ? 'Turned off' : 'Turned on'
    }

    async toggleSubscription(e: Event) {
        const deviceCard
            = (e.target! as HTMLElement).closest(".device-card") as HTMLDivElement
            ?? throwNullReferenceError('Device is not found')

        const deviceId = Number(deviceCard.dataset.deviceId) ??
            throwNullReferenceError(`deviceId data attribute with id does not exist`);

        if (!Store.getDeviceById(deviceId)?.active) return;
        if (Store.eventSource !== null) {
            closeSseConnection()
        }
        if (deviceId === Store.currentSubscriptionId) {
            closeSseConnection();
            return;
        }

        createSseConnection(deviceId)
        Store.currentSubscriptionId = deviceId;

    }
}