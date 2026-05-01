import Device from "./models/device.js";
declare class Store {
    private state;
    private subscribers;
    get devicesList(): Device[];
    getDeviceById(id: number): Device | undefined;
    addDevice(device: Device): void;
    get imgLinksList(): string[];
    get currentSubscriptionId(): number | null;
    set currentSubscriptionId(newId: number | null);
    get isConnectionsOverlayActive(): boolean;
    set isConnectionsOverlayActive(isActive: boolean);
    get eventSource(): EventSource | null;
    set eventSource(newEventSource: EventSource | null);
    subscribe(callback: Function): () => boolean;
    private notify;
}
declare const _default: Store;
export default _default;
//# sourceMappingURL=store.d.ts.map