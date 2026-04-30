import Store from './store.js';
import type Device from "./models/device";

const baseUrl = 'http://localhost:8080/devices/'

export function createSseConnection(deviceId: number) {
    const getUrl = baseUrl + `${deviceId}/reachable-devices`;
    const sse = new EventSource(getUrl);
    Store.eventSource = sse;
    sse.addEventListener("INITIAL_STATE", (e) => {
        console.log(e.data);
        console.log(e.type)
    })
    sse.addEventListener("update", (e) => {
        console.log(e.data);
        console.log(e.type)
    })
    sse.onerror = (error) => {
        console.error('Event source failed: ', error);
    }
}

export function closeSseConnection() {
    Store.eventSource.close();
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
