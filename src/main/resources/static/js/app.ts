import throwNullReferenceError from "./helpers/nullError.js";
import Ui from "./ui.js"
import seeder from "./seeder.js"

const gridContainer = document.querySelector(".grid-container") ??
    throwNullReferenceError("No grid container is found")
const ui = new Ui();

gridContainer.addEventListener("click", async (e) => {
    if ((e.target as HTMLElement).closest('.device-card .switch-container')) {
        await ui.toggleDevice(e);
    } else if ((e.target as HTMLElement).closest('.device-card')) {
        await ui.toggleSubscription(e);
    }
})

document.addEventListener("DOMContentLoaded", (e) => {
    seeder();
})