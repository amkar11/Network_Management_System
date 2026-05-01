import throwNullReferenceError from "./helpers/nullError.js";
import Store from "./store.js";
import { performPatchRequest, createSseConnection, closeSseConnection } from "./api.js";
import { clearOverlay } from "./overlay.js";

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

    toggleSubscription(e: Event) {
        const deviceCard
            = (e.target! as HTMLElement).closest(".device-card") as HTMLDivElement
            ?? throwNullReferenceError('Device is not found')

        const deviceId = Number(deviceCard.dataset.deviceId) ??
            throwNullReferenceError(`deviceId data attribute with id does not exist`);

        if (!Store.getDeviceById(deviceId)?.active) return;

        const overlay = document.querySelector('.connections-overlay') ??
            throwNullReferenceError(`Connections overlay not found`);

        if (deviceId === Store.currentSubscriptionId) {
            closeSseConnection();
            clearOverlay();
            Store.isConnectionsOverlayActive = false;
            Store.currentSubscriptionId = null;
            overlay.classList.remove('connections-overlay-active')
            return;
        }

        if (Store.eventSource !== null || deviceId !== Store.currentSubscriptionId) {
            closeSseConnection()
            clearOverlay();
            Store.currentSubscriptionId = null;
            Store.isConnectionsOverlayActive = false;
            overlay.classList.remove('connections-overlay-active')
        }

        createSseConnection(deviceId)
        Store.currentSubscriptionId = deviceId;
        overlay.classList.add('connections-overlay-active');
        const overlayTitle = overlay.querySelector('h2') ??
            throwNullReferenceError('Overlay h2 is not found');
        overlayTitle.textContent = `Reachable devices for device ${deviceId}`;
        Store.isConnectionsOverlayActive = true;
    }

    closeOverlayByCross(e: Event) {
        Store.currentSubscriptionId = null;
        Store.eventSource = null;
        Store.isConnectionsOverlayActive = false;

        const overlay = document.querySelector('.connections-overlay') ??
            throwNullReferenceError(`Connections overlay not found`);
        clearOverlay();
        overlay.classList.remove('connections-overlay-active')
    }
}