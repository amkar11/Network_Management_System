var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var _a;
import throwNullReferenceError from "./helpers/nullError.js";
import Ui from "./ui.js";
import seeder from "./seeder.js";
const gridContainer = (_a = document.querySelector(".grid-container")) !== null && _a !== void 0 ? _a : throwNullReferenceError("No grid container is found");
const ui = new Ui();
gridContainer.addEventListener("click", (e) => __awaiter(void 0, void 0, void 0, function* () {
    if (e.target.closest('.device-card .switch-container')) {
        yield ui.toggleDevice(e);
    }
    else if (e.target.closest('.device-card')) {
        ui.toggleSubscription(e);
    }
}));
document.addEventListener("click", (e) => {
    if (e.target.closest('#overlay-cross')) {
        ui.closeOverlayByCross(e);
    }
});
document.addEventListener("DOMContentLoaded", (e) => {
    seeder();
});
//# sourceMappingURL=app.js.map