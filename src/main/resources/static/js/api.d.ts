import type Device from "./models/device";
export declare function createSseConnection(deviceId: number): void;
export declare function closeSseConnection(): void;
export declare function performPatchRequest(deviceId: number, active: boolean): Promise<Device>;
//# sourceMappingURL=api.d.ts.map