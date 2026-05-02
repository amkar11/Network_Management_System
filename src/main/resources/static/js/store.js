import Device from "./models/device.js";
class Store {
    constructor() {
        this.state = {
            devicesList: [],
            currentSubscriptionId: null,
            isConnectionsOverlayActive: false,
            imgLinksList: ['media/warszawa.jpg', 'media/krakow.webp', 'media/wroclaw.jpg', 'media/poznan.webp',
                'media/gdansk.jpg', 'media/szczecin.jpg', 'media/bydgoszcz.jpg', 'media/lublin.jpg', 'media/katowice.jpg',
                'media/bialystok.jpg', 'media/gdynia.jpg', 'media/czestochowa.jpg', 'media/radom.jpg', 'media/torun.jpg',
                'media/sosnowiec.jpg', 'media/kielce.jpg', 'media/gliwice.jpg', 'media/zabrze.jpg', 'media/olsztyn.jpg',
                'media/rzeszow.jpg'],
            eventSource: null
        };
        this.subscribers = new Set();
    }
    // devicesList
    get devicesList() {
        return this.state.devicesList;
    }
    getDeviceById(id) {
        return this.devicesList.find((device) => device.id === id);
    }
    addDevice(device) {
        this.devicesList.push(device);
    }
    //imgLinksList
    get imgLinksList() {
        return this.state.imgLinksList;
    }
    // currentSubscriptionId
    get currentSubscriptionId() {
        return this.state.currentSubscriptionId;
    }
    set currentSubscriptionId(newId) {
        this.state.currentSubscriptionId = newId;
    }
    // isConnectionsOverlayActive
    get isConnectionsOverlayActive() {
        return this.state.isConnectionsOverlayActive;
    }
    set isConnectionsOverlayActive(isActive) {
        this.state.isConnectionsOverlayActive = isActive;
    }
    // eventSource
    get eventSource() {
        return this.state.eventSource;
    }
    set eventSource(newEventSource) {
        this.state.eventSource = newEventSource;
    }
    // Subscribe and notify
    subscribe(callback) {
        this.subscribers.add(callback);
        callback();
        return () => this.subscribers.delete(callback);
    }
    notify() {
        for (const sub of this.subscribers) {
            sub();
        }
    }
}
export default new Store();
//# sourceMappingURL=store.js.map