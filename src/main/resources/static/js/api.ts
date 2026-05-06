import Store from './store.js';
import type Device from "./models/device";
import { drawInitialState, addOrDeleteDeviceCard } from "./overlay.js";

const baseUrl = 'http://localhost:8080/devices/'

export function createSseConnection(deviceId: number) {
    const getUrl = baseUrl + `${deviceId}/reachable-devices`;
    Store.eventSource = new EventSource(getUrl);
    console.log("New event source created")
    Store.eventSource.addEventListener("INITIAL_STATE", (e) => {
        drawInitialState(e.data)
    })
    Store.eventSource.addEventListener("update", (e) => {
        addOrDeleteDeviceCard(e.data)
        console.log(e.data)
    })
    Store.eventSource.onerror = (error) => {
        console.error('Event source failed: ', error);
    }
}

export function closeSseConnection() {
    if (Store.eventSource !== null) {
        Store.eventSource.close();
        Store.eventSource = null;
    }
}

export async function performPatchRequest(deviceId: number, active: boolean): Promise<Device> {
    const body = {
        active: active,
    }
    const response = await fetch(baseUrl + deviceId.toString(), {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(body),
    })
    if (!response.ok) {
        throw new Error(`Unable to performPatchRequest on device with id: ${deviceId}`);
    }
    return await response.json() as Device;
    }
