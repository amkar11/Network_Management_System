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
    const element = e.target as HTMLElement;

    if (element.closest('#overlay-cross')) {
        closeOverlayByCross();
    } else if (element.closest('#popup-cross') ||
            element.closest('.popup-switch-container')) {
        closePopup(element.closest('.popup-switch-container') as Element);
    }
})

document.addEventListener("DOMContentLoaded", async () => {
    await seeder();
})