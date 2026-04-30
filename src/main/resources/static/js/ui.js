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
        return __awaiter(this, void 0, void 0, function* () {
            var _a, _b, _c;
            const deviceCard = (_a = e.target.closest(".device-card")) !== null && _a !== void 0 ? _a : throwNullReferenceError('Device is not found');
            const deviceId = (_b = Number(deviceCard.dataset.deviceId)) !== null && _b !== void 0 ? _b : throwNullReferenceError(`deviceId data attribute with id does not exist`);
            if (!((_c = Store.getDeviceById(deviceId)) === null || _c === void 0 ? void 0 : _c.active))
                return;
            if (Store.eventSource !== null) {
                closeSseConnection();
            }
            if (deviceId === Store.currentSubscriptionId) {
                closeSseConnection();
                return;
            }
            createSseConnection(deviceId);
            Store.currentSubscriptionId = deviceId;
        });
    }
}
//# sourceMappingURL=ui.js.map