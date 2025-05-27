import { Injectable } from "@angular/core";
import { ILine } from "../entities/line/line.model";

@Injectable({
    providedIn: 'root'
})
export class ShiftLineService {
    private lineToShift: ILine | null = null;
    
    setLine(line: ILine) {
        this.lineToShift = line;
    }

    getLine(): ILine | null {
        return this.lineToShift;
    }

    clear() {
        this.lineToShift = null;
    }
}