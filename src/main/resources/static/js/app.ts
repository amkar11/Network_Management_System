import throwNullReferenceError from "./helpers/nullError.js";
import { toggleDevice, toggleSubscription, closePopup, closeOverlayByCross } from "./ui.js"
import seeder from "./seeder.js"

const gridContainer = document.querySelector(".grid-container") ??
    throwNullReferenceError("No grid container is found")

gridContainer.addEventListener("click", async (e) => {
    if ((e.target as HTMLElement).closest('.device-card .switch-container')) {
        await toggleDevice(e);
    } else if ((e.target as HTMLElement).closest('.device-card') &&
        !(e.target as HTMLElement).classList.contains('switch-container')) {
            toggleSubscription(e);
    }
})

document.addEventListener("click", (e) => {
    if ((e.target as HTMLElement).closest('#overlay-cross')) {
        closeOverlayByCross();
    } else if ((e.target as HTMLElement).closest('#popup-cross')) {
        closePopup();
    }

})

document.addEventListener("DOMContentLoaded", () => {
    seeder();
})