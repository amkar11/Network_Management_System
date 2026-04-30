import Device from "./models/device.js";
declare class Store {
    private state;
    private subscribers;
    get devicesList(): Device[];
    getDeviceById(id: number): Device | undefined;
    addDevice(device: Device): void;
    get imgLinksList(): string[];
    get currentSubscriptionId(): number;
    set currentSubscriptionId(newId: number);
    get isConnectionsOverlayActive(): boolean;
    set isConnectionsOverlayActive(isActive: boolean);
    get eventSource(): EventSource;
    set eventSource(newEventSource: EventSource);
    subscribe(callback: Function): () => boolean;
    private notify;
}
declare const _default: Store;
export default _default;
//# sourceMappingURL=store.d.ts.map