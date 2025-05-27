import { IDriver } from "../driver/driver.model";
import { ILine } from "../line/line.model";

export interface IShiftCreate {
    drivers: IDriver[] | null,
    line: ILine | null,
    id?: number,
}

export class ShiftCreate {
    constructor(
        public drivers: IDriver[],
        public line: ILine,
        public id?: number,
    ) {}
}