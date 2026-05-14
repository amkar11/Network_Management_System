import Device from "./models/device.js";

class Store {
     private state = {
        devicesList: [] as Device[],
        currentSubscriptionId: null as number | null,
        isConnectionsOverlayActive: false,
         imgLinksList: ['media/warszawa.jpg', 'media/krakow.webp', 'media/wroclaw.jpg', 'media/poznan.webp',
             'media/gdansk.jpg', 'media/szczecin.jpg', 'media/bydgoszcz.jpg', 'media/lublin.jpg', 'media/katowice.jpg',
             'media/bialystok.jpg', 'media/gdynia.jpg', 'media/czestochowa.jpg', 'media/radom.jpg', 'media/torun.jpg',
             'media/sosnowiec.jpg', 'media/kielce.jpg', 'media/gliwice.jpg', 'media/zabrze.jpg', 'media/olsztyn.jpg',
             'media/rzeszow.jpg'],
         eventSource: null as EventSource | null,
         isPopupSwitchTurnedOff: true
    }

     get devicesList() {
         return this.state.devicesList;
     }
    getDeviceById(id: number) {
        return this.devicesList.find((device) => device.id === id);
    }

    addDevice(device: Device) {
        this.devicesList.push(device);
    }

    get imgLinksList() {
         return this.state.imgLinksList;
    }

     get currentSubscriptionId() {
         return this.state.currentSubscriptionId;
     }
     set currentSubscriptionId(newId: number | null) {
        this.state.currentSubscriptionId = newId;
     }

    get isConnectionsOverlayActive() {
         return this.state.isConnectionsOverlayActive;
    }
    set isConnectionsOverlayActive(isActive: boolean) {
         this.state.isConnectionsOverlayActive = isActive;
    }

    get eventSource() {
         return this.state.eventSource;
    }
    set eventSource(newEventSource: EventSource | null) {
        this.state.eventSource = newEventSource;
    }

    get isPopupSwitchTurnedOff() {
         return this.state.isPopupSwitchTurnedOff;
    }
    set isPopupSwitchTurnedOff(newState: boolean) {
         this.state.isPopupSwitchTurnedOff = newState;
    }
}

export default new Store();