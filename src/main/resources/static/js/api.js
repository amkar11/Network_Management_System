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
import { drawInitialState, addOrDeleteDeviceCard } from "./overlay.js";
const baseUrl = 'http://localhost:8080/devices/';
export function createSseConnection(deviceId) {
    const getUrl = baseUrl + `${deviceId}/reachable-devices`;
    Store.eventSource = new EventSource(getUrl);
    console.log("New event source created");
    Store.eventSource.addEventListener("INITIAL_STATE", (e) => {
        drawInitialState(e.data);
    });
    Store.eventSource.addEventListener("update", (e) => {
        addOrDeleteDeviceCard(e.data);
        console.log(e.data);
    });
    Store.eventSource.onerror = (error) => {
        console.error('Event source failed: ', error);
    };
}
export function closeSseConnection() {
    if (Store.eventSource !== null) {
        Store.eventSource.close();
        Store.eventSource = null;
    }
}
export function performPatchRequest(deviceId, active) {
    return __awaiter(this, void 0, void 0, function* () {
        const body = {
            active: active,
        };
        const response = yield fetch(baseUrl + deviceId.toString(), {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(body),
        });
        if (!response.ok) {
            throw new Error(`Unable to performPatchRequest on device with id: ${deviceId}`);
        }
        return yield response.json();
    });
}
//# sourceMappingURL=api.js.map