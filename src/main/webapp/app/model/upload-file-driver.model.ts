import { IDriver } from "../entities/driver/driver.model";

export interface IUploadFileDriver {
    insertedDrivers: IDriver[],
    errorDrivers: string[],
}

export class UploadFileDriver {
    constructor(
        public insertedDrivers: IDriver[],
        public errorDrivers: string[],
    ) {}
}