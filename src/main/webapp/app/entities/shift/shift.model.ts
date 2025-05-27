import { IDriver } from "../driver/driver.model";
import { ILine } from "../line/line.model";

export interface IShift {
    driver: IDriver | null,
    line: ILine | null,
    id?: number,
}

export class Shift {
    constructor(
        public driver: IDriver,
        public line: ILine,
        public id?: number,
    ) {}
}