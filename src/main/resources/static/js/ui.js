var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
import throwNullReferenceError from "./helpers/nullError.js";
import Store from "./store.js";
import { performPatchRequest, createSseConnection, closeSseConnection } from "./api.js";
import { clearOverlay } from "./overlay.js";
export default class Ui {
    toggleDevice(e) {
        return __awaiter(this, void 0, void 0, function* () {
            var _a, _b, _c, _d, _e;
            const deviceCard = (_a = e.target.closest(".device-card")) !== null && _a !== void 0 ? _a : throwNullReferenceError('Device is not found');
            const deviceId = (_b = Number(deviceCard.dataset.deviceId)) !== null && _b !== void 0 ? _b : throwNullReferenceError(`deviceId data attribute with id does not exist`);
            const deviceStore = (_c = Store.getDeviceById(deviceId)) !== null && _c !== void 0 ? _c : throwNullReferenceError(`There is no device with id ${deviceId} in the store`);
            const patchDeviceResponse = yield performPatchRequest(deviceStore.id, !deviceStore.active);
            deviceStore.active = patchDeviceResponse.active;
            const switchButton = (_d = deviceCard.querySelector(".switch")) !== null && _d !== void 0 ? _d : throwNullReferenceError('Switch is not found');
            const switchSpan = (_e = deviceCard.querySelector(".switch-background span")) !== null && _e !== void 0 ? _e : throwNullReferenceError('Switch is not found');
            deviceCard.classList.toggle('device-card-turned-off');
            switchButton.classList.toggle('switch-turned-off');
            switchSpan.classList.toggle('switch-background-span-turned-off');
            switchSpan.textContent = (switchSpan.textContent == 'Turned on') ? 'Turned off' : 'Turned on';
        });
    }
    toggleSubscription(e) {
        var _a, _b, _c, _d, _e;
        const deviceCard = (_a = e.target.closest(".device-card")) !== null && _a !== void 0 ? _a : throwNullReferenceError('Device is not found');
        const deviceId = (_b = Number(deviceCard.dataset.deviceId)) !== null && _b !== void 0 ? _b : throwNullReferenceError(`deviceId data attribute with id does not exist`);
        if (!((_c = Store.getDeviceById(deviceId)) === null || _c === void 0 ? void 0 : _c.active))
            return;
        const overlay = (_d = document.querySelector('.connections-overlay')) !== null && _d !== void 0 ? _d : throwNullReferenceError(`Connections overlay not found`);
        if (deviceId === Store.currentSubscriptionId) {
            closeSseConnection();
            clearOverlay();
            Store.isConnectionsOverlayActive = false;
            Store.currentSubscriptionId = null;
            overlay.classList.remove('connections-overlay-active');
            return;
        }
        if (Store.eventSource !== null || deviceId !== Store.currentSubscriptionId) {
            closeSseConnection();
            clearOverlay();
            Store.currentSubscriptionId = null;
            Store.isConnectionsOverlayActive = false;
            overlay.classList.remove('connections-overlay-active');
        }
        createSseConnection(deviceId);
        Store.currentSubscriptionId = deviceId;
        overlay.classList.add('connections-overlay-active');
        const overlayTitle = (_e = overlay.querySelector('h2')) !== null && _e !== void 0 ? _e : throwNullReferenceError('Overlay h2 is not found');
        overlayTitle.textContent = `Reachable devices for device ${deviceId}`;
        Store.isConnectionsOverlayActive = true;
    }
    closeOverlayByCross(e) {
        var _a;
        Store.currentSubscriptionId = null;
        Store.eventSource = null;
        Store.isConnectionsOverlayActive = false;
        const overlay = (_a = document.querySelector('.connections-overlay')) !== null && _a !== void 0 ? _a : throwNullReferenceError(`Connections overlay not found`);
        clearOverlay();
        overlay.classList.remove('connections-overlay-active');
    }
}
//# sourceMappingURL=ui.js.map