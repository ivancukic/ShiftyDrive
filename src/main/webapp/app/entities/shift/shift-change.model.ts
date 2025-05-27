import { IDriver } from "../driver/driver.model";
import { ILine } from "../line/line.model";
import { IShift } from "./shift.model";

export interface IShiftChange {
    driver: IDriver | null,
    line: ILine | null,
    driversShifts: IShift,
}

export class ShiftChange {
    constructor(
        public driver: IDriver,
        public line: ILine,
        public driversShifts: IShift
    ) {}
}