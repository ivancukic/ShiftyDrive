import { ILine } from "../entities/line/line.model";

export interface IUploadFileLine {
    insertedLines: ILine[],
    errorLines: string[],
}

export class UploadFileLine {
    constructor(
        public insertedLines: ILine[],
        public errorLines: string[],
    ) {}
}